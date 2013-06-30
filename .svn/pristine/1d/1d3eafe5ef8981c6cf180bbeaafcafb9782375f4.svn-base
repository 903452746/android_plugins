package cn.com.lowe.android.app.util;

import java.lang.reflect.Field;

import cn.com.lowe.android.app.annotation.InjectView;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.view.View;

public class InjectTools {
	private static final String TAG = "InjectTools";

	/**
	 * @Title: doActivityInjectWork
	 * @Description: Acitivity类注入
	 * @param @param context
	 * @param @param clazz
	 * @return void
	 * @throws
	 */
	public static void doActivityInjectWork(Activity context, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		InjectView viewAnno;
		View view;
		for (Field field : fields) {
			if (field.isAnnotationPresent(InjectView.class)) {
				viewAnno = field.getAnnotation(InjectView.class);
				int id = 0;
				if (viewAnno.value() != 0) {
					id = viewAnno.value();
				} else if (viewAnno.id() != 0) {
					id = viewAnno.id();
				}
				if (id != 0) {
					view = context.findViewById(id);
					if (view == null) {
						Log.e(TAG, field.getName() + "找不到View");
					} else {
						try {
							field.setAccessible(true);
							field.set(context, view);
						} catch (Exception e) {
							Log.e(TAG, field.getName() + "注入异常", e);
						}
					}
				}
			}

		}
	}

	public static void doActivityInjectWork(Fragment context, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		InjectView viewAnno;
		View view;
		View rootView = context.getView();
		for (Field field : fields) {
			if (field.isAnnotationPresent(InjectView.class)) {
				viewAnno = field.getAnnotation(InjectView.class);
				int id = 0;
				if (viewAnno.value() != 0) {
					id = viewAnno.value();
				} else if (viewAnno.id() != 0) {
					id = viewAnno.id();
				}
				if (id != 0) {
					view = rootView.findViewById(id);
					if (view == null) {
						Log.e(TAG, field.getName() + "找不到View");
					} else {
						try {
							field.setAccessible(true);
							field.set(context, view);
						} catch (Exception e) {
							Log.e(TAG, field.getName() + "注入异常", e);
						}
					}
				}
			}

		}

	}

	public static void doActivityInjectWork(android.support.v4.app.Fragment context, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		InjectView viewAnno;
		View view;
		View rootView = context.getView();
		for (Field field : fields) {
			if (field.isAnnotationPresent(InjectView.class)) {
				viewAnno = field.getAnnotation(InjectView.class);
				int id = 0;
				if (viewAnno.value() != 0) {
					id = viewAnno.value();
				} else if (viewAnno.id() != 0) {
					id = viewAnno.id();
				}
				if (id != 0) {
					view = rootView.findViewById(id);
					if (view == null) {
						Log.e(TAG, field.getName() + "找不到View");
					} else {
						try {
							field.setAccessible(true);
							field.set(context, view);
						} catch (Exception e) {
							Log.e(TAG, field.getName() + "注入异常", e);
						}
					}
				}
			}

		}

	}
}
