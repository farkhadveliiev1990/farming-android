package com.dmy.farming.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.R;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class C01_FindHelperFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	BaseActivity mActivity;

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
		View mainView = inflater.inflate(R.layout.c01_findhelper_item, null);
		mActivity = (BaseActivity) getActivity();




	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/

	//	initView(mainView);
		phone = mainView.findViewById(R.id.phone);
		phone.setOnClickListener(this);
		return mainView;
	}

	XListView list_news;
	View null_pager,phone;
	View footerView,mFooter;
	C00_MySellListAdapter listAdapter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
		list_news = (XListView) mainView.findViewById(R.id.list_news);

		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		list_news.addFooterView(footerView);

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

	public void requestData(boolean bShow)
	{
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {

	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
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

	}
}
