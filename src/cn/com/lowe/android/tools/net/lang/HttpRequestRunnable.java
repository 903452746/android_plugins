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

package cn.com.lowe.android.tools.net.lang;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

import cn.com.lowe.android.tools.net.response.HttpResponseHandler;

public class HttpRequestRunnable implements Runnable {
	private final AbstractHttpClient client;
	private final HttpContext context;
	private final HttpUriRequest request;
	private final HttpResponseHandler responseHandler;

	public HttpRequestRunnable(AbstractHttpClient client, HttpContext context, HttpUriRequest request, HttpResponseHandler responseHandler) {
		this.client = client;
		this.context = context;
		this.request = request;
		this.responseHandler = responseHandler;
	}

	@Override
	public void run() {

		if (responseHandler != null) {
			responseHandler.sendStartMessage();
		}
		try {
			HttpResponse response = client.execute(request, context);
			if (responseHandler != null) {
				responseHandler.sendResponseMessage(response);
			}
		} catch (UnknownHostException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(e, "can't resolve host");
			}
			return;
		} catch (SocketException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(e, "can't resolve host");
			}
			return;
		} catch (SocketTimeoutException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(e, "socket time out");
			}
			return;
		} catch (IOException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(e, "network io error");
			}
			return;
		} catch (NullPointerException e) {
			if (responseHandler != null) {
				responseHandler.sendFailureMessage(e, "someobject is null");
			}
			return;
		}
		if (responseHandler != null) {
			responseHandler.sendFinishMessage();
		}

	}
}
