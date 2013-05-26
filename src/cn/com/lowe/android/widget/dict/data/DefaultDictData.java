package cn.com.lowe.android.widget.dict.data;

import cn.com.lowe.android.widget.dict.DictData;

public class DefaultDictData implements DictData {
	private String code;
	private String detail;
	private String spell;

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getDetail() {
		return detail;
	}

	@Override
	public String getSpell() {
		return spell;
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o)) {
			return true;
		} else {
			if (o instanceof String && code.equals(o)) {
				return true;
			} else if (o instanceof DictData && ((DictData) o).getCode().equals(code)) {
				return true;
			} else {
				return false;
			}
		}
	}

}
