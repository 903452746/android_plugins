package cn.com.lowe.android.view;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import cn.com.lowe.android.R;
import cn.com.lowe.android.app.InjectActivity;
import cn.com.lowe.android.widget.dict.DataAdapter;
import cn.com.lowe.android.widget.dict.DataAdapter.OnLoadDataListener;
import cn.com.lowe.android.widget.dict.DictData;

public class ViewTestActivity extends InjectActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_view);

		testDict();
	}

	private void testDict() {
		final DictEditText signalText = (DictEditText) findViewById(R.id.dict_signal);
		DictEditText multipleText = (DictEditText) findViewById(R.id.dict_multiple);
		DataAdapter adapter = new DataAdapter(this) {
			@Override
			public boolean setDataList(List<DictData> list, Map<String, Object> dataExtParams) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for(int i=0;i<100;i++){
					list.add(new TestDictData("signalText-code"+i,"signalText-detail"+i,"signalText-spell"+i));
				}
				return true;
			}
		};
		
		signalText.setDataAdapter(adapter);
		DataAdapter adapter2 = new DataAdapter(this) {
			@Override
			public boolean setDataList(List<DictData> list, Map<String, Object> dataExtParams) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				String key=(String) dataExtParams.get("KEY");
				for(int i=0;i<100;i++){
					list.add(new TestDictData(key+"[code"+i+"]",key+"[detail"+i+"]",key+"[spell"+i+"]"));
				}
				return false;
			}
		};
		adapter2.setLoadDataListener(new OnLoadDataListener() {
			
			@Override
			public void beforeLoadData(Map<String, Object> dataExtParams) {
				dataExtParams.put("KEY", signalText.getCode());
				
			}
			
			@Override
			public void afterLoadData(Map<String, Object> dataExtParams, List<DictData> list) {
				
				
			}
		});
		multipleText.setDataAdapter(adapter2);

	}

	public class TestDictData extends DictData {
		private String code;
		private String detail;
		private String spell;

		public TestDictData(String code, String detail, String spell) {
			super();
			this.code = code;
			this.detail = detail;
			this.spell = spell;
		}

		@Override
		public String getCode() {
			return code;
		}

		@Override
		public String getDetail() {
			return detail;
		}

		@Override
		public String getSpell() {
			return spell;
		}

	}

}
