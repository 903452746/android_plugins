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

package cn.com.lowe.android.tools.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;

import cn.com.lowe.android.tools.net.lang.HttpRequestRunnable;
import cn.com.lowe.android.tools.net.lang.PersistentCookieStore;
import cn.com.lowe.android.tools.net.response.HttpResponseHandler;

import android.content.Context;

/**
 * The AsyncHttpClient can be used to make asynchronous GET, POST, PUT and
 * DELETE HTTP requests in your Android applications. Requests can be made with
 * additional parameters by passing a {@link RequestParams} instance, and
 * responses can be handled by passing an anonymously overridden
 * {@link HttpResponseHandler} instance.
 * <p>
 * For example:
 * <p>
 * 
 * <pre>
 * AsyncHttpClient client = new AsyncHttpClient();
 * client.get(&quot;http://www.google.com&quot;, new AsyncHttpResponseHandler() {
 * 	&#064;Override
 * 	public void onSuccess(String response) {
 * 		System.out.println(response);
 * 	}
 * });
 * </pre>
 */
public class SyncHttpClient {
	private static final String VERSION = "1.4.3";

	private static final int DEFAULT_MAX_CONNECTIONS = 10;
	private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
	private static final String ENCODING_GZIP = "gzip";

	private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
	private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

	private final DefaultHttpClient httpClient;
	private final HttpContext httpContext;
	private final Map<String, String> clientHeaderMap;

