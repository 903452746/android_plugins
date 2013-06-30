package cn.com.lowe.android.widget.dict.util;

import android.view.View;
import cn.com.lowe.android.tools.dataprocess.IValue;
import cn.com.lowe.android.view.DictEditText;

public class DictValueUtil implements IValue {

	@Override
	public Object getValue(View view, Class<?> clazz) throws Exception {
		
		return getValueStr(view);
	}

	@Override
	public String getValueStr(View view) {
		return ((DictEditText)view).getCode();
	}

	@Override
	public boolean setValue(Object value, View view, String splitFlag) {
		((DictEditText)view).setCode((String) value);
		return false;
	}

	@Override
	public Object getValueByStr(String value, Class<?> clazz) throws Exception {
		return value;
	}

}
