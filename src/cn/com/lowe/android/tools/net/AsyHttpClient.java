package cn.com.lowe.android.tools.net;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import cn.com.lowe.android.tools.net.response.HttpResponseHandler;

public class AsyHttpClient extends SyncHttpClient {

	public AsyHttpClient() {
		//这里需要创建线程池
		super();
	}

	@Override
	protected void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, HttpResponseHandler responseHandler, Context context) {
		//这里面采用线程方法调用
		super.sendRequest(client, httpContext, uriRequest, contentType, responseHandler, context);
	}

}
