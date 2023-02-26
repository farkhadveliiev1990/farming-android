package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dmy.farming.R;
import com.dmy.farming.activity.MainActivity;
import com.dmy.farming.adapter.B00_WarningListAdapter;
import com.dmy.farming.adapter.E01_MyCollectionExpertListAdapter;
import com.dmy.farming.api.model.WarnModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class B01_WarnFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";

	WarnModel warnModel;
	BaseActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.b00_warning, null);

		warnModel = new WarnModel(mActivity);
		warnModel.addResponseListener(mActivity);

		initView(mainView);
		return mainView;
	}

	XListView list_black;
	View null_pager;
	B00_WarningListAdapter chatItemListAdapter;
	View footerView,mFooter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
	//	list_news = (XListView) mainView.findViewById(R.id.list_answer);

		chatItemListAdapter = new B00_WarningListAdapter(mActivity,warnModel.data.routes);
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		null_pager = mainView.findViewById(R.id.null_pager);

	}



	@Override
	public void onClick(View v) 
	{
	}

	public void requestData(boolean bShow)
	{

	}

	@Override
	public void onResume() {
		//updateData();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/*private void updateData() {
		list_news.stopRefresh();
		list_news.stopLoadMore();

	}*/


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
