package cn.com.lowe.android.widget.dict;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.lowe.android.R;
import cn.com.lowe.android.view.DictEditText;
import cn.com.lowe.android.widget.dialog.CustomDialog;
import cn.com.lowe.android.widget.dict.util.DictListViewAdapter;

/**
 * @Description: 字典对话框
 * 
 * @Author zhengjin
 * @Date 2013-6-29 下午12:07:17
 * @Version 1.0
 */
public class DictDialog extends CustomDialog implements View.OnClickListener {
	private static final String TAG = "DictDialog";
	/* 配置属性 */
	public DataAdapter dataAdapter;
	public boolean multiple;
	public int multipleItemLayout;
	public int singleItemLayout;
	public String itemformate;
	public int textViewId;
	public int maxselect;

	protected EditText filterView;
	protected Button clearBtn;
	protected Button selectBtn;
	private ListView listView;
	private Context mContext;
	public DictListViewAdapter listviewAdapter;
	DictEditText dictEditText;

	public DictDialog(Context context, DictEditText dictEditText) {
		super(context, R.layout.android_dictdialog_content);
		mContext = context;
		setTitleIcon(android.R.drawable.ic_menu_agenda);
		setTitleText("请选择");
		this.dictEditText = dictEditText;
		listviewAdapter = new DictListViewAdapter(mContext, null, null, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		filterView = (EditText) findViewById(R.id.widget_dict_filter_text);
		clearBtn = (Button) findViewById(R.id.btn2);
		selectBtn = (Button) findViewById(R.id.btn1);
		listView = (ListView) findViewById(R.id.widget_dict_list);
		clearBtn.setOnClickListener(this);
		selectBtn.setOnClickListener(this);
		listView.setAdapter(listviewAdapter);
		listView.setOnItemClickListener(itemClickListener);
		filterView.addTextChangedListener(dictTextWatcher);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		if (multiple) {
			selectBtn.setVisibility(View.VISIBLE);
		} else {
			selectBtn.setVisibility(View.GONE);
		}
		if (dataAdapter != null) {
			dataAdapter.requestData(new DataAdapter.OnSetDataListener() {

				@Override
				public void setData(List<DictData> list) {
					listviewAdapter.setDataList(list);
					listviewAdapter.getFilter().filter(filterView.getText().toString());
				}
			});

		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn1){
			this.dismiss();
		}else if(v.getId()==R.id.btn1){
			listviewAdapter.getSelectDataList().clear();
			this.dismiss();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		String code = "";
		String detail = "";
		int length = listviewAdapter.getSelectDataList().size();
		for (int i = 0; i < length; i++) {
			DictData o = listviewAdapter.getSelectDataList().get(i);
			code += o.getCode();
			detail += o.getDetail();
			if (i < length - 1) {
				code += ",";
				detail += ",";
			}
		}
		dictEditText.setText(detail);
		dictEditText.setCodeWithoutTrance(code);

	}

	/**
	 * @Title: setSelect
	 * @Description: 设置以选中数据
	 * @param selectDataList
	 * @return void
	 */
	public void setSelect(List<DictData> selectDataList) {
		listviewAdapter.setSelectDataList(selectDataList);
		listviewAdapter.notifyDataSetChanged();
	}

	public OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final DictData item = (DictData) parent.getItemAtPosition(position);
			if (multiple) {
				final CheckedTextView t;
				if (view instanceof CheckedTextView) {
					t = (CheckedTextView) view;
				} else {
					t = (CheckedTextView) view.findViewById(textViewId);
					if (t == null)
						throw new RuntimeException("字典展示内容定义ID不存在");
				}
				t.setChecked(!t.isChecked());
				if (t.isChecked()) {
					if (listviewAdapter.getSelectDataList().size() >= maxselect) {
						Toast.makeText(mContext, "最多选择" + maxselect + "个", Toast.LENGTH_SHORT).show();
						t.setChecked(false);
					} else {
						listviewAdapter.getSelectDataList().add(item);
					}

				} else {
					listviewAdapter.getSelectDataList().remove(item);
				}
			} else {
				TextView t;
				if (view instanceof TextView) {
					t = (TextView) view;
				} else {
					t = (TextView) view.findViewById(textViewId);
					if (t == null)
						throw new RuntimeException("字典展示内容定义ID不存在");
				}
				listviewAdapter.getSelectDataList().clear();
				listviewAdapter.getSelectDataList().add(item);
				DictDialog.this.dismiss();
			}

		}
	};
	
	private TextWatcher dictTextWatcher=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			listviewAdapter.getFilter().filter(s.toString());
			
		}
	};

}
