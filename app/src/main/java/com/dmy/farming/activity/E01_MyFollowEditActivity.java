package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_MyFollowEditListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;
import java.util.Map;

public class E01_MyFollowEditActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	DictionaryModel followModel;
	Map<String,String> attentionCrop;
	Button submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_follow_edit);

		attentionCrop = new LinkedHashMap<>();

		initView();

		followModel = new DictionaryModel(this);
		followModel.addResponseListener(this);
		followModel.followType(AppConst.info_from,true);
	}

	XListView list_black;
	View null_pager;
	GridView gridView;
	E01_MyFollowEditListAdapter followEditListAdapter;


	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		gridView= (GridView)findViewById(R.id.GridView1);
		submit = (Button)findViewById(R.id.submit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				followModel.deleteAttention(AppConst.info_from,followEditListAdapter.removelist,true);
			}
		});

	}

	private void requestData() {
		if (followModel.data.follow_type.size() > 0) {
			if (followEditListAdapter == null) {
				followEditListAdapter = new E01_MyFollowEditListAdapter(this, followModel.data.follow_type);
				gridView.setAdapter(followEditListAdapter);
			} else {
				followEditListAdapter.notifyDataSetChanged();
			}
		} else {
			followEditListAdapter = null;
			gridView.setAdapter(null);
		}
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.FOLLOWTYPE)) {
			requestData();
		}else if (url.contains(ApiInterface.DELETEATTENTION)){
			if (followModel.lastStatus.error_code == 200){
				finish();
			}else
				errorMsg(followModel.lastStatus.error_desc);
		}
	}

	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}


}