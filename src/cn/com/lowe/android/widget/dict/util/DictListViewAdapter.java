package cn.com.lowe.android.widget.dict.util;

import java.util.ArrayList;
import java.util.List;

import cn.com.lowe.android.widget.dict.DictData;
import cn.com.lowe.android.widget.dict.DictDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.TextView;

/**
* @Description: ListView 展示适配器
*
* @Author zhengjin 
* @Date 2013-6-29 下午5:46:14
* @Version 1.0
*/
public class DictListViewAdapter extends BaseAdapter {
	private List<DictData> dataList;
	private DictDialog dictDialog;
	private Context mContext;
	private List<DictData> selectDataList;
	private DictFilter mFilter;

	public DictListViewAdapter(Context context, List<DictData> dataList, List<DictData> selectDataList, DictDialog dictDialog) {
		super();
		this.dictDialog = dictDialog;
		this.mContext = context;
		this.dataList = dataList == null ? new ArrayList<DictData>() : dataList;
		this.selectDataList = selectDataList == null ? new ArrayList<DictData>() : selectDataList;

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final DictData item = (DictData) getItem(position);
		boolean isSelected = selectDataList.contains(item);
		if (dictDialog.multiple) {
			view = LayoutInflater.from(mContext).inflate(dictDialog.multipleItemLayout, null);
			final CheckedTextView t;
			if (view instanceof CheckedTextView) {
				t = (CheckedTextView) view;
			} else {
				t = (CheckedTextView) view.findViewById(dictDialog.textViewId);
				if (t == null)
					throw new RuntimeException("字典展示内容定义ID不存在");
			}
			t.setChecked(isSelected);
			setTextByFmt(t, item, dictDialog.itemformate);
		} else {
			view = LayoutInflater.from(mContext).inflate(dictDialog.singleItemLayout, null);
			TextView t;
			if (view instanceof TextView) {
				t = (TextView) view;
			} else {
				t = (TextView) view.findViewById(dictDialog.textViewId);
				if (t == null)
					throw new RuntimeException("字典展示内容定义ID不存在");
			}
			setTextByFmt(t, item, dictDialog.itemformate);
		}
		return view;
	}

	private void setTextByFmt(TextView view, DictData item, String itemformate) {
		String value = item.getShowText(itemformate);
		view.setText(value);
	}

	/**
	* @Title: getSelectDataList
	* @Description: 获取选中项
	* @return
	* @return List<DictData>
	*/
	public List<DictData> getSelectDataList() {
		return selectDataList;
	}

	/**
	* @Title: setSelectDataList
	* @Description: 设置选中项
	* @param selectDataList
	* @return void
	*/
	public void setSelectDataList(List<DictData> selectDataList) {
		this.selectDataList = selectDataList;
	}

	/**
	* @Title: setDataList
	* @Description: 设置字典可选项
	* @param list
	* @return void
	*/
	public void setDataList(List<DictData> list) {
		//添加进过滤器
		getFilter().reSetData(list);
		getFilter().filter("");
	}

	public DictFilter getFilter() {
		if (mFilter == null) {
			mFilter = new DictFilter();
		}
		return mFilter;
	}

	/**
	* @Description: 数据过滤器
	*
	* @Author zhengjin 
	* @Date 2013-6-29 下午5:47:41
	* @Version 1.0
	*/
	public class DictFilter extends Filter {
		private List<DictData> orginalDataList = new ArrayList<DictData>();
		private List<DictData> desDataList = new ArrayList<DictData>();

		protected void reSetData(List<DictData> list) {
			orginalDataList = list;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			desDataList.clear();
			if ("".equals(prefix.toString().trim())) {
				for (DictData o : orginalDataList) {
					desDataList.add(o);
				}
			} else {
				for (DictData o : orginalDataList) {
					if (o.getShowText(dictDialog.itemformate).contains(prefix)) {
						desDataList.add(o);
					}
				}

			}
			results.values = desDataList;
			results.count = desDataList.size();
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			dataList = (List<DictData>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
	}

}
