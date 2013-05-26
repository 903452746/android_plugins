package cn.com.lowe.android.tools.dataprocess.util;

import cn.com.lowe.android.tools.dataprocess.IValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Ä¬ÈÏÈ¡ÖµÆ÷
 * @author zhengjin
 *
 */
public class ValueUtil implements IValue {

	@Override
	public String getValueStr(View view) {
		String value = null;
		if (view instanceof TextView) {
			value = ((TextView) view).getText().toString();
		} else if (view instanceof RadioGroup) {
			RadioGroup rd = (RadioGroup) view;
			int id = rd.getCheckedRadioButtonId();
			if (id == -1) {
				value = null;
			}
			value = rd.findViewById(id).getTag().toString();

		} else if (view instanceof Spinner) {
			Spinner sp = (Spinner) view;
			Object o = sp.getSelectedItem();
			if (o != null) {
				value = o.toString();
			}
		} else if (view instanceof CheckBox) {
			CheckBox cb = (CheckBox) view;
			if (cb.isChecked()) {
				value = cb.getTag().toString();
			}

		}
		return value;
	}


	@Override
	public Object getValueByStr(String value, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object getValue(View view, Class<?> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean setValue(Object value, View view) {
		// TODO Auto-generated method stub
		return false;
	}

}
