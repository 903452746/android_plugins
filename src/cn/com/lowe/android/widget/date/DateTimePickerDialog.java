package cn.com.lowe.android.widget.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.com.lowe.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class DateTimePickerDialog extends AlertDialog implements OnDateChangedListener, OnTimeChangedListener, OnClickListener {

	private DatePicker datePicker;
	private TimePicker timePicker;
	private Context mContext;
	private final OnDateTimeSetListener mCallback;
	private static final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Calendar mCalendar;

	public interface OnDateTimeSetListener {
		void onDateTimeSet(DatePicker dateView, TimePicker timerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);

		void onDateTimeClear(DatePicker dateView, TimePicker timerView);
	}

	public DateTimePickerDialog(Context context, OnDateTimeSetListener callBack, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
		super(context);
		mCallback = callBack;

		setButton(BUTTON_POSITIVE, context.getText(R.string.date_time_set), this);
		setButton(BUTTON_NEGATIVE, context.getText(R.string.cancel), this);
		setIcon(0);
		setTitle(R.string.date_picker_dialog_title);

		mContext = context;
		mCalendar = Calendar.getInstance();
		datePicker = new DatePicker(context);
		datePicker.init(year, monthOfYear, dayOfMonth, this);

		timePicker = new TimePicker(context);
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(this);
		timePicker.setCurrentHour(hourOfDay);
		timePicker.setCurrentMinute(minute);

		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linearLayout.addView(datePicker.getRootView());
		linearLayout.addView(timePicker.getRootView());
		setView(linearLayout);

		updateTitle(year, monthOfYear, dayOfMonth, hourOfDay, minute);
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		updateTitleTime(hourOfDay, minute);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		updateTitleDate(year, monthOfYear, dayOfMonth);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case BUTTON_POSITIVE:
			if (mCallback != null) {
				timePicker.clearFocus();
				datePicker.clearFocus();
				mCallback.onDateTimeSet(datePicker, timePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
			}
			break;

		case BUTTON_NEGATIVE:
			if (mCallback != null) {
				timePicker.clearFocus();
				datePicker.clearFocus();
				mCallback.onDateTimeClear(datePicker, timePicker);
			}
			break;
		}
	}

	private void updateTitleDate(int year, int month, int day) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, month);
		mCalendar.set(Calendar.DAY_OF_MONTH, day);
		setTitle(fmt.format(mCalendar.getTime()));
	}

	private void updateTitleTime(int hour, int minute) {
		mCalendar.set(Calendar.HOUR_OF_DAY, hour);
		mCalendar.set(Calendar.MINUTE, minute);
		mCalendar.set(Calendar.SECOND, 0);
		setTitle(fmt.format(mCalendar.getTime()));
	}

	private void updateTitle(int year, int month, int day, int hour, int minute) {
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, month);
		mCalendar.set(Calendar.DAY_OF_MONTH, day);
		mCalendar.set(Calendar.HOUR_OF_DAY, hour);
		mCalendar.set(Calendar.MINUTE, minute);
		mCalendar.set(Calendar.SECOND, 0);
		setTitle(fmt.format(mCalendar.getTime()));
	}
}
