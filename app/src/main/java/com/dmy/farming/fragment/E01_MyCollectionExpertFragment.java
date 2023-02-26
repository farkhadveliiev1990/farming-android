package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_FindHelperListAdapter;
import com.dmy.farming.adapter.E01_MyCollectionExpertListAdapter;
import com.dmy.farming.api.model.CollectionModel;
import com.dmy.farming.api.model.FindHelpModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class E01_MyCollectionExpertFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	BaseActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_my_collection_expert, null);
		mActivity = (BaseActivity) getActivity();

		collectionModel = new CollectionModel(mActivity);
		collectionModel.addResponseListener(this);

		initView(mainView);
		return mainView;
	}

	CollectionModel collectionModel;
	XListView list_black;
	View null_pager;
	E01_MyCollectionExpertListAdapter chatItemListAdapter;
	View footerView,mFooter;

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

	}

	@Override
	public void onResume() {
		updateData();
	//	requestData(true);
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {

//		list_black.stopRefresh();
//		list_black.stopLoadMore();
//		if (collectionModel.data.articlelists.size() > 0)
//		{
//			null_pager.setVisibility(View.GONE);
//			if (chatItemListAdapter == null) {
//				chatItemListAdapter = new E01_MyCollectionExpertListAdapter(mActivity, collectionModel.data.articlelists);
//				list_black.setAdapter(chatItemListAdapter);
//
//			} else {
//				chatItemListAdapter.notifyDataSetChanged();
//			}
//			if (0 == collectionModel.paginated.more) {
//				list_black.setPullLoadEnable(false);
//			} else {
//				list_black.setPullLoadEnable(true);
//			}
//		} else {
//			chatItemListAdapter = null;
//			list_black.setAdapter(null);
//			null_pager.setVisibility(View.VISIBLE);
//		}

		null_pager.setVisibility(View.VISIBLE);

	}


	String info_from= "1";
	String type_code = "辽宁";
	String provience = "1";
	String city = "1";
	String district = "1";
	float lon = 1;
	float lat = 1;
	String page = "1";
	String this_app = "1";


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {

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
