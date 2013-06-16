package cn.com.lowe.android.tools.dataprocess;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

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
 * ����ӳ�乤��
 * 
 * @author zhengjin
 * 
 */
public class DataTool {
	private static final String TAG = "DataTool";
	// У��ȡֵ���󻺴�
	private static Map<Class<?>, IValue> valueInterfaceMap = new HashMap<Class<?>, IValue>();
	private static Map<Class<?>, IValidation> validationInterfaceMap = new HashMap<Class<?>, IValidation>();
	static {
		valueInterfaceMap.put(ValueUtil.class, new ValueUtil());
		validationInterfaceMap.put(ValidationUtil.class, new ValidationUtil());
	}
	// ʵ�����ݻ��棬����ӳ��ɶ���ʱ����������ȡֵ���������
	private static WeakHashMap<View, Map<String, String>> cacheData = new WeakHashMap<View, Map<String, String>>();

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
		if (cacheData.get(validView.hashCode()) == null) {
			Log.d(TAG, entityClazz.getName() + "δ���ֻ�������,���¹���");
			cacheData.put(validView, new HashMap<String, String>());
		}
		Map<String, String> entity = cacheData.get(validView);
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getType();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":δ����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				tipAnno = field.getAnnotation(TipProperty.class);
				tipName = AnnotationUtil.parseTipProperty(tipAnno, res, fieldName);

				IValidation iValidation = validationInterfaceMap.get(fieldAnno.validationClass());

				// �����ظ�����Ĭ��У�����
				if (iValidation == null) {
					try {
						iValidation = (IValidation) fieldAnno.validationClass().newInstance();
						validationInterfaceMap.put(fieldAnno.validationClass(), iValidation);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.validationClass���ԣ��޲ι��캯��������");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.validationClass���ԣ��಻����");
						continue;
					}

				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// �����ظ�����Ĭ��ȡֵ����
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��޲ι��캯��������");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��಻����");
						continue;
					}
				}

				String[] ss = iValidation.check(fieldName, fieldClass, fieldView, fieldAnno, tipName, iValue);
				if (ss != null && ss.length == 2) {
					errorBuf.append(ss[0]);
					entity.put(fieldName, ss[1]);
				}

			} else {
				Log.d(TAG, fieldName + ":δ����FieldPropertyע��");
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
		Map<String, String> entity = cacheData.get(validView);
		boolean entityIsNull = (entity == null);
		if (entityIsNull) {
			Log.d(TAG, entityClazz.getName() + "ȡֵ������δ���ֻ�������");
		}
		Object o = null;
		try {
			o = entityClazz.newInstance();
		} catch (InstantiationException e) {
			Log.e(TAG, entityClazz.getName() + "�޲ι��캯��������");
			throw e;
		} catch (IllegalAccessException e) {
			Log.e(TAG, entityClazz.getName() + "�಻����");
			throw e;
		}
		for (Field field : fields) {
			fieldName = field.getName();
			fieldClass = field.getType();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":δ����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// �����ظ�����Ĭ��ȡֵ����
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��޲ι��캯��������");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��಻����");
						continue;
					}
				}
				Object value = null;
				try {
					if (entityIsNull) {
						value = iValue.getValue(fieldView, fieldClass);
					} else if (entity.get(fieldName) != null) {
						value = iValue.getValueByStr(entity.get(fieldName), fieldClass);
					} else {
						value = iValue.getValue(fieldView, fieldClass);
					}
				} catch (Exception e) {
					Log.e(TAG, fieldName + "���ݲ���ת��Ϊ"+fieldClass+"����", e);
				}
				field.setAccessible(true);
				field.set(o, value);
			} else {
				Log.d(TAG, fieldName + ":δ����FieldPropertyע��");
			}

		}
		return o;

	}

	public static void setView(Object entity, Class<?> entityClazz, View validView) {
		Field[] fields = entityClazz.getDeclaredFields();
		FieldProperty fieldAnno;
		String fieldName;
		View fieldView;
		for (Field field : fields) {
			fieldName = field.getName();
			if (field.isAnnotationPresent(FieldProperty.class)) {
				fieldAnno = field.getAnnotation(FieldProperty.class);
				if (fieldAnno.mapToViewId() == 0) {
					Log.e(TAG, fieldName + ":δ����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				fieldView = validView.findViewById(fieldAnno.mapToViewId());
				if (fieldView == null) {
					Log.e(TAG, fieldName + ":����FieldProperty.mapToViewId���ԣ��޷��ҵ���ӦView");
					continue;
				}
				IValue iValue = valueInterfaceMap.get(fieldAnno.valueConstructClass());
				// �����ظ�����Ĭ��ȡֵ����
				if (iValue == null) {
					try {
						iValue = (IValue) fieldAnno.valueConstructClass().newInstance();
						valueInterfaceMap.put(fieldAnno.valueConstructClass(), iValue);
					} catch (InstantiationException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��޲ι��캯��������");
						continue;
					} catch (IllegalAccessException e) {
						Log.e(TAG, fieldName + ":����FieldProperty.valueConstructClass���ԣ��಻����");
						continue;
					}
				}
				Object o = null;
				field.setAccessible(true);
				try {
					o = field.get(entity);
				} catch (IllegalArgumentException e) {
					Log.e(TAG, fieldName + "���Բ�����");
					continue;
				} catch (IllegalAccessException e) {
					Log.e(TAG, fieldName + "���Բ��ɼ�");
					continue;
				}
				iValue.setValue(o, fieldView, fieldAnno.arrySplitFlag());
			} else {
				Log.d(TAG, fieldName + ":δ����FieldPropertyע��");
			}

		}
	}
}
