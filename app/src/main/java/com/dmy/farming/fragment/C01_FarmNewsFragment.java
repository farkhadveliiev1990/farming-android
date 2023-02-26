package com.dmy.farming.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.R;
import com.dmy.farming.activity.C01_FarmNewsItemDetailActivity;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class C01_FarmNewsFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
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
		View mainView = inflater.inflate(R.layout.c01_farmnews_item, null);
		mActivity = (BaseActivity) getActivity();

	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/
		View guanbi =mainView.findViewById(R.id.guanbi);
		guanbi.setOnClickListener(this);
		layout_2 = mainView.findViewById(R.id.layout_2);
		guanbi_view = mainView.findViewById(R.id.guanbi_view);
		initView(mainView);
		return mainView;
	}

	C00_MySellListAdapter listAdapter;
	View layout_0,layout_2,guanbi_view;

	void initView(View mainView)
	{
		layout_0 = mainView.findViewById(R.id.layout_0);
		layout_0.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) 
	{
		Intent intent;
		switch(v.getId())
        {
			case R.id.layout_0:
				intent = new Intent(mActivity, C01_FarmNewsItemDetailActivity.class);
				startActivity(intent);
				break;
			case R.id.guanbi:
				layout_2.setVisibility(v.GONE);
				guanbi_view.setVisibility(v.GONE);
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
