package cn.com.lowe.android.view;

import java.util.ArrayList;
import java.util.List;

import cn.com.lowe.android.R;
import cn.com.lowe.android.widget.dict.DataAdapter;
import cn.com.lowe.android.widget.dict.DictData;
import cn.com.lowe.android.widget.dict.DictDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class DictEditText extends EditText implements View.OnTouchListener {
	private static final String TAG = "DictEditText";
	protected volatile boolean isClicked = false;

	private DictDialog dictDialog;
	private String code = "";
	private String detail = "";

	public DictEditText(Context context) {
		this(context, null);
	}

	public DictEditText(Context context, AttributeSet attrs) {
		this(context, attrs, com.android.internal.R.attr.editTextStyle);
	}

	public DictEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		dictDialog = new DictDialog(context, this);
		dictDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				isClicked = false;
			}
		});
		configAttrs(context, attrs);
		initView(context, attrs);

	}

	/* 字典控件开放方法 begin */
	/**
	 * 设置数据适配器
	 * 
	 * @param dataAdapter
	 */
	public void setDataAdapter(DataAdapter dataAdapter) {
		dictDialog.dataAdapter = dataAdapter;

	}

	/**
	 * @Title: getCode
	 * @Description: 获取值
	 * @return
	 * @return String
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @Title: setCode
	 * @Description: 设置值将翻译
	 * @param code
	 * @return void
	 */
	public void setCode(final String code) {
		if (dictDialog.dataAdapter == null) {
			throw new RuntimeException("setCode只能在调用setDataAdapter(数据适配器)后调用!");
		}
		// setText(code);
		setCodeWithoutTrance(code);
		dictDialog.dataAdapter.tranceData(this, new DataAdapter.OnTranceDataListener() {

			@Override
			public void tranceData(List<DictData> list) {
				List<DictData> selectDataList = new ArrayList<DictData>();
				String[] dictSelecteds = code.split(",");
				for (int i = 0, len = dictSelecteds.length; i < len; i++) {
					for (DictData o : list) {
						if (o.getCode().equals(dictSelecteds[i])) {
							selectDataList.add(o);
							break;
						}
					}
				}
				int length = selectDataList.size();
				for (int i = 0; i < length; i++) {
					DictData o = selectDataList.get(i);
					detail += o.getDetail();
					if (i < length - 1) {
						detail += ",";
					}
				}
				dictDialog.setSelect(selectDataList);
				DictEditText.this.setText(detail);
			}
		});
	}

	/**
	 * @Title: setCodeWithoutTrance
	 * @Description: 设置值不翻译
	 * @param code
	 * @return void
	 */
	public void setCodeWithoutTrance(String code) {
		this.code = code;
	}

	private void initView(Context context, AttributeSet attrs) {

		this.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_agenda, 0);
		this.setInputType(InputType.TYPE_NULL);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		super.setOnTouchListener(this);
	}

	private void configAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DictEditText);
		dictDialog.multiple = ta.getBoolean(R.styleable.DictEditText_multiple, false);
		dictDialog.multipleItemLayout = ta.getResourceId(R.styleable.DictEditText_multipleItemLayout, android.R.layout.simple_list_item_checked);
		dictDialog.singleItemLayout = ta.getResourceId(R.styleable.DictEditText_singleItemLayout, android.R.layout.simple_list_item_1);
		dictDialog.itemformate = (String) ta.getText(R.styleable.DictEditText_itemformate);
		dictDialog.textViewId = ta.getResourceId(R.styleable.DictEditText_textViewId, 0);
		dictDialog.maxselect = ta.getInt(R.styleable.DictEditText_maxselect, 1);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isClicked == false) {
			isClicked = true;
			Log.d(TAG, "dialog is show");
			dictDialog.show();
		}
		return false;

	}
}
