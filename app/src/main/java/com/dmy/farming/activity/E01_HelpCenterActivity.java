package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_HelpCenterAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.HelpCenterModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;
import com.dmy.farming.api.helpcenterRequest;

public class E01_HelpCenterActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_helpcenter);

		helpCenterModel = new HelpCenterModel(this);
		helpCenterModel.addResponseListener(this);
		initView();
		helpcenterRequest = new helpcenterRequest();

	}

	XListView list_black;
	View null_pager;
	HelpCenterModel helpCenterModel;
	E01_HelpCenterAdapter helpCenterAdapter;
	helpcenterRequest  helpcenterRequest;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		null_pager = findViewById(R.id.null_pager);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setXListViewListener(this, 1);
		list_black.setPullRefreshEnable(true);
		list_black.setPullLoadEnable(false);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
		}
	}

	String type = "0";
	String page = "1";

	public void requestData(boolean bShow)
	{
		helpcenterRequest.info_from = AppConst.info_from;
		helpcenterRequest.type = "0";
		helpcenterRequest.page = "1";
		helpCenterModel.getuserHelp(helpcenterRequest);
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

		if (helpCenterModel.data.helpcenters.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (helpCenterAdapter == null) {
				helpCenterAdapter = new E01_HelpCenterAdapter(this, helpCenterModel.data.helpcenters);
				list_black.setAdapter(helpCenterAdapter);
			} else {
				helpCenterAdapter.notifyDataSetChanged();
			}
			if (0 ==  helpCenterModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			helpCenterAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}



	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.USERHELP)) {
			updateData();
		}
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

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}


}