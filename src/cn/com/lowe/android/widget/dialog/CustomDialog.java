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
	private static final String TAG = "CustomDialog";
	public static final float WRAP_CONTENT = -2.0f;
	private int viewResourceId;
	private DialogCreateListener viewCreateListener;
	private Context context;
	private LayoutInflater inflater;

	// 内部页面元素
	private LinearLayout contentView;
	private RelativeLayout titleContentView;
	private ImageView titleIconView;
	private TextView titleView;

	// 页面配置属性
	private Config config;
	private int screenWidth;
	private int screenHeight;
	// 暂时不用
	private int titleHeight;
	private int dialogWidth;
	private int dialogHeiht;

	private boolean isCreated = false;

	public CustomDialog(Context context, int viewResourceId) {
		this(context, viewResourceId, null, 0);
	}

	public CustomDialog(Context context, int viewResourceId, int theme) {
		this(context, viewResourceId, null, theme);
	}

	public CustomDialog(Context context, DialogCreateListener viewCreateListener) {
		this(context, 0, viewCreateListener, 0);
	}

	public CustomDialog(Context context, DialogCreateListener viewCreateListener, int theme) {
		this(context, 0, viewCreateListener, theme);
	}

	public CustomDialog(Context context, int viewResourceId, DialogCreateListener viewCreateListener) {
		this(context, viewResourceId, viewCreateListener, 0);
	}

	public CustomDialog(Context context, int viewResourceId, DialogCreateListener viewCreateListener, int theme) {
		super(context, theme);
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
		if (config.dialogFrameId != 0) {
			setContentView(config.dialogFrameId);
		} else {
			setContentView(R.layout.android_customdialog);
		}
		contentView = (LinearLayout) findViewById(R.id.widget_customdialog_content);
		titleIconView = (ImageView) findViewById(R.id.widget_customdialog_title_icon);
		titleView = (TextView) findViewById(R.id.widget_customdialog_title_text);
		titleContentView = (RelativeLayout) findViewById(R.id.widget_customdialog_title);
		if(contentView==null||titleIconView==null||titleView==null||titleContentView==null){
			throw new RuntimeException("自定义对话框顶层布局是要包含如下元素Id[widget_customdialog_content|widget_customdialog_title_icon|widget_customdialog_title_text|widget_customdialog_title]");
		}
		createContentUI();
		isCreated = true;

	}

	@Override
	protected void onStart() {
		super.onStart();
		initConfig();

	}

	@Override
	@Deprecated
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	@Override
	@Deprecated
	public void setTitle(int titleId) {
		// TODO Auto-generated method stub
		super.setTitle(titleId);
	}

	private void initConfig() {
		if (!isCreated) {
			return;
		}

		if (config.titleIcon != null) {
			titleIconView.setImageDrawable(config.titleIcon);
		}
		if (!TextUtils.isEmpty(config.titleText)) {
			titleView.setText(config.titleText);
		}
		if (config.titleTextColor != Integer.MAX_VALUE) {
			titleView.setTextColor(config.titleTextColor);
		}
		if (config.titleBgColor != Integer.MAX_VALUE) {
			titleContentView.setBackgroundColor(config.titleBgColor);
		} else if (config.titleBgImage != null) {
			titleContentView.setBackground(config.titleBgImage);
		}
		if (config.contentBgColor != Integer.MAX_VALUE) {
			contentView.setBackgroundColor(config.contentBgColor);
		} else if (config.contentBgImage != null) {
			contentView.setBackground(config.contentBgImage);
		}

		// 对话框宽高设置
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		titleHeight = titleContentView.getHeight();

		LayoutParams layoutParams = contentView.getLayoutParams();
		// 宽高比计算
		if (config.widthPercent <= 1 && config.widthPercent > 0) {
			dialogWidth = (int) (screenWidth * config.widthPercent);
			layoutParams.width = dialogWidth;
		}
		if (config.heightPercent <= 1 && config.heightPercent > 0) {
			dialogHeiht = (int) (screenHeight * config.heightPercent);
			layoutParams.height = dialogHeiht;
		}

		// 高度需要剔除标题高 度
		// 取到的高度一直为0，还没找到解决方案
		if (dialogHeiht != WRAP_CONTENT) {
			dialogHeiht -= titleHeight;
		}

		contentView.setLayoutParams(layoutParams);
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

	public interface DialogCreateListener {
		public void onCreate(LinearLayout contentRootView);
	}

	public class Config implements Serializable {

		private static final long serialVersionUID = 1L;
		public Drawable titleIcon;
		public String titleText;
		public int titleTextColor = Integer.MAX_VALUE;
		public int titleBgColor = Integer.MAX_VALUE;
		public Drawable titleBgImage;
		public int contentBgColor = Integer.MAX_VALUE;
		public Drawable contentBgImage;
		public float heightPercent = 100;
		public float widthPercent = 100;
		public int dialogFrameId = 0;
	}

	// 对话框配置-begin
	public void setDialogFrame(int dialogFrameId) {
		this.config.dialogFrameId = dialogFrameId;
	}

	/**
	 * 设置标题
	 * 
	 * @param resourceId
	 */
	public void setTitleText(int resourceId) {
		this.config.titleText = context.getResources().getString(resourceId);
		initConfig();
	}

	/**
	 * 设置标题
	 * 
	 * @param titleText
	 */
	public void setTitleText(String titleText) {
		this.config.titleText = titleText;
		initConfig();
	}

	/**
	 * 设置标题文字颜色
	 * 
	 * @param colorString
	 */
	public void setTitleTextColor(String colorString) {
		setTitleTextColor(Color.parseColor(colorString));
		initConfig();

	}

	/**
	 * 设置标题 文字颜色
	 * 
	 * @param colorId
	 */
	public void setTitleTextColor(int colorId) {
		this.config.titleTextColor = colorId;
		initConfig();
	}

	/**
	 * 设置标题图标
	 * 
	 * @param resourceId
	 */
	public void setTitleIcon(int resourceId) {
		config.titleIcon = getContext().getResources().getDrawable(resourceId);
		initConfig();
	}

	/**
	 * 设置标题图标
	 * 
	 * @param bitmap
	 */
	public void setTitleIcon(Bitmap bitmap) {
		this.config.titleIcon = new BitmapDrawable(getContext().getResources(), bitmap);
		initConfig();
	}

	/**
	 * 设置标题图标
	 * 
	 * @param drawable
	 */
	public void setTitleIcon(Drawable drawable) {
		this.config.titleIcon = drawable;
		initConfig();
	}

	/**
	 * 设置标题背景颜色
	 * 
	 * @param colorString
	 */
	public void setTitleBgColor(String colorString) {
		setTitleBgColor(Color.parseColor(colorString));
		initConfig();

	}

	/**
	 * 设置标题背景颜色
	 * 
	 * @param colorId
	 */
	public void setTitleBgColor(int colorId) {
		this.config.titleBgColor = colorId;
		this.config.titleBgImage = null;
		initConfig();
	}

	/**
	 * 设置标题背景图
	 * 
	 * @param resourceId
	 */
	public void setTitleBgImage(int resourceId) {
		setTitleBgImage(getContext().getResources().getDrawable(resourceId));
		initConfig();
	}

	/**
	 * 设置标题背景图
	 * 
	 * @param bitmap
	 */
	public void setTitleBgImage(Bitmap bitmap) {
		setTitleBgImage(new BitmapDrawable(getContext().getResources(), bitmap));
		initConfig();
	}

	/**
	 * 设置标题背景图
	 * 
	 * @param drawable
	 */
	public void setTitleBgImage(Drawable drawable) {
		this.config.titleBgImage = drawable;
		this.config.titleBgColor = Integer.MAX_VALUE;
		initConfig();
	}

	/**
	 * 设置正文背景图
	 * 
	 * @param resourceId
	 */
	public void setContentBgImage(int resourceId) {
		setContentBgImage(getContext().getResources().getDrawable(resourceId));
		initConfig();
	}

	/**
	 * 设置正文背景图
	 * 
	 * @param bitmap
	 */
	public void setContentBgImage(Bitmap bitmap) {
		setContentBgImage(new BitmapDrawable(getContext().getResources(), bitmap));
		initConfig();
	}

	/**
	 * 设置正文背景图
	 * 
	 * @param drawable
	 */
	public void setContentBgImage(Drawable drawable) {
		this.config.contentBgImage = drawable;
		this.config.contentBgColor = Integer.MAX_VALUE;
		initConfig();
	}

	/**
	 * 设置正文背景颜色
	 * 
	 * @param colorString
	 */
	public void setContentBgColor(String colorString) {
		setContentBgColor(Color.parseColor(colorString));
		initConfig();
	}

	/**
	 * 设置正文背景颜色
	 * 
	 * @param colorId
	 */
	public void setContentBgColor(int colorId) {
		this.config.contentBgColor = colorId;
		this.config.contentBgImage = null;
		initConfig();
	}

	/**
	 * 设置对话框宽高比例
	 * 
	 * @param wp
	 * @param hp
	 */
	public void setSizePercent(float wp, float hp) {
		this.config.widthPercent = wp;
		this.config.heightPercent = hp;
	}

	/**
	 * 设置对话框全屏
	 */
	public void setFullScreen() {
		setSizePercent(1.0f, 1.0f);
	}

	/**
	 * 设置对话框半屏
	 */
	public void setHalfScreen() {
		setSizePercent(0.5f, 0.5f);
	}
	// 对话框配置-end

}
