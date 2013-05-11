package cn.com.lowe.android.widget.dialog;

import java.io.Serializable;

import cn.com.lowe.android.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private static final String TAG = CustomDialog.class.getName();
	public static final float WRAP_CONTENT = -2.0f;
	private int viewResourceId;
	private DialogCreateListener viewCreateListener;
	private Context context;
	private LayoutInflater inflater;

	// �ڲ�ҳ��Ԫ��
	private LinearLayout contentView;
	private RelativeLayout titleContentView;
	private ImageView titleIconView;
	private TextView titleView;

	// ҳ����������
	private Config config;
	private int screenWidth;
	private int screenHeight;
	private int titleHeight;
	private int dialogWidth;
	private int dialogHeiht;

	public CustomDialog(Context context, int viewResourceId) {
		this(context, viewResourceId, null);
	}

	public CustomDialog(Context context, DialogCreateListener viewCreateListener) {
		this(context, 0, viewCreateListener);
	}

	public CustomDialog(Context context, int viewResourceId, DialogCreateListener viewCreateListener) {
		super(context);
		this.context = context;
		this.viewResourceId = viewResourceId;
		this.viewCreateListener = viewCreateListener;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		config = new Config();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "CustomDialog.onCreate()");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (savedInstanceState != null) {

		}
		setContentView(R.layout.lowe_widget_customdialog);
		contentView = (LinearLayout) findViewById(R.id.widget_customdialog_content);
		titleIconView = (ImageView) findViewById(R.id.widget_customdialog_title_icon);
		titleView = (TextView) findViewById(R.id.widget_customdialog_title_text);
		titleContentView = (RelativeLayout) findViewById(R.id.widget_customdialog_title);
		createContentUI();
		initConfig();
	}

	private void initConfig() {
		if (config.titleIcon != null) {
			titleIconView.setImageDrawable(config.titleIcon);
		}
		if (!TextUtils.isEmpty(config.titleText)) {
			titleView.setText(config.titleText);
		}
		if (config.titleTextColor != -1) {
			titleView.setTextColor(config.titleTextColor);
		}
		if (config.titleBgColor != -1) {
			titleContentView.setBackgroundColor(config.titleBgColor);
		} else if (config.titleBgImage != null) {
			titleContentView.setBackground(config.titleBgImage);
		}
		if (config.contentBgColor != -1) {
			contentView.setBackgroundColor(config.contentBgColor);
		} else if (config.contentBgImage != null) {
			contentView.setBackground(config.contentBgImage);
		}

		// �Ի���������
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		titleHeight = titleContentView.getHeight();

		// ��߱ȼ���
		if (config.widthPercent == WRAP_CONTENT) {
			dialogWidth = (int) WRAP_CONTENT;
		} else if (config.widthPercent <= 1 && config.widthPercent > 0) {
			dialogWidth = (int) (screenWidth * config.widthPercent);
		} else {
			Log.e(TAG, "�Ի�������ֵ����,������0��1(����1)֮��ĸ�����ֵ����CustomDialog.WRAP_CONTENT");
			dialogWidth = (int) WRAP_CONTENT;
		}
		if (config.heightPercent == WRAP_CONTENT) {
			dialogHeiht = (int) WRAP_CONTENT;
		} else if (config.heightPercent <= 1 && config.heightPercent > 0) {
			dialogHeiht = (int) (screenHeight * config.heightPercent);
		} else {
			Log.e(TAG, "�Ի���߶���ֵ����,������0��1(����1)֮��ĸ�����ֵ����CustomDialog.WRAP_CONTENT");
			dialogHeiht = (int) WRAP_CONTENT;
		}

		// �߶���Ҫ�޳�����߶�
		if (dialogHeiht != WRAP_CONTENT) {
			dialogHeiht -= titleHeight;
		}
		contentView.setLayoutParams(new LayoutParams(dialogWidth, dialogHeiht));
	}

	private void createContentUI() {
		if (viewResourceId != 0) {
			Log.d(TAG, "viewResourceId is used");
			contentView.addView(inflater.inflate(viewResourceId, null));
		}
		if (viewCreateListener != null) {
			Log.d(TAG, "viewCreateListener is used");
			viewCreateListener.onCreate(contentView);
		}

	}

	@Override
	public Bundle onSaveInstanceState() {
		return super.onSaveInstanceState();
	}

	public interface DialogChangeListener {

	}

	public interface DialogCreateListener {
		public void onCreate(LinearLayout contentRootView);
	}

	public class Config implements Serializable {

		private static final long serialVersionUID = 1L;
		public Drawable titleIcon;
		public String titleText;
		public int titleTextColor = -1;
		public int titleBgColor = -1;
		public Drawable titleBgImage;
		public int contentBgColor = -1;
		public Drawable contentBgImage;
		public float heightPercent;
		public float widthPercent;

	}

	// �Ի�������-begin

	/**
	 * ���ñ���
	 * 
	 * @param resourceId
	 */
	public void setTitleText(int resourceId) {
		this.config.titleText = context.getResources().getString(resourceId);
	}

	/**
	 * ���ñ���
	 * 
	 * @param titleText
	 */
	public void setTitleText(String titleText) {
		this.config.titleText = titleText;
	}

	/**
	 * ���ñ���������ɫ
	 * 
	 * @param colorString
	 */
	public void setTitleTextColor(String colorString) {
		setTitleTextColor(Color.parseColor(colorString));

	}

	/**
	 * ���ñ��� ������ɫ
	 * 
	 * @param colorId
	 */
	public void setTitleTextColor(int colorId) {
		this.config.titleTextColor = colorId;
	}

	/**
	 * ���ñ���ͼ��
	 * 
	 * @param resourceId
	 */
	public void setTitleIcon(int resourceId) {
		config.titleIcon = getContext().getResources().getDrawable(resourceId);
	}

	/**
	 * ���ñ���ͼ��
	 * 
	 * @param bitmap
	 */
	public void setTitleIcon(Bitmap bitmap) {
		this.config.titleIcon = new BitmapDrawable(getContext().getResources(), bitmap);
	}

	/**
	 * ���ñ���ͼ��
	 * 
	 * @param drawable
	 */
	public void setTitleIcon(Drawable drawable) {
		this.config.titleIcon = drawable;
	}

	/**
	 * ���ñ��ⱳ����ɫ
	 * 
	 * @param colorString
	 */
	public void setTitleBgColor(String colorString) {
		setTitleBgColor(Color.parseColor(colorString));

	}

	/**
	 * ���ñ��ⱳ����ɫ
	 * 
	 * @param colorId
	 */
	public void setTitleBgColor(int colorId) {
		this.config.titleBgColor = colorId;
		this.config.titleBgImage = null;
	}

	/**
	 * ���ñ��ⱳ��ͼ
	 * 
	 * @param resourceId
	 */
	public void setTitleBgImage(int resourceId) {
		setTitleBgImage(getContext().getResources().getDrawable(resourceId));
	}

	/**
	 * ���ñ��ⱳ��ͼ
	 * 
	 * @param bitmap
	 */
	public void setTitleBgImage(Bitmap bitmap) {
		setTitleBgImage(new BitmapDrawable(getContext().getResources(), bitmap));
	}

	/**
	 * ���ñ��ⱳ��ͼ
	 * 
	 * @param drawable
	 */
	public void setTitleBgImage(Drawable drawable) {
		this.config.titleBgImage = drawable;
		this.config.titleBgColor = -1;
	}

	/**
	 * �������ı���ͼ
	 * 
	 * @param resourceId
	 */
	public void setContentBgImage(int resourceId) {
		setContentBgImage(getContext().getResources().getDrawable(resourceId));
	}

	/**
	 * �������ı���ͼ
	 * 
	 * @param bitmap
	 */
	public void setContentBgImage(Bitmap bitmap) {
		setContentBgImage(new BitmapDrawable(getContext().getResources(), bitmap));
	}

	/**
	 * �������ı���ͼ
	 * 
	 * @param drawable
	 */
	public void setContentBgImage(Drawable drawable) {
		this.config.contentBgImage = drawable;
		this.config.contentBgColor = -1;
	}

	/**
	 * �������ı�����ɫ
	 * 
	 * @param colorString
	 */
	public void setContentBgColor(String colorString) {
		setContentBgColor(Color.parseColor(colorString));
	}

	/**
	 * �������ı�����ɫ
	 * 
	 * @param colorId
	 */
	public void setContentBgColor(int colorId) {
		this.config.contentBgColor = colorId;
		this.config.contentBgImage = null;
	}

	/**
	 * ���öԻ����߱���
	 * 
	 * @param wp
	 * @param hp
	 */
	public void setSizePercent(float wp, float hp) {
		this.config.widthPercent = wp;
		this.config.heightPercent = hp;
	}

	/**
	 * ���öԻ���ȫ��
	 */
	public void setFullScreen() {
		setSizePercent(1.0f, 1.0f);
	}

	/**
	 * ���öԻ������
	 */
	public void setHalfScreen() {
		setSizePercent(0.5f, 0.5f);
	}
	// �Ի�������-end

}
