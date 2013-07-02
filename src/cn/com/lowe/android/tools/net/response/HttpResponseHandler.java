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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

/**
 * Used to intercept and handle the responses from requests made using
 * {@link AsyncHttpClient}. The {@link #onSuccess(String)} method is designed to
 * be anonymously overridden with your own response handling code.
 * <p>
 * Additionally, you can override the {@link #onFailure(Throwable, String)},
 * {@link #onStart()}, and {@link #onFinish()} methods as required.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onStart() {
 * 		// Initiated the request
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(String response) {
 * 		// Successfully got a response
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(Throwable e, String response) {
 * 		// Response failed :(
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFinish() {
 * 		// Completed the request (either success or failure)
 * 	}
 * });
 * </pre>
 */
public abstract class HttpResponseHandler {
	private static final String TAG = "HttpResponseHandler";

	protected static final int SUCCESS_MESSAGE = 0;
	protected static final int FAILURE_MESSAGE = 1;
	protected static final int START_MESSAGE = 2;
	protected static final int FINISH_MESSAGE = 3;

	private Handler handler;
	protected String ENCODING = "UTF-8";

	/**
	 * Creates a new AsyncHttpResponseHandler
	 */
	public HttpResponseHandler() {
		if (Looper.myLooper() != null) {
			handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					HttpResponseHandler.this.handleMessage(msg);
				}
			};
		}
	}

	protected abstract void onSuccess(int statusCode, Header[] headers, Object content);

	public abstract void sendResponseMessage(HttpResponse response);

	protected void onStart() {
		Log.d(TAG, "http request -onStart");
	};

	protected void onFinish() {
		Log.d(TAG, "http request -onFinish");
	};

	protected void onFailure(Throwable error, Object content) {
		Log.e(TAG, "http request -onFailure", error);
	};

	private void handleMessage(Message msg) {
		Object[] response;
		switch (msg.what) {
		case SUCCESS_MESSAGE:
			response = (Object[]) msg.obj;
			onSuccess(((Integer) response[0]).intValue(), (Header[]) response[1], response[2]);
			break;
		case FAILURE_MESSAGE:
			response = (Object[]) msg.obj;
			onFailure((Throwable) response[0], response[1]);
			break;
		case START_MESSAGE:
			onStart();
			break;
		case FINISH_MESSAGE:
			onFinish();
			break;
		}
	}

	protected void sendMessage(final Message msg) {
		if (handler != null) {
			// 只有在UI线程中创建响应处理，才能有handler, 而sendMessage方法基本上是在后台线程执行，因此onSuccess等
			// 调用需发往UI线程执行比较合适，
			// 其中如果Handler想用sendMessage,就要重新写一遍onSuccess调用处理，不合适，就直接用post
			handler.post(new Runnable() {
				@Override
				public void run() {
					Log.d(TAG, "network handler work[" + msg.what + "]");
					handleMessage(msg);
				}
			});
		} else {
			handleMessage(msg);
		}
	}

	protected Message obtainMessage(int responseMessage, Object response) {
		Message msg = null;
		if (handler != null) {
			msg = this.handler.obtainMessage(responseMessage, response);
		} else {
			msg = Message.obtain();
			msg.what = responseMessage;
			msg.obj = response;
		}
		return msg;
	}

	protected void sendSuccessMessage(int statusCode, Header[] headers, Object responseBody) {
		sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[] { Integer.valueOf(statusCode), headers, responseBody }));
	}

	public void sendFailureMessage(Throwable e, Object responseBody) {
		sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[] { e, responseBody }));
	}

	public void sendStartMessage() {
		sendMessage(obtainMessage(START_MESSAGE, null));
	}

	public void sendFinishMessage() {
		sendMessage(obtainMessage(FINISH_MESSAGE, null));
	}
}
