package cn.com.lowe.android.tools.dataprocess;

import cn.com.lowe.android.R;
import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import cn.com.lowe.android.tools.dataprocess.lang.DataType;

public class Bean {
	// 类型转化测试
	@FieldProperty(mapToViewId = R.id.text_1)
	public String text1;
	@FieldProperty(mapToViewId = R.id.text_2)
	public Integer text2;
	@FieldProperty(mapToViewId = R.id.text_3)
	public Short text3;
	@FieldProperty(mapToViewId = R.id.text_4)
	public Long text4;
	@FieldProperty(mapToViewId = R.id.text_5)
	public Float text5;
	@FieldProperty(mapToViewId = R.id.text_6)
	public Double text6;
	@FieldProperty(mapToViewId = R.id.text_7)
	public Boolean text7;

	// 必填校验
	@FieldProperty(required = true, mapToViewId = R.id.text_8)
	public String text8;
	@FieldProperty(required = false, mapToViewId = R.id.text_9)
	public String text9;

	// 长度校验
	@FieldProperty(mapToViewId = R.id.text_10, maxLength = 4)
	public String text10;
	@FieldProperty(mapToViewId = R.id.text_11, minLength = 2)
	public String text11;

	// 数据类型测试
	@FieldProperty(mapToViewId = R.id.text_12, dataType = DataType.TEXT)
	public String text12;
	@FieldProperty(mapToViewId = R.id.text_13,  required=true, dataType = DataType.DATE, dateFmt = "yyyy-MM-dd")
	public String text13;
	@FieldProperty(mapToViewId = R.id.text_14, dataType = DataType.DATETIME, dateFmt = "yyyy-MM-dd HH:mm:ss")
	public String text14;
	@FieldProperty(mapToViewId = R.id.text_15, dataType = DataType.FLOATNUMBER)
	public String text15;
	@FieldProperty(mapToViewId = R.id.text_16, dataType = DataType.NUMBER)
	public String text16;
	@FieldProperty(mapToViewId = R.id.text_17, dataType = DataType.TIME, dateFmt = "HH:mm:ss")
	public String text17;

	// 数据格式校验
	@FieldProperty(mapToViewId = R.id.text_18, dataType = DataType.TEXT, dataExpression = "^\\d{10}$", dataExpressionTip = "数据必须为10位数字")
	public String text18;

}
