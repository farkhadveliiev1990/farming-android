package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.B00_WarningListAdapter;
import com.dmy.farming.adapter.C01_MarketPriceListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.api.model.BuyModel;
import com.dmy.farming.api.model.MarketPriceModel;
import com.dmy.farming.api.model.WarnModel;
import com.dmy.farming.api.warnRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class B00_WarningActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {

	WarnModel warnModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.b00_warning);

		warnModel = new WarnModel(this);
		warnModel.addResponseListener(this);
		wRequest = new warnRequest();
		initView();


	}

	XListView list_black;
	View null_pager;
	B00_WarningListAdapter warningListAdapter;
	warnRequest wRequest;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		null_pager = findViewById(R.id.null_pager);
		/*warningListAdapter = new B00_WarningListAdapter(this,warnModel.data.routes);
		list_black.setAdapter(warningListAdapter);*/
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
		}
	}

	@Override
	protected void onResume() {
		updateData();
		requestData(true);
		super.onResume();
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (warnModel.data.routes.size() > 0) {
			null_pager.setVisibility(View.GONE);
			if (warningListAdapter == null) {
				warningListAdapter = new B00_WarningListAdapter(this, warnModel.data.routes);
				list_black.setAdapter(warningListAdapter);

			} else {
				warningListAdapter.notifyDataSetChanged();
			}
			if (0 == warnModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			warningListAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	String info_from = "德铭源";
	int page = 1;

	public void requestData(boolean bShow) {
		//request.city = AppUtils.getCurrCity(mActivity);
		wRequest.city = AppUtils.getCurrCity(this);
		wRequest.district = AppUtils.getCurrDistrict(this);
		wRequest.province = AppUtils.getCurrProvince(this);
		wRequest.info_from = "德铭源";
		wRequest.page = 1;
		warnModel.getWarnList(wRequest,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.WARN)) {
			list_black.setRefreshTime();
			updateData();
		}
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);

	}

	@Override
	public void onLoadMore(int id) {
		wRequest.city = AppUtils.getCurrCity(this);
		wRequest.district = AppUtils.getCurrDistrict(this);
		wRequest.province = AppUtils.getCurrProvince(this);
		wRequest.info_from = "德铭源";
		page = page+1;
		wRequest.page = page;
		warnModel.getWarnListMore(wRequest, true);

	}



	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}


}