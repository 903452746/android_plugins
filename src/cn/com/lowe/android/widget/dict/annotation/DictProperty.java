package cn.com.lowe.android.widget.dict.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.lowe.android.widget.dict.DictDisplayMode;
import cn.com.lowe.android.widget.dict.data.DefaultDataAdapter;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DictProperty {
	public Class<?> dataAdapter() default DefaultDataAdapter.class;
	
	public String[] constructParams() default {};
	
	public DictDisplayMode  displayMode() default DictDisplayMode.mode_001100;
	
}
