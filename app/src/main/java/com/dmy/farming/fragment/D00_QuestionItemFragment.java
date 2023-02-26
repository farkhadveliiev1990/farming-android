package com.dmy.farming.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.dmy.farming.R;
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.adapter.D00_QuestionListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.QuestionListModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class D00_QuestionItemFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";

	QuestionListModel dataModel;
	int type_id = 1; // 分类
	String user_attention = "";
	int page = 1;
	int BUFFER_TIME = 30 * 60 * 1000;
	int p = 1;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.d00_chat_item, null);

		initView(mainView);

		dataModel = new QuestionListModel(getContext(),type_id);
		dataModel.addResponseListener(this);

		Bundle bundle=getArguments();
		//判断需写
		if(bundle!=null)
		{
			user_attention = bundle.getString("mes");
			p=bundle.getInt("p");
		}

		return mainView;
	}

	XListView list_news;
	View null_pager;
	D00_QuestionListAdapter questionListAdapter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
		list_news = (XListView) mainView.findViewById(R.id.list_black);

		list_news.setPullLoadEnable(false);
		list_news.setPullRefreshEnable(true);
		list_news.setXListViewListener(this, 1);
		list_news.setAdapter(null);

//		list_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			Intent intent;
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				intent = new Intent( getContext(), D01_QuestionDetailActivity.class);
//				startActivity(intent);
//			}
//		});

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
		//user_attention = "XIAOMAI_CODE";
		dataModel.getQuestionList(user_attention,page,p ,bShow);
	}

	@Override
	public void onResume() {
//		updateData();
//		if (dataModel.data.lastUpdateTime + BUFFER_TIME < System.currentTimeMillis())
//		{
			requestData(true);
//		}
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {
		list_news.stopRefresh();
		list_news.stopLoadMore();

		if (dataModel.data.questions.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (questionListAdapter == null) {
				questionListAdapter = new D00_QuestionListAdapter(getContext(), dataModel.data.questions);
				list_news.setAdapter(questionListAdapter);
			} else {
				questionListAdapter.notifyDataSetChanged();
			}
			if (0 == dataModel.data.paginated.more) {
//				mFooter.setVisibility(View.VISIBLE);
				list_news.setPullLoadEnable(false);

			} else {
//				mFooter.setVisibility(View.GONE);
				list_news.setPullLoadEnable(true);
			}
		} else {
			questionListAdapter = null;
			list_news.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.QUESTIONLIST)) {
			list_news.setRefreshTime();
			list_news.setAdapter(null);
			updateData();


		}
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
