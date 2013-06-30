package cn.com.lowe.android.tools.net.response;

import org.apache.http.Header;

/**
 * @Description: 同步请求响应处理
 * 
 * @Author zhengjin
 * @Date 2013-6-30 下午6:30:15
 * @Version 1.0
 */
public class HtmlSyncResponseHandler extends HtmlHttpResponseHandler {
	public int statusCode;
	public Header[] header;
	public String content;

	@Override
	protected void onSuccess(int statusCode, Header[] headers, String content) {
		this.statusCode = statusCode;
		this.header = headers;
		this.content = content;
	}

}
