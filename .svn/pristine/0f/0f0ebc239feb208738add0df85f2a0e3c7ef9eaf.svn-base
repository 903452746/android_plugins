package cn.com.lowe.android.widget.dict;

import java.io.Serializable;

/**
* @Description: 字典数据数据结构
*
* @Author zhengjin 
* @Date 2013-6-29 下午5:53:07
* @Version 1.0
*/
public abstract class DictData implements Serializable{

	private static final long serialVersionUID = 1L;

	public abstract String getCode();

	public abstract String getDetail();

	public abstract String getSpell();

	@Override
	public boolean equals(Object o) {
		if (super.equals(o)) {
			return true;
		} else {
			if (o instanceof String && getCode().equals(o)) {
				return true;
			} else if (o instanceof DictData && ((DictData) o).getCode().equals(this.getCode())) {
				return true;
			} else {
				return false;
			}
		}
	}

	public String getShowText(String itemformate) {
		if ("1".equals(itemformate)) {
			// 111111格式
			return getCode() + "\t|\t" + getDetail() + "\t|\t" + getSpell();
		} else if ("2".equals(itemformate)) {
			// 001100格式
			return getDetail();
		} else {
			return getDetail();
		}
	}

}