	/**
	 * Creates a new AsyncHttpClient.
	 */
	public SyncHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, socketTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(maxConnections));
		ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
		HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
		HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUserAgent(httpParams, String.format("android-async-http/%s", VERSION));
		
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

		httpContext = new SyncBasicHttpContext(new BasicHttpContext());
		httpClient = new DefaultHttpClient(cm, httpParams);
		httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
			@Override
			public void process(HttpRequest request, HttpContext context) {
				if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
					request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
				}
				for (String header : clientHeaderMap.keySet()) {
					request.addHeader(header, clientHeaderMap.get(header));
				}
			}
		});
		httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
			@Override
			public void process(HttpResponse response, HttpContext context) {
				final HttpEntity entity = response.getEntity();
				if (entity == null) {
					return;
				}
				final Header encoding = entity.getContentEncoding();
				if (encoding != null) {
					for (HeaderElement element : encoding.getElements()) {
						if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
							response.setEntity(new InflatingEntity(response.getEntity()));
							break;
						}
					}
				}
			}
		});
		clientHeaderMap = new HashMap<String, String>();
	}

	/**
	 * Get the underlying HttpClient instance. This is useful for setting
	 * additional fine-grained settings for requests by accessing the client's
	 * ConnectionManager, HttpParams and SchemeRegistry.
	 */
	public HttpClient getHttpClient() {
		return this.httpClient;
	}

	/**
	 * Get the underlying HttpContext instance. This is useful for getting and
	 * setting fine-grained settings for requests by accessing the context's
	 * attributes such as the CookieStore.
	 */
	public HttpContext getHttpContext() {
		return this.httpContext;
	}

	/**
	 * Sets an optional CookieStore to use when making requests
	 * 
	 * @param cookieStore
	 *            The CookieStore implementation to use, usually an instance of
	 *            {@link PersistentCookieStore}
	 */
	public void setCookieStore(CookieStore cookieStore) {
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	/**
	 * Sets the User-Agent header to be sent with each request. By default,
	 * "Android Asynchronous Http Client/VERSION (http://loopj.com/android-async-http/)"
	 * is used.
	 * 
	 * @param userAgent
	 *            the string to use in the User-Agent header.
	 */
	public void setUserAgent(String userAgent) {
		HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
	}

	/**
	 * Sets the connection time oout. By default, 10 seconds
	 * 
	 * @param timeout
	 *            the connect/socket timeout in milliseconds
	 */
	public void setTimeout(int timeout) {
		final HttpParams httpParams = this.httpClient.getParams();
		ConnManagerParams.setTimeout(httpParams, timeout);
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	}

	/**
	 * Sets the SSLSocketFactory to user when making requests. By default, a
	 * new, default SSLSocketFactory is used.
	 * 
	 * @param sslSocketFactory
	 *            the socket factory to use for https requests.
	 */
	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
	}

	/**
	 * Sets headers that will be added to all requests this client makes (before
	 * sending).
	 * 
	 * @param header
	 *            the name of the header
	 * @param value
	 *            the contents of the header
	 */
	public void addHeader(String header, String value) {
		clientHeaderMap.put(header, value);
	}

	/**
	 * Sets basic authentication for the request. Uses AuthScope.ANY. This is
	 * the same as setBasicAuth('username','password',AuthScope.ANY)
	 * 
	 * @param username
	 * @param password
	 */
	public void setBasicAuth(String user, String pass) {
		AuthScope scope = AuthScope.ANY;
		setBasicAuth(user, pass, scope);
	}

	/**
	 * Sets basic authentication for the request. You should pass in your
	 * AuthScope for security. It should be like this
	 * setBasicAuth("username","password", new
	 * AuthScope("host",port,AuthScope.ANY_REALM))
	 * 
	 * @param username
	 * @param password
	 * @param scope
	 *            - an AuthScope object
	 * 
	 */
	public void setBasicAuth(String user, String pass, AuthScope scope) {
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, pass);
		this.httpClient.getCredentialsProvider().setCredentials(scope, credentials);
	}

	public static String getUrlWithQueryString(String url, RequestParams params) {
		if (params != null) {
			String paramString = params.getParamString();
			if (url.indexOf("?") == -1) {
				url += "?" + paramString;
			} else {
				url += "&" + paramString;
			}
		}

		return url;
	}

	private static class InflatingEntity extends HttpEntityWrapper {
		public InflatingEntity(HttpEntity wrapped) {
			super(wrapped);
		}

		@Override
		public InputStream getContent() throws IOException {
			return new GZIPInputStream(wrappedEntity.getContent());
		}

		@Override
		public long getContentLength() {
			return -1;
		}
	}

	private HttpEntity paramsToEntity(RequestParams params) {
		HttpEntity entity = null;

		if (params != null) {
			entity = params.getEntity();
		}

		return entity;
	}

	private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
		if (entity != null) {
			requestBase.setEntity(entity);
		}

		return requestBase;
	}

	/**
	 * Perform a HTTP GET request, without any parameters.
	 * 
	 * @param url
	 *            the URL to send the request to.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(String url, HttpResponseHandler responseHandler) {
		get(null, url, null, responseHandler);
	}

	/**
	 * Perform a HTTP GET request with parameters.
	 * 
	 * @param url
	 *            the URL to send the request to.
	 * @param params
	 *            additional GET parameters to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(String url, RequestParams params, HttpResponseHandler responseHandler) {
		get(null, url, params, responseHandler);
	}

	/**
	 * Perform a HTTP GET request without any parameters and track the Android
	 * Context which initiated the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(Context context, String url, HttpResponseHandler responseHandler) {
		get(context, url, null, responseHandler);
	}

	/**
	 * Perform a HTTP GET request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param params
	 *            additional GET parameters to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(Context context, String url, RequestParams params, HttpResponseHandler responseHandler) {
		sendRequest(httpClient, httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, responseHandler, context);
	}

	/**
	 * Perform a HTTP GET request and track the Android Context which initiated
	 * the request with customized headers
	 * 
	 * @param url
	 *            the URL to send the request to.
	 * @param headers
	 *            set headers only for this request
	 * @param params
	 *            additional GET parameters to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void get(Context context, String url, Header[] headers, RequestParams params, HttpResponseHandler responseHandler) {
		HttpUriRequest request = new HttpGet(getUrlWithQueryString(url, params));
		if (headers != null)
			request.setHeaders(headers);
		sendRequest(httpClient, httpContext, request, null, responseHandler, context);
	}

	//
	// HTTP POST Requests
	//

	/**
	 * Perform a HTTP POST request, without any parameters.
	 * 
	 * @param url
	 *            the URL to send the request to.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(String url, HttpResponseHandler responseHandler) {
		post(null, url, null, responseHandler);
	}

	/**
	 * Perform a HTTP POST request with parameters.
	 * 
	 * @param url
	 *            the URL to send the request to.
	 * @param params
	 *            additional POST parameters or files to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(String url, RequestParams params, HttpResponseHandler responseHandler) {
		post(null, url, params, responseHandler);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param params
	 *            additional POST parameters or files to send with the request.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(Context context, String url, RequestParams params, HttpResponseHandler responseHandler) {
		post(context, url, paramsToEntity(params), null, responseHandler);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request.
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param entity
	 *            a raw {@link HttpEntity} to send with the request, for
	 *            example, use this to send string/json/xml payloads to a server
	 *            by passing a {@link org.apache.http.entity.StringEntity}.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(Context context, String url, HttpEntity entity, String contentType, HttpResponseHandler responseHandler) {
		sendRequest(httpClient, httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, responseHandler, context);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request. Set headers only for this request
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param headers
	 *            set headers only for this request
	 * @param params
	 *            additional POST parameters to send with the request.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(Context context, String url, Header[] headers, RequestParams params, String contentType, HttpResponseHandler responseHandler) {
		HttpEntityEnclosingRequestBase request = new HttpPost(url);
		if (params != null)
			request.setEntity(paramsToEntity(params));
		if (headers != null)
			request.setHeaders(headers);
		sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
	}

	/**
	 * Perform a HTTP POST request and track the Android Context which initiated
	 * the request. Set headers only for this request
	 * 
	 * @param context
	 *            the Android Context which initiated the request.
	 * @param url
	 *            the URL to send the request to.
	 * @param headers
	 *            set headers only for this request
	 * @param entity
	 *            a raw {@link HttpEntity} to send with the request, for
	 *            example, use this to send string/json/xml payloads to a server
	 *            by passing a {@link org.apache.http.entity.StringEntity}.
	 * @param contentType
	 *            the content type of the payload you are sending, for example
	 *            application/json if sending a json payload.
	 * @param responseHandler
	 *            the response handler instance that should handle the response.
	 */
	public void post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, HttpResponseHandler responseHandler) {
		HttpEntityEnclosingRequestBase request = addEntityToRequestBase(new HttpPost(url), entity);
		if (headers != null)
			request.setHeaders(headers);
		sendRequest(httpClient, httpContext, request, contentType, responseHandler, context);
	}

	// Private stuff
	protected void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, HttpResponseHandler responseHandler, Context context) {
		
		new HttpRequestRunnable(client, httpContext, uriRequest, responseHandler).run();
	}

}
