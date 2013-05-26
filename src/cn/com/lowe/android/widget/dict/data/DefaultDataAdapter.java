package cn.com.lowe.android.widget.dict.data;

import java.util.ArrayList;
import java.util.List;

import cn.com.lowe.android.widget.dict.DataAdapter;

public class DefaultDataAdapter implements DataAdapter<DefaultDictData> {
	private List<DefaultDictData> list = new ArrayList<DefaultDictData>();

	@Override
	public List<DefaultDictData> getDataList() {
		return list;
	}

	@Override
	public String translate(String code) {
		int index = list.indexOf(code);
		if (index == -1) {
			return "";
		} else {
			return list.get(index).getDetail();
		}
	}

}
