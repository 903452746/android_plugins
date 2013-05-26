package cn.com.lowe.android.tools.dataprocess;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import cn.com.lowe.android.tools.dataprocess.annotation.FieldProperty;
import cn.com.lowe.android.tools.dataprocess.annotation.TipProperty;
import cn.com.lowe.android.tools.dataprocess.lang.ValidationResult;
import cn.com.lowe.android.tools.dataprocess.util.AnnotationUtil;
import cn.com.lowe.android.tools.dataprocess.util.ValidationUtil;
import cn.com.lowe.android.tools.dataprocess.util.ValueUtil;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

/**
 * 数据映射工具
 * 
 * @author zhengjin
 * 
 */
public class DataTool {
	private static final String TAG = "DataTool";
	private static ValueUtil valueUtil = new ValueUtil();
	private static ValidationUtil validationUtil = new ValidationUtil();

	// 实体数据缓存，方便映射成对象时，不必重新取值，提高性能
	private static Map<String, WeakReference<Map<String, String>>> cacheData = new HashMap<String, WeakReference<Map<String, String>>>();

	public static ValidationResult validation(Class<?> entityClazz, View validView) {
		Resources res = validView.getResources();
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		TipProperty tipAnno;
		String fieldName;
		String tipName;
		Class<?> fieldClass;
		View fieldView;
		StringBuffer errorBuf = new StringBuffer();
		WeakReference<Map<String, String>> weakEntity = cacheData.get(entityClazz.getName());
		if (weakEntity == null || weakEntity.get() == null) {
			cacheData.put(entityClazz.getName(), new WeakReference<Map<String, String>>(new HashMap<String, String>()));
		}
		Map<String, String> entity = cacheData.get(entityClazz.getName()).get();
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getClass();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":未设置FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":根据FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				tipAnno = field.getAnnotation(TipProperty.class);
				tipName = AnnotationUtil.parseTipProperty(tipAnno, res, fieldName);

				IValidation iValidation;
				// 避免重复创建默认校验对象
				if (fieldAnno.validationClass().getName().equals(validationUtil.getClass().getName())) {
					iValidation = validationUtil;
				} else {
					try {
						iValidation = (IValidation) fieldAnno.validationClass().newInstance();
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.validationClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.validationClass属性，类不存在");
						continue;
					}
				}
				IValue iValue;
				// 避免重复创建默认取值对象
				if (fieldAnno.valueConstructClass().getName().equals(valueUtil.getClass().getName())) {
					iValue = valueUtil;
				} else {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，类不存在");
						continue;
					}
				}

				String[] ss = iValidation.check(fieldName, fieldClass, fieldView, fieldAnno, tipName, iValue);
				errorBuf.append(ss[0]);
				entity.put(fieldName, ss[1]);
			} else {
				Log.d(TAG, fieldName + ":未设置FieldProperty注解");
			}
		}
		ValidationResult vr = new ValidationResult();
		if (errorBuf.length() > 0) {
			vr.success = false;
			vr.note = errorBuf.toString();
		} else {
			vr.success = true;
		}
		return vr;

	}

	public static Object getEntity(Class<?> entityClazz, View validView) throws IllegalAccessException, InstantiationException {
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		String fieldName;
		Class<?> fieldClass;
		View fieldView;
		Map<String, String> entity = cacheData.get(entityClazz.getName()).get();
		boolean entityIsNull = (entity == null);
		Object o=null;
		try {
			o =entityClazz.newInstance();
		} catch (InstantiationException e) {
			Log.e(TAG, entityClazz.getName() + "无参构造函数不存在");
			throw e;
		} catch (IllegalAccessException e) {
			Log.e(TAG, entityClazz.getName() + "类不存在");
			throw e;
		}
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getClass();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":未设置FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":根据FieldProperty.mapToViewId属性，无法找到对应View");
					continue;
				}
				IValue iValue;
				// 避免重复创建默认取值对象
				if (fieldAnno.valueConstructClass().getName().equals(valueUtil.getClass().getName())) {
					iValue = valueUtil;
				} else {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，无参构造函数不存在");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":根据FieldProperty.valueConstructClass属性，类不存在");
						continue;
					}
				}
				if (entityIsNull) {
					Object value = iValue.getValue(fieldView, fieldClass);
					field.setAccessible(true);
					field.set(o, value);
				} else if(entity.get(fieldName)!=null) {
					Object value = iValue.getValueByStr(entity.get(fieldName), fieldClass);
					field.setAccessible(true);
					field.set(o, value);
				}else{
					Object value = iValue.getValue(fieldView, fieldClass);
					field.setAccessible(true);
					field.set(o, value);
				}
			} else {
				Log.d(TAG, fieldName + ":未设置FieldProperty注解");
			}

		}
		return o;

	}

	public static void setView(Object entity, Class<?> entityClazz, View validView) {

	}
}
