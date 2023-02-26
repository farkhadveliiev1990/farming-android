package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class B01_RouteCateFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	BaseActivity mActivity;

	int BUFFER_TIME = 30 * 60 * 1000;

	long tag_id = 1;

	int isShown;

	public int getIsShown()
	{
		return isShown;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		isShown = 0;
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.b01_news_cate, null);
		mActivity = (BaseActivity) getActivity();

		initView(mainView);
		return mainView;
	}

	XListView list_news;
	View null_pager;
	View footerView,mFooter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
		list_news = (XListView) mainView.findViewById(R.id.list_news);

		list_news.setPullLoadEnable(false);
		list_news.setPullRefreshEnable(true);
		list_news.setXListViewListener(this, 1);
		list_news.setAdapter(null);
	}

	public void setTagID(long tagID)
	{
		this.tag_id = tagID;
	}


	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
        {
		}
	}

	public void requestData(boolean bShow)
	{

	}

	@Override
	public void onResume() {
		isShown = 1;
		updateData();

		String city = AppUtils.getCurrCity(mActivity);

		super.onResume();
	}

	@Override
	public void onPause() {
		isShown = 0;
		super.onPause();
	}

	private void updateData() {
		list_news.stopRefresh();
		list_news.stopLoadMore();

	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

	}

	@Override
	public void onDestroyView() {
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
