package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.activity.E01_MyCollectionActivity;
import com.dmy.farming.adapter.E01_MyCollectiotQuestionListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CollectionModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class E01_MyCollectionQuestionFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	E01_MyCollectionActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_my_collection_chat, null);
		mActivity = (E01_MyCollectionActivity) getActivity();

		collectionModel = new CollectionModel(mActivity);
		collectionModel.addResponseListener(this);
		initView(mainView);
		return mainView;
	}

	XListView list_black;
	View null_pager;
	E01_MyCollectiotQuestionListAdapter chatItemListAdapter;
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

	private void updateData() {

		list_black.stopRefresh();
		list_black.stopLoadMore();
		if (collectionModel.data.questionlists.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (chatItemListAdapter == null) {
				chatItemListAdapter = new E01_MyCollectiotQuestionListAdapter(mActivity, collectionModel.data.questionlists);
				list_black.setAdapter(chatItemListAdapter);

			} else {
				chatItemListAdapter.notifyDataSetChanged();
			}
			if (0 == collectionModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			chatItemListAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	public void requestData(boolean bShow)
	{
		collectionModel.collectionQuestionlist(AppConst.info_from,"3");
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
