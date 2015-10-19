package com.peer.activity;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.peer.R;
import com.peer.IMimplements.easemobchatImp;
import com.peer.base.Constant;
import com.peer.base.pBaseActivity;
import com.peer.net.HttpConfig;
import com.peer.net.HttpUtil;
import com.peer.utils.BussinessUtils;
import com.peer.utils.GetAddressUtil;
import com.peer.utils.ManagerActivity;
import com.peer.utils.pLog;
import com.peer.utils.pViewBox;

/**
 * 获取address类 加载city.json
 */
public class GetAddressInfoActivity extends pBaseActivity {

	List<String> addressList = null;
	boolean isCityChoose = false;
	String province = null;
	String city = null;

	private PageViewList pageViewaList;

	class PageViewList {
		private LinearLayout ll_back;
		private TextView tv_title;
		private ListView listview;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_info);
		findViewById();
		setListener();
		processBiz();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pageViewaList = new PageViewList();
		pViewBox.viewBox(this, pageViewaList);
		pageViewaList.tv_title.setText(getResources().getString(
				R.string.address));

		final GetAddressUtil location = new GetAddressUtil(this);
		addressList = location.getProvinceList();

		final ProvinceAdapter adapter = new ProvinceAdapter();
		pageViewaList.listview.setAdapter(adapter);

		pageViewaList.listview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						if (!isCityChoose) {
							province = addressList.get(arg2);
							addressList = location.getCityList(addressList
									.get(arg2));
							if (addressList.size() == 1) {
								Intent intent = new Intent();
								intent.putExtra("province", province);

								if (!addressList.get(0).equals(province)) {
									intent.putExtra("city", addressList.get(0));
									intent.putExtra("code", location
											.getCode(addressList.get(0)));
								} else {
									intent.putExtra("code",
											location.getCode(province));
								}

								setResult(Activity.RESULT_OK, intent);
								finish();
								return;
							}
							adapter.notifyDataSetChanged();
							isCityChoose = true;
						} else {
							city = addressList.get(arg2);
							Intent intent = new Intent();
							intent.putExtra("province", province);
							intent.putExtra("city", city);
							intent.putExtra("code", location.getCode(city));
							setResult(Activity.RESULT_OK, intent);
							finish();
						}

					}
				});
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		pageViewaList.ll_back.setOnClickListener(this);
	}

	@Override
	protected void processBiz() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

	}

	class ProvinceAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return addressList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return addressList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = GetAddressInfoActivity.this.getLayoutInflater().inflate(
						R.layout.item_addressinfo, null);
				convertView = view;
				convertView.setTag(view);
			} else {
				convertView = (View) convertView.getTag();
			}
			TextView text = (TextView) convertView
					.findViewById(R.id.item_address_city);
			pLog.i("text", addressList.get(position));
			text.setText(addressList.get(position));
			return convertView;
		}

	}

	@Override
	public void onNetworkOn() {
		// TODO Auto-generated method stub
//		sendSystemConfig();
	}

	@Override
	public void onNetWorkOff() {
		// TODO Auto-generated method stub

	}
	
}
