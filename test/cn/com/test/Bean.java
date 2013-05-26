package cn.com.test;

import android.R;
import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import cn.com.lowe.android.tools.dataprocess.annotation.TipProperty;
import cn.com.lowe.android.tools.dataprocess.lang.ExpressionCollections;
import cn.com.lowe.android.widget.dict.annotation.DictProperty;
import cn.com.lowe.android.widget.dict.data.DefaultDataAdapter;

public class Bean {
	@FieldProperty(dataExpression = ExpressionCollections.ADDRESS,
			mapToViewId=R.id.addToDictionary)
	@TipProperty()
	public Byte byt;
	public Integer inte;
	public Short shor;
	public Long lon;
	public Float flo;
	public Double doub;
	public Boolean bool;
	public Character cha;
	public String str;

}
