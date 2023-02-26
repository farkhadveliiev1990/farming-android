package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class C02_ExpertVideoActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c02_expert_video);

		initView();


	}

	Button btn_evaluate;
	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		btn_evaluate = (Button) findViewById(R.id.btn_evaluate);
		btn_evaluate.setOnClickListener(this);
	}

	Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.btn_evaluate:
				Intent intent = new Intent(C02_ExpertVideoActivity.this,C03_ExpertEvaluateActivity.class);
				startActivity(intent);
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
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


	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.EXPERT)) {

		}
	}

}