/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package cn.com.lowe.android.tools.net.response;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}. Receives response body as byte array with a
 * content-type whitelist. (e.g. checks Content-Type against allowed list,
 * Content-length).
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * String[] allowedTypes = new String[] { &quot;image/png&quot; };
 * client.get(&quot;http://www.example.com/image.png&quot;, new BinaryHttpResponseHandler(allowedTypes) {
 * 	&#064;Override
 * 	public void onSuccess(byte[] imageData) {
 * 		// Successfully got a response
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(Throwable e, byte[] imageData) {
 * 		// Response failed :(
 * 	}
 * });
 * </pre>
 */
public abstract class BinaryHttpResponseHandler extends HttpResponseHandler {
	// Allow images by default
	private static String[] mAllowedContentTypes = new String[] {"application/octet-stream" };

	public BinaryHttpResponseHandler() {
		super();
	}

	public BinaryHttpResponseHandler(String[] allowedContentTypes) {
		this();
		mAllowedContentTypes = allowedContentTypes;
	}

	protected abstract void onSuccess(int statusCode, Header[] headers, byte[] content);

	@Override
	protected void onSuccess(int statusCode, Header[] headers, Object content) {
		onSuccess(statusCode, headers, (byte[]) content);

	}

	@Override
	public void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		Header[] contentTypeHeaders = response.getHeaders("Content-Type");
		byte[] responseBody = null;
		if (contentTypeHeaders.length != 1) {
			sendFailureMessage(new HttpResponseException(status.getStatusCode(), "None, or more than one, Content-Type Header found!"), responseBody);
			return;
		}
		Header contentTypeHeader = contentTypeHeaders[0];
		boolean foundAllowedContentType = false;
		for (String anAllowedContentType : mAllowedContentTypes) {
			if (Pattern.matches(anAllowedContentType, contentTypeHeader.getValue())) {
				foundAllowedContentType = true;
			}
		}
		if (!foundAllowedContentType) {
			sendFailureMessage(new HttpResponseException(status.getStatusCode(), "Content-Type not allowed!"), responseBody);
			return;
		}
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
			}
			responseBody = EntityUtils.toByteArray(entity);
		} catch (IOException e) {
			sendFailureMessage(e, responseBody);
		}

		if (status.getStatusCode() >= 300) {
			sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
		} else {
			sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
		}
	}

}