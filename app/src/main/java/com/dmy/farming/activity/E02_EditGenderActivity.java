package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.utils.IDCard;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;


public class E02_EditGenderActivity extends BaseActivity implements OnClickListener, BusinessResponse {


	String gender = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e02_edit_gender);

		gender = getIntent().getStringExtra("gender");
		initView();
	}

	ImageView img_male,img_female;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		findViewById(R.id.layout_male).setOnClickListener(this);
		findViewById(R.id.layout_female).setOnClickListener(this);

		img_male = (ImageView) findViewById(R.id.img_male);
		img_female = (ImageView) findViewById(R.id.img_female);

		if ("0".equals(gender)) {
			img_male.setVisibility(View.VISIBLE);
			img_female.setVisibility(View.INVISIBLE);
		}else if ("1".equals(gender)){
			img_male.setVisibility(View.INVISIBLE);
			img_female.setVisibility(View.VISIBLE);
		}

		View button_save = findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_male:
				img_male.setVisibility(View.VISIBLE);
				img_female.setVisibility(View.INVISIBLE);
				gender = "0";
				break;
			case R.id.layout_female:
				img_male.setVisibility(View.INVISIBLE);
				img_female.setVisibility(View.VISIBLE);
				gender = "1";
				break;
			case R.id.button_save:
				clickOK();
				break;
		}
	}


	void clickOK()
	{
		successClose();
	}

	void successClose() {
		Intent intent = new Intent();
		intent.putExtra("value", gender);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
//		if (url.endsWith(ApiInterface.GROUP_REQ)) {
//			if (groupModel.lastStatus.succeed == 1) {
//				errorMsg("提交成功");
//				successClose();
//			} else {
//				errorMsg(groupModel.lastStatus.error_desc);
//			}
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}


}
