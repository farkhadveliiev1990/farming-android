package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_ActcenterListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.ACTIVITYCENTER;
import com.dmy.farming.api.model.ActivityCenterModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class E01_ActCenterActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	ActivityCenterModel activityCenterModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_actcenter);

		initView();

		activityCenterModel = new ActivityCenterModel(this);
		activityCenterModel.addResponseListener(this);

	}

	XListView list_black;
	View null_pager;
	E01_ActcenterListAdapter listAdapter ;
	int page = 1;
	List<ACTIVITYCENTER> datas;

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

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (activityCenterModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new E01_ActcenterListAdapter(this,activityCenterModel.data.data);

				list_black.setAdapter(listAdapter);
			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == activityCenterModel.data.paginated.more) {
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

	@Override
	protected void onResume() {
		super.onResume();
		activityCenterModel.getActivityList(AppConst.info_from,page+"",true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityCenterModel.removeResponseListener(this);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.ACTIVITYCENTERLIST)) {
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
        activityCenterModel.getActivityList(AppConst.info_from,page+"",false);
	}

	@Override
	public void onLoadMore(int id) {
		activityCenterModel.getActivityListMore(AppConst.info_from,(page++)+"");
	}
}