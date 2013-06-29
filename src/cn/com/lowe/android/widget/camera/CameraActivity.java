package cn.com.lowe.android.widget.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.lowe.android.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.view.View.OnTouchListener;

public class CameraActivity extends Activity {
	private static final String TAG = "CameraActivity";

	private Camera mCamera;
	private CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_camera);
		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		FrameLayout crop = (FrameLayout) findViewById(R.id.camera_crop);
		CropPreview cropPreview = new CropPreview(this);
		crop.addView(cropPreview);
		Button captureButton = (Button) findViewById(R.id.button_capture);
		captureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, null, mPicture);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCamera.release();
	}

	/**
	* @Title: getCameraInstance
	* @Description: 获取相机
	* @return
	* @return Camera
	*/
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			Log.d(TAG, "打开相机失败", e);
		}
		return c;
	}
    
	public class CropPreview extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {
		private SurfaceHolder mHolder;
		private float x;
		private float y;
		private int w;
		private int h;
		public CropPreview(Context context) {
			super(context);
			mHolder = getHolder();
			mHolder.addCallback(this);
			this.setBackgroundResource(R.drawable.camera_bg);
			this.setOnTouchListener(this);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			x=getX();
			y=getY();
			w=width;
			h=height;
			Log.d(TAG, "x:"+x);
			Log.d(TAG, "y:"+y);
			Log.d(TAG, "w:"+w);
			Log.d(TAG, "h:"+h);
			
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
		    Log.d(TAG, "envent-x"+event.getX());
		    Log.d(TAG, "envent-y"+event.getY());
			return false;
		}
		
	}
	
	/** A basic Camera preview class */
	public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback{
		private SurfaceHolder mHolder;
		private Camera mCamera;

		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			mHolder = getHolder();
			mHolder.addCallback(this);
			// deprecated setting, but required on Android versions prior to 3.0
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		}

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (IOException e) {
				Log.d(TAG, "Error setting camera preview: " + e.getMessage());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			// empty. Take care of releasing the Camera preview in your
			// activity.
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			// If your preview can change or rotate, take care of those events
			// here.
			// Make sure to stop the preview before resizing or reformatting it.

			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}
			// stop preview before making changes
			try {
				mCamera.stopPreview();
			} catch (Exception e) {
				// ignore: tried to stop a non-existent preview
			}

			// set preview size and make any resize, rotate or
			// reformatting changes here

			// start preview with new settings
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception e) {
				Log.d(TAG, "Error starting camera preview: " + e.getMessage());
			}
		}

	}

	private PictureCallback mPicture = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
			if (pictureFile == null) {
				Log.d(TAG, "Error creating media file, check storage permissions: ");
				return;
			}

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
			} catch (FileNotFoundException e) {
				Log.d(TAG, "File not found: " + e.getMessage());
			} catch (IOException e) {
				Log.d(TAG, "Error accessing file: " + e.getMessage());
			}
		}
	};

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		Log.d(TAG, "存储文件目录:" + mediaStorageDir.getAbsolutePath());
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
}
