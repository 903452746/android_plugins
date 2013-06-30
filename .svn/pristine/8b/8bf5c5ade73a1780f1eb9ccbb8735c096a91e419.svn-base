package cn.com.lowe.android.widget.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import cn.com.lowe.android.tools.thread.InterruptThread;
import cn.com.lowe.android.tools.thread.InvokeId;
import cn.com.lowe.android.view.DictEditText;

/**
* @Description: 字典数据加载适配器
*
* @Author zhengjin 
* @Date 2013-6-29 下午5:48:03
* @Version 1.0
*/
public abstract class DataAdapter {
	private static final String TAG = "DataAdapter";
	private List<DictData> list;
	private volatile boolean loaded = false;
	private volatile DictLoadThread t;
	private Context mContext;
	private volatile OnSetDataListener setListener;
	private volatile SparseArray<DataAdapter.OnTranceDataListener> tranceMap;
	private Map<String,Object> getDataExtParams=new HashMap<String,Object>();
	private OnLoadDataListener loadDataListener;
	
	public DataAdapter(Context context) {
		mContext = context;
		list = new ArrayList<DictData>();
		tranceMap = new SparseArray<DataAdapter.OnTranceDataListener>();
	}

	public void requestData(OnSetDataListener listener) {
		if (t == null || !t.isAlive()) {
			Log.d(TAG, "启动加载字典数据线程");
			t = new DictLoadThread(mContext);
			t.execute(DictLoadThread.LoadDict);
		}
		setListener = listener;

	}

	public void tranceData(DictEditText dictEditText, OnTranceDataListener listener) {
		if (t == null || !t.isAlive()) {
			Log.d(TAG, "启动翻译字典数据线程");
			t = new DictLoadThread(mContext);
			t.execute(DictLoadThread.LoadDict);
		}
		tranceMap.put(dictEditText.hashCode(), listener);

	}

	/**
	* @Title: setLoadDataListener
	* @Description: 设置字典数据加载监听
	* @param loadDataListener
	* @return void
	*/
	public void setLoadDataListener(OnLoadDataListener loadDataListener) {
		this.loadDataListener = loadDataListener;
	}

	/**
	* @Title: setDataList
	* @Description: 字典数据加载抽象方法，具体实现看情况，注意：该方法在非UI线程环境执行，不可做UI改变操作
	* @param list
	* @param dataExtParams
	* @return
	* @return boolean
	*/
	public abstract boolean setDataList(final List<DictData> list,Map<String,Object> dataExtParams);

	/**
	* @Description: 加载数据的前置和后置监听，用于实现级联字典功能
	*
	* @Author zhengjin 
	* @Date 2013-6-29 下午5:49:43
	* @Version 1.0
	*/
	public interface OnLoadDataListener{
		public void beforeLoadData(Map<String,Object> dataExtParams);
		public void afterLoadData(Map<String,Object> dataExtParams,List<DictData> list);
	}
	
	/**
	* @Description: 加载数据进listview的回调接口
	*
	* @Author zhengjin 
	* @Date 2013-6-29 下午5:50:14
	* @Version 1.0
	*/
	public interface OnSetDataListener {
		public void setData(List<DictData> list);
	}

	/**
	* @Description: 字典数据翻译监听实现的回调接口
	*
	* @Author zhengjin 
	* @Date 2013-6-29 下午5:51:06
	* @Version 1.0
	*/
	public interface OnTranceDataListener {
		public void tranceData(List<DictData> list);
	}

	/**
	* @Title: isLoaded
	* @Description: 数据是否加载成功
	* @return
	* @return boolean
	*/
	public boolean isLoaded() {
		return loaded;
	}

	/**
	* @Description: 数据加载线程
	*
	* @Author zhengjin 
	* @Date 2013-6-29 下午5:52:24
	* @Version 1.0
	*/
	class DictLoadThread extends InterruptThread {
		public static final int LoadDict = 1;

		public DictLoadThread(Context context) {
			super(context);
			enableProcessDialog(true);
		}

		@InvokeId(LoadDict)
		public void getData() {
			updateDialogMsg("加载数据中,请稍后......");
			if (!isLoaded()) {
				Log.d(TAG, "字典数据正在请求中...");
				list.clear();
				if(loadDataListener!=null){
					loadDataListener.beforeLoadData(getDataExtParams);
				}
				loaded = setDataList(list,getDataExtParams);
				if(loadDataListener!=null){
					loadDataListener.afterLoadData(getDataExtParams, list);
				}
			} else {
				Log.d(TAG, "字典数据已请求,直接使用缓存数据");
			}

			uiHandler.post(new Runnable() {

				@Override
				public void run() {
					if (setListener != null) {
						Log.d(TAG, "执行加载字典数据");
						setListener.setData(list);
						setListener = null;
					}
					for(int i=0;i<tranceMap.size();i++){
						OnTranceDataListener tranceListener = tranceMap.get(tranceMap.keyAt(i));
						if (tranceListener != null) {
							Log.d(TAG, "执行翻译字典数据index=" + i + "]");
							tranceListener.tranceData(list);
						}
					}
					tranceMap.clear();

				}
			});

		}
	}

}
