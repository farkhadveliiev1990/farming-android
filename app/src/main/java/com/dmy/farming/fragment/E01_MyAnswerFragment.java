package com.dmy.farming.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.dmy.farming.R;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.model.MyQuestionModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class E01_MyAnswerFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_myquestion,null);
		mActivity = (BaseActivity) getActivity();

		initView(mainView);


		return mainView;
	}

	XListView list_black;
	View null_pager;


	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);


		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(false);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
        {

		}
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
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

	}

	@Override
	public void onLoadMore(int id) {

	}
}
