package com.dmy.farming.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.activity.E01_MyCollectionActivity;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.dmy.farming.adapter.C01_AgrotechniqueVideoAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CollectionModel;
import com.dmy.farming.api.model.FarmVideoModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class E01_MyCollectionVideoFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	E01_MyCollectionActivity mActivity;
	CollectionModel collectionModel;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_my_collection_video, null);
		mActivity = (E01_MyCollectionActivity) getActivity();

		initView(mainView);

		collectionModel = new CollectionModel(mActivity);
		collectionModel.addResponseListener(this);

		return mainView;
	}

	View null_pager;
	View footerView,mFooter;
	GridView grid_video;
	C01_AgrotechniqueVideoAdapter videoAdapter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);

		grid_video = (GridView) mainView.findViewById(R.id.grid_video);

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
		collectionModel.collectionVideolist(AppConst.info_from,"1");
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

	void updateData()
	{
		updateVideo();
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.COLLECTIONLIST)) {
			if (collectionModel.data.videolists.size() > 0)
				updateData();
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	private void updateVideo() {
		int size = collectionModel.data.videolists.size();
		if (size > 0) {
			null_pager.setVisibility(View.GONE);
			if (videoAdapter == null) {
				videoAdapter = new C01_AgrotechniqueVideoAdapter(mActivity , collectionModel.data.videolists);
				grid_video.setAdapter(videoAdapter);
			} else {
				videoAdapter.notifyDataSetChanged();
			}
		}else{
			null_pager.setVisibility(View.VISIBLE);
			videoAdapter = null;
			grid_video.setAdapter(null);
		}
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);
	}

	@Override
	public void onLoadMore(int id) {

	}
}
