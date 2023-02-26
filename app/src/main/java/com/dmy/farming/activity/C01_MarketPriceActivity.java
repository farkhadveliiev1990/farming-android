package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_MarketPriceListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.MarkerPriceRequest;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.MarketPriceModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class C01_MarketPriceActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {


	MarketPriceModel marketPriceModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_market_price);


		marketPriceModel = new MarketPriceModel(this,0);
		marketPriceModel.addResponseListener(this);
		type_code = "";
		initView();
		markerPriceRequest = new MarkerPriceRequest();
		//updateData();
		requestData(true);

	}

	XListView list_black;
	//ListAdapter listAdapter;
	C01_MarketPriceListAdapter listAdapter;
	View null_pager;
	MarkerPriceRequest markerPriceRequest;


	void initView() {
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		findViewById(R.id.layout_filter).setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);
		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setPullLoadEnable(true);
		list_black.setPullRefreshEnable(false);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_filter:
				Intent intent = new Intent(C01_MarketPriceActivity.this,PriceFilterActivity.class);
				startActivityForResult(intent,REQUEST_FILTER);
				break;

		}
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (marketPriceModel.data.prices.size() > 0) {
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C01_MarketPriceListAdapter(this, marketPriceModel.data.prices);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == marketPriceModel.data.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	String info_from = "";
	String type_code = "";
	/*String provience = GLOBAL_DATA.getInstance(this).currProvince;
	String city = GLOBAL_DATA.getInstance(this).currCity;
	String district = GLOBAL_DATA.getInstance(this).currDistrict;*/
	String provience =AppUtils.getCurrProvince(this);
	String city = AppUtils.getCurrCity(this);
	String district = AppUtils.getCurrDistrict(this);
	int page = 1;
	String this_app = "1";
//	String user_phone = SESSION.getInstance().sid;
    String user_phone = "";
	String  type = "";

	public void requestData(boolean bShow) {
		//request.city = AppUtils.getCurrCity(mActivity);
		markerPriceRequest.info_from = AppConst.info_from;
		markerPriceRequest.crop_type = type_code;
		markerPriceRequest.provience = provience;
		markerPriceRequest.city =city;
		markerPriceRequest.district = district;
		markerPriceRequest.page = 1;
		markerPriceRequest.this_app = 1;
		markerPriceRequest.type = "";
		marketPriceModel.getpriceList(markerPriceRequest);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.PRICE)) {
			list_black.setRefreshTime();
			updateData();
		}
	}


	final static int REQUEST_FILTER = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_FILTER) {
				type_code = data.getStringExtra("crop");
				provience = data.getStringExtra("province").substring(0, data.getStringExtra("province").length()-1);
				city = data.getStringExtra("city").substring(0, data.getStringExtra("city").length()-1);
				district = data.getStringExtra("district").substring(0, data.getStringExtra("district").length()-1);
				requestData(false);
			}
		}
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);

	}

	@Override
	public void onLoadMore(int id) {
		markerPriceRequest.page++;
		marketPriceModel.getpriceListMore(markerPriceRequest, true);

	}
}

