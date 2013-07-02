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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;


/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}, with automatic parsing into a {@link JSONObject} or
 * {@link JSONArray}.
 * <p>
 * This class is designed to be passed to get, post, put and delete requests
 * with the {@link #onSuccess(JSONObject)} or {@link #onSuccess(JSONArray)}
 * methods anonymously overridden.
 * <p>
 * Additionally, you can override the other event methods from the parent class.
 */
public abstract class HtmlHttpResponseHandler extends HttpResponseHandler {
	protected abstract void onSuccess(int statusCode, Header[] headers, String response);

	@Override
	protected void onSuccess(int statusCode, Header[] headers, Object content) {
		if (statusCode != HttpStatus.SC_NO_CONTENT) {
			onSuccess(statusCode, headers, (String) content);
		}
	}

	@Override
	public void sendResponseMessage(HttpResponse response) {
		StatusLine status = response.getStatusLine();
		String responseBody = null;
		try {
			HttpEntity entity = null;
			HttpEntity temp = response.getEntity();
			if (temp != null) {
				entity = new BufferedHttpEntity(temp);
				responseBody = EntityUtils.toString(entity, ENCODING);
			}
		} catch (IOException e) {
			sendFailureMessage(e, (String) null);
		}

		if (status.getStatusCode() >= 300) {
			sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
		} else {
			sendSuccessMessage(status.getStatusCode(), response.getAllHeaders(), responseBody);
		}
		
	}
}
