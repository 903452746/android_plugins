package cn.com.lowe.android.view;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PhoneView extends LinearLayout {
	private EditText code;
	private EditText dh;
	private EditText fqh;

	private TextView split1View;
	private TextView split2View;

	public String PHONE_EXPRESSION = "^\\d{3,4}-\\d{8}|\\d{3,4}-\\d{8}-\\d{4}$";

	public PhoneView(Context context) {
		super(context);
		buildView(context);
	}

	public PhoneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		buildView(context);

	}

	public PhoneView(Context context, AttributeSet attrs) {
		super(context, attrs);
		buildView(context);
	}

	private void buildView(Context context) {
		code = new EditText(context);
		LayoutParams codeLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		code.setLayoutParams(codeLayoutParams);
		code.setInputType(InputType.TYPE_CLASS_NUMBER);
		code.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });

		dh = new EditText(context);
		LayoutParams dhLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 4);
		dh.setLayoutParams(dhLayoutParams);
		dh.setInputType(InputType.TYPE_CLASS_NUMBER);
		dh.setFilters(new InputFilter[] { new InputFilter.LengthFilter(8) });

		fqh = new EditText(context);
		LayoutParams fqhLayoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		fqh.setLayoutParams(fqhLayoutParams);
		fqh.setInputType(InputType.TYPE_CLASS_NUMBER);
		fqh.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });

		split1View = new TextView(context);
		LayoutParams split1LayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		split1View.setLayoutParams(split1LayoutParams);
		split1View.setText("-");

		split2View = new TextView(context);
		LayoutParams split2LayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		split2View.setLayoutParams(split2LayoutParams);
		split2View.setText("-");

		this.setOrientation(HORIZONTAL);
		addView(code, 0);
		addView(split1View, 1);
		addView(dh, 2);
		addView(split2View, 3);
		addView(fqh, 4);
	}

	public String getText() {
		return mergePhone(code.getText().toString(), dh.getText().toString(), fqh.getText().toString());
	}

	public String getFqh() {
		return fqh.getText().toString();

	}

	public String getDh() {
		return dh.getText().toString();

	}

	public String getCode() {
		return code.getText().toString();

	}

	public void setText(String dh) {
		String[] phones = prasePhone(dh);
		setCode(phones[0]);
		setDh(phones[1]);
		setFqh(phones[2]);
	}

	public void setFqh(String fqh) {
		this.fqh.setText(fqh == null ? "" : fqh);
	}

	public void setDh(String dh) {
		this.dh.setText(dh == null ? "" : dh);
	}

	public void setCode(String code) {
		this.code.setText(code == null ? "" : code);
	}

	private String[] prasePhone(String allPhone) {
		String[] phones = new String[] { "", "", "" };
		String[] strSplit = allPhone.split("-");
		if (strSplit.length == 1) {
			phones[1] = strSplit[0];
		} else if (strSplit.length == 2) {
			if (strSplit[0].length() > 4) {
				phones[1] = strSplit[0];
				phones[2] = strSplit[1];
			} else {
				phones[0] = strSplit[0];
				phones[1] = strSplit[1];
			}

		} else if (strSplit.length == 3) {
			phones[0] = strSplit[0];
			phones[1] = strSplit[1];
			phones[2] = strSplit[2];
		}
		return phones;

	}

	private String mergePhone(String code, String dh, String fqh) {
		if (TextUtils.isEmpty(code)) {
			dh = code + "-" + dh;
		}
		if (TextUtils.isEmpty(fqh)) {
			dh = dh + "-" + fqh;
		}
		return dh;
	}

	/**
	* @Title: isError
	* @Description: 内容是否正确 true:正确  false:错误
	* @return
	* @return boolean
	*/
	public boolean isError() {
		return !getText().matches(PHONE_EXPRESSION);
	}
}
