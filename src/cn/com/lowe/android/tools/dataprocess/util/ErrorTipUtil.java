package cn.com.lowe.android.tools.dataprocess.util;

import android.view.View;
import android.widget.TextView;

public class ErrorTipUtil {

	public static void setError(final View fieldView, String errorNote) {

		final String note = "".equals(errorNote) ? null : errorNote;
		fieldView.getHandler().post(new Runnable() {
			@Override
			public void run() {
				if (fieldView instanceof TextView) {
					((TextView) fieldView).setError(note);
				}
			}
		});

	}

}
