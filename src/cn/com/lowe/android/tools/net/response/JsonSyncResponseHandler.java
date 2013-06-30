package cn.com.lowe.android.tools.net.response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


/**
* @Description: 同步请求响应处理
*
* @Author zhengjin 
* @Date 2013-6-30 下午6:30:15
* @Version 1.0
*/
public class JsonSyncResponseHandler extends JsonHttpResponseHandler {
	public int statusCode;
	public Header[] header;
	public JSONObject jsonObject;
	public JSONArray jsonArray;

	@Override
	protected void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		this.statusCode=statusCode;
		this.header=headers;
		this.jsonObject=response;

	}

	@Override
	protected void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		this.statusCode=statusCode;
		this.header=headers;
		this.jsonArray=response;
	}

}
