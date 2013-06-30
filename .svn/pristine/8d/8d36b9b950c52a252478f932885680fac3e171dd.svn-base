package cn.com.lowe.android.tools.dataprocess.util;

import cn.com.lowe.android.tools.dataprocess.IValue;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

/**
 * 默认取值器
 * 
 * @author zhengjin
 * 
 */
public class ValueUtil implements IValue {

	/** (非 Javadoc)
	* <p>Title: getValueStr</p>
	* <p>Description:支持TextView, EditText, Spinner, RadioGroup, RadioButton, CheckBox组件的取值 </p>
	* @param view
	* @return
	* @see cn.com.lowe.android.tools.dataprocess.IValue#getValueStr(android.view.View)
	*/
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

		} else if (view instanceof RadioButton) {
			RadioButton rd = (RadioButton) view;
			if (rd.isChecked()) {
				value = rd.getTag().toString();
			}
		}else{
			Log.e("ValueUtil", "ValueUtil不支持"+view.getClass().getName()+"对象的取值");
		}
		return value;
	}

	@Override
	public Object getValueByStr(String value, Class<?> clazz) throws NumberFormatException {
		if(null==value||"".equals(value)){
			return null;
		}else if (clazz == String.class) {
			return value;
		} else if (clazz == Integer.class) {
			return Integer.valueOf(value);
		} else if (clazz == Float.class) {
			return Float.valueOf(value);
		} else if (clazz == Double.class) {
			return Double.valueOf(value);
		} else if (clazz == Short.class) {
			return Short.valueOf(value);
		} else if (clazz == Long.class) {
			return Long.valueOf(value);
		} else if (clazz == Boolean.class) {
			return Boolean.valueOf(value);
		} else{
			Log.e("ValueUtil", "ValueUtil不支持"+clazz.getClass().getName()+"类型的对象转换");
		}
		return null;
	}

	@Override
	public Object getValue(View view, Class<?> clazz) throws NumberFormatException {
		String value = getValueStr(view);
		if (value != null) {
			Object o = getValueByStr(value, clazz);
			return o;
		}
		return null;
	}

	/**
	* (非 Javadoc)
	* <p>Title: setValue</p>
	* <p>Description:支持TextView, EditText, Spinner, RadioGroup, RadioButton, CheckBox组件的赋值 </p>
	* @param value
	* @param view
	* @param splitFlag
	* @return
	* @see cn.com.lowe.android.tools.dataprocess.IValue#setValue(java.lang.Object, android.view.View, java.lang.String)
	*/
	@Override
	public boolean setValue(Object value, View view, String splitFlag) {
		String showValue;
		if (value == null) {
			return true;
		}
		if (value instanceof Boolean) {
			showValue = ((Boolean) value) ? "true" : "false";
		} else {
			showValue = value.toString();
		}
		if (view instanceof TextView) {
			((TextView) view).setText(showValue);
		} else if (view instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup) view;
			int count = rg.getChildCount();
			for (int i = 0; i < count; i++) {
				RadioButton rd = (RadioButton) rg.getChildAt(i);
				if (showValue.equals(rd.getTag())) {
					rd.setChecked(true);
				}
			}
		} else if (view instanceof Spinner) {
			Spinner sp = (Spinner) view;
			SpinnerAdapter sa = sp.getAdapter();
			int length = sa.getCount();
			for (int i = 0; i < length; i++) {
				if (showValue.equals(sa.getItem(i).toString())) {
					sp.setSelection(i);
					break;
				}
			}

		} else if (view instanceof CheckBox) {
			CheckBox cb = (CheckBox) view;
			if (showValue.equals(cb.getTag())) {
				cb.setChecked(true);
			}

		} else if (view instanceof RadioButton) {
			RadioButton rb = (RadioButton) view;
			if (showValue.equals(rb.getTag())) {
				rb.setChecked(true);
			}

		}else{
			Log.e("ValueUtil", "ValueUtil不支持"+view.getClass().getName()+"对象的赋值");
		}
		return true;

	}

}
