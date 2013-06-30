package cn.com.lowe.android.tools.net.response;

import org.apache.http.Header;

public class BinarySyncHttpResponseHandler extends BinaryHttpResponseHandler {
	public int statusCode;
	public Header[] header;
	public byte[] content;

	@Override
	protected void onSuccess(int statusCode, Header[] headers, byte[] content) {
		this.statusCode = statusCode;
		this.header = headers;
		this.content = content;
	}

}
