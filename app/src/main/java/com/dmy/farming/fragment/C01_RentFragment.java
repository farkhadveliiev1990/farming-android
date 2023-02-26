package com.dmy.farming.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.dmy.farming.adapter.C01_RentListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.RentModel;
import com.dmy.farming.api.model.RentResponse;
import com.dmy.farming.api.publishRentRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class C01_RentFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	BaseActivity mActivity;

	RentModel rentModel;
	RentResponse rentResponse;
	publishRentRequest request;

/*	RouteListModel dataModel;
	int BUFFER_TIME = 30 * 60 * 1000;

	routeListRequest request;*/


	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.d00_chat_item, null);
		mActivity = (BaseActivity) getActivity();




		rentModel = new RentModel(mActivity);
		rentModel.addResponseListener(this);
	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/
		request = new publishRentRequest();

		initView(mainView);

		return mainView;
	}

	XListView list_black;
	View null_pager;
	View footerView,mFooter;
	C01_RentListAdapter listAdapter;

	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);

//		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		//list_black.addFooterView(footerView);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}



	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
        {
			case  R.id.phone:
				call("057187063728");
				break;
		}
	}


	@Override
	public void onResume() {
		updateData();
		requestData(true);
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (rentModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C01_RentListAdapter(mActivity, rentModel.data.data);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == rentModel.paginated.more) {
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


	String info_from= "1";
	String type_code = "辽宁";
	String provience = "1";
	String city = "1";
	String district = "1";
	float lon = 1;
	float lat = 1;
	int page = 1;
	String this_app = "1";
	String lon1 = "1";
	String lat1 = "1";


	public void requestData(boolean bShow)
	{
		//request.city = AppUtils.getCurrCity(mActivity);
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(mActivity);
		request.provience = AppUtils.getCurrProvince(mActivity);
		request.district = AppUtils.getCurrDistrict(mActivity);
		request.lon = AppUtils.getCurrLon(mActivity);
		request.lat = AppUtils.getCurrLat(mActivity);
		request.page = 1;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		rentModel.getrentList(request,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.RENT)) {
			list_black.setRefreshTime();
			updateData();
		}
	}

	@Override
	public void onDestroyView() {
		listAdapter = null;
		super.onDestroyView();
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);
	}

	@Override
	public void onLoadMore(int id) {
		page = page+1;
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(mActivity);
		request.provience = AppUtils.getCurrProvince(mActivity);
		request.district = AppUtils.getCurrDistrict(mActivity);
		request.lon = AppUtils.getCurrLon(mActivity);
		request.lat = AppUtils.getCurrLat(mActivity);
		request.page =page;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		rentModel.getrentListMore(request,true);
	}
}
