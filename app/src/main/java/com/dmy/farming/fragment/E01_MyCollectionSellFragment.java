package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.activity.E01_MyCollectionActivity;
import com.dmy.farming.adapter.E01_MyCollectionExpertListAdapter;
import com.dmy.farming.adapter.E01_MyCollectionSellListAdapter;
import com.dmy.farming.adapter.E01_MyCollectiotActicleListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CollectionModel;
import com.dmy.farming.api.model.SaleModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class E01_MyCollectionSellFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	E01_MyCollectionActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_my_collection_sell, null);
		mActivity = (E01_MyCollectionActivity) getActivity();

		collectionModel = new CollectionModel(mActivity);
		collectionModel.addResponseListener(this);
		initView(mainView);
		return mainView;
	}

	XListView list_black;
	View null_pager;
	E01_MyCollectionSellListAdapter sellListAdapter;
	View footerView,mFooter;
	CollectionModel collectionModel;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
		list_black = (XListView) mainView.findViewById(R.id.list_black);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);


	}



	@Override
	public void onClick(View v) 
	{
	}

	public void requestData(boolean bShow)
	{
		collectionModel.collectionGongqiulist(AppConst.info_from,"4");
	}

	private void updateData() {

		list_black.stopRefresh();
		list_black.stopLoadMore();
		if (collectionModel.data.salelists.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (sellListAdapter == null) {
				sellListAdapter = new E01_MyCollectionSellListAdapter(mActivity, collectionModel.data.salelists);
				list_black.setAdapter(sellListAdapter);

			} else {
				sellListAdapter.notifyDataSetChanged();
			}
			if (0 == collectionModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			sellListAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
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


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.COLLECTIONLIST)) {
			updateData();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		collectionModel.removeResponseListener(this);
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);
	}

	@Override
	public void onLoadMore(int id) {

	}
}
