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
import com.dmy.farming.activity.C01_BuyActivity;
import com.dmy.farming.adapter.C00_BuyListAdapter;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.BuyModel;
import com.dmy.farming.api.model.BuyResponse;
import com.dmy.farming.api.publishBuyRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class C01_BuyFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;
	BuyModel buyModel;
	BuyResponse buyResponse;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.d00_chat_item, null);
		mActivity = (BaseActivity) getActivity();
		buyModel = new BuyModel(mActivity);
		buyModel.addResponseListener(this);
		request = new publishBuyRequest();
		initView(mainView);
		return mainView;
	}

	XListView list_news;
	View null_pager;
	C00_BuyListAdapter listAdapter;
	publishBuyRequest request;

	void initView(View mainView)
	{
		list_news = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);

//		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		//list_black.addFooterView(footerView);

		list_news.setPullLoadEnable(false);
		list_news.setPullRefreshEnable(true);
		list_news.setXListViewListener(this, 1);
		list_news.setAdapter(null);
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

	private void updateData() {
		list_news.stopRefresh();
		list_news.stopLoadMore();

		if (buyModel.data.routes.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C00_BuyListAdapter(mActivity, buyModel.data.routes);
				list_news.setAdapter(listAdapter);
			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 ==  buyModel.paginated.more) {
				list_news.setPullLoadEnable(false);
			} else {
				list_news.setPullLoadEnable(true);
			}
		} else {
			listAdapter = null;
			list_news.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

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

	String type_code = "0";
	String provience = "";
	String city = "";
	String district = "";
	double lon = 1;
	double lat = 1;
	int page = 1;
	String this_app = "1";
	String lon1 = "1";
	String lat1 = "1";

	public void requestData(boolean bShow)
	{
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(mActivity);
		request.provience = AppUtils.getCurrProvince(mActivity);
		request.district = AppUtils.getCurrDistrict(mActivity);
		request.lon = AppUtils.getCurrLon(mActivity);
		request.lat = AppUtils.getCurrLat(mActivity);
		request.page = page;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		buyModel.getBuyList(request,bShow);
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



	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.BUY)) {
			list_news.setRefreshTime();
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
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(mActivity);
		request.provience = AppUtils.getCurrProvince(mActivity);
		request.district = AppUtils.getCurrDistrict(mActivity);
		request.lon = AppUtils.getCurrLon(mActivity);
		request.lat = AppUtils.getCurrLat(mActivity);
		page = page+1;
		request.page = page;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		buyModel.getBuyListMore(request,true);
	}
}
