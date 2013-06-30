package cn.com.lowe.android.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.com.lowe.android.R;
import cn.com.lowe.android.widget.date.DatePickerDialog;
import cn.com.lowe.android.widget.date.DateTimePickerDialog;
import cn.com.lowe.android.widget.date.TimePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class DateEditText extends EditText implements View.OnTouchListener {
	private int dateDrawbleId;
	private String pattern;
	private int dateType;

	private Drawable dateDrawable = null;
	public static final int DATETYPE_DATE = 1;
	public static final int DATETYPE_TIME = 2;
	public static final int DATETYPE_DATETIME = 3;

	private SimpleDateFormat fmt = new SimpleDateFormat();

	public DateEditText(Context context) {
		this(context, null);
	}

	public DateEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		parseAttrs(context, attrs, defStyle);
		setViewAttrs(context, attrs, defStyle);

	}

	private void setViewAttrs(Context context, AttributeSet attrs, int defStyle) {
		setInputType(InputType.TYPE_NULL);
		dateDrawable = context.getResources().getDrawable(dateDrawbleId);
		setCompoundDrawablesWithIntrinsicBounds(null, null, dateDrawable, null);
		this.setOnTouchListener(this);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		fmt.applyPattern(pattern);

	}

	private void parseAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray textViewTa = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.TextView);
		dateDrawbleId = textViewTa.getResourceId(com.android.internal.R.styleable.TextView_drawableRight, android.R.drawable.ic_menu_today);

		TypedArray dateTa = context.obtainStyledAttributes(attrs, R.styleable.DateEditText);
		pattern = dateTa.getString(R.styleable.DateEditText_pattern);
		dateType = dateTa.getInteger(R.styleable.DateEditText_dateType, 1);
		if (TextUtils.isEmpty(pattern)) {
			switch (dateType) {
			case DATETYPE_DATE:
				pattern = "yyyy-MM-dd";
				break;
			case DATETYPE_TIME:
				pattern = "HH:mm:ss";
				break;
			case DATETYPE_DATETIME:
				pattern = "yyyy-MM-dd HH:mm:ss";
				break;
			default:
				pattern = "yyyy-MM-dd";
				break;
			}

		}

	}

	public DateEditText(Context context, AttributeSet attrs) {
		this(context, attrs, com.android.internal.R.attr.editTextStyle);
	}

	@Override
	public boolean onTouch(final View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			v.requestFocus();
			String value = this.getText().toString();
			final Calendar cal = Calendar.getInstance();
			if (!TextUtils.isEmpty(value)) {
				try {
					cal.setTime(fmt.parse(value));
				} catch (ParseException e) {
				}
			}
			int theme=0;
			if(android.os.Build.VERSION.SDK_INT==10){
				theme=R.style.date_dialog_theme_api_10;
			}
			if (dateType == DATETYPE_DATE) {
				Dialog dialog = new DatePickerDialog(v.getContext(),theme, new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, monthOfYear);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						((TextView) v).setText(fmt.format(cal.getTime()));
					}

					@Override
					public void onDateClear(DatePicker view) {
						DateEditText.this.setText("");

					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

				dialog.show();
			} else if (dateType == DATETYPE_TIME) {
				Dialog dialog = new TimePickerDialog(v.getContext(),theme, new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
						cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
						cal.set(Calendar.MINUTE, minute);
						cal.set(Calendar.SECOND, 0);
						((TextView) v).setText(fmt.format(cal.getTime()));

					}

					@Override
					public void onTimeClear(TimePicker view) {
						DateEditText.this.setText("");

					}
				}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
				dialog.show();
			} else if (dateType == DATETYPE_DATETIME) {
				Dialog dialog = new DateTimePickerDialog(v.getContext(), new DateTimePickerDialog.OnDateTimeSetListener() {

					@Override
					public void onDateTimeSet(DatePicker dateView, TimePicker timerView, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
						cal.set(Calendar.YEAR, year);
						cal.set(Calendar.MONTH, monthOfYear);
						cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
						cal.set(Calendar.MINUTE, minute);
						cal.set(Calendar.SECOND, 0);
						((TextView) v).setText(fmt.format(cal.getTime()));

					}

					@Override
					public void onDateTimeClear(DatePicker dateView, TimePicker timerView) {
						DateEditText.this.setText("");
						
					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
				dialog.show();
			}
		}
		return true;
	}

	@Override
	public void setError(CharSequence error) {
		super.setError(error);
		if (error == null) {
			recoveryDefault();
		}
	}

	@Override
	public void setError(CharSequence error, Drawable icon) {
		super.setError(error, icon);
		if (error == null) {
			recoveryDefault();
		}
	}

	private void recoveryDefault() {
		setCompoundDrawablesWithIntrinsicBounds(null, null, dateDrawable, null);
	}

}
