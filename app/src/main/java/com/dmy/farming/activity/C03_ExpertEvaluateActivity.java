package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.ExpertModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class C03_ExpertEvaluateActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	ExpertModel expertModel;
	String expert_id = "3c3c244546574d36a990af960f192c63";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c03_expert_evaluate);

		initView();

		expertModel = new ExpertModel(this);
		expertModel.addResponseListener(this);
	}

	Button btn_submit;
	TextView text_next,text_answer,text_attitude;
	RatingBar rat_answer,rat_attitude;
	EditText edit_evaluate;
	int answer = 0,attitude = 0;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);

		findViewById(R.id.text_next).setOnClickListener(this);

		text_answer = (TextView) findViewById(R.id.text_answer);
		text_attitude = (TextView) findViewById(R.id.text_attitude);
		edit_evaluate = (EditText) findViewById(R.id.edit_evaluate);

		rat_answer = (RatingBar) findViewById(R.id.rat_answer);
		rat_answer.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				answer = (int)rating;
			}
		});
		rat_attitude = (RatingBar) findViewById(R.id.rat_attitude);
		rat_attitude.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				attitude = (int)rating;
			}
		});
	}

	Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.btn_submit:
				submit();
				break;
			case R.id.text_next:
				expertModel.evaluateExpert(AppConst.info_from,expert_id,text_answer.getText().toString(),"5",text_attitude.getText().toString(),
						"5",edit_evaluate.getText().toString(),true);
				break;
		}
	}

	private void submit() {
		expertModel.evaluateExpert(AppConst.info_from,expert_id,text_answer.getText().toString(),answer+"",text_attitude.getText().toString(),
				attitude+"",edit_evaluate.getText().toString(),true);
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
		if (url.contains(ApiInterface.EVALUATEEXPERT)) {
            if (expertModel.lastStatus.error_code == 200) {
				finish();
				startActivity(new Intent(C03_ExpertEvaluateActivity.this,C01_ExpertListActivity.class));
			}
            else
            	errorMsg(expertModel.lastStatus.error_desc);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		expertModel.removeResponseListener(this);
	}
}