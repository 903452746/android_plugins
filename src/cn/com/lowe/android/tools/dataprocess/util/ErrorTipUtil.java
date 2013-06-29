package cn.com.lowe.android.tools.dataprocess.util;

import android.view.View;
import android.widget.TextView;

public class ErrorTipUtil {

	public static void setError(final View fieldView, final String errorNote) {
		if(fieldView instanceof TextView){
			fieldView.getHandler().post(new Runnable() {
				
				@Override
				public void run() {
					((TextView)fieldView).setError(errorNote);
				}
			});
		}
		
	}

}
