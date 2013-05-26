package cn.com.lowe.android.widget.dict;

import java.util.List;

public interface DataAdapter<T extends DictData> {
	
	public List<T> getDataList();
	
	public String translate(String code);
	
	
}
