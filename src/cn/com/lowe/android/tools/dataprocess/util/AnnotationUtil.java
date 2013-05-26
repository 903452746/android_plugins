package cn.com.lowe.android.tools.dataprocess.util;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;
import android.util.Log;
import cn.com.lowe.android.tools.dataprocess.annotation.TipProperty;

public class AnnotationUtil {
	private static final String TAG = "AnnotationUtil";

	public static String parseTipProperty(TipProperty tipAnno, Resources res, String fieldName) {
		if (tipAnno == null) {
			return fieldName;
		} else if (!TextUtils.isEmpty(tipAnno.value())) {
			return tipAnno.value();
		} else if (!TextUtils.isEmpty(tipAnno.fieldCN())) {
			return tipAnno.fieldCN();
		} else if (tipAnno.fieldCNId() != 0) {
			try {
				return res.getString(tipAnno.fieldCNId());
			} catch (NotFoundException e) {
				Log.d(TAG, "tipProperty fieldCNID no exits", e);
				return fieldName;
			}
		} else {
			return fieldName;
		}
	}

}
