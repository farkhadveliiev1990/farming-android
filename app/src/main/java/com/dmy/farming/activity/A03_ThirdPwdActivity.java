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

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.api.signin3rdRequest;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class A03_ThirdPwdActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	LoginModel loginModel;
	signin3rdRequest request = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a03_third_pwd);

		String strRequest = getIntent().getStringExtra("request");
		if (!TextUtils.isEmpty(strRequest)) {
			request = new signin3rdRequest();
			try {
				request.fromJson(new JSONObject(strRequest));
			} catch (JSONException e) {
				request = null;
				e.printStackTrace();
			}
		}

		initView();

		loginModel = new LoginModel(this);
		loginModel.addResponseListener(this);
	}

	@Override
	protected void onDestroy() {
		if (loginModel != null)
		{
			loginModel.removeResponseListener(this);
			loginModel = null;
		}
		super.onDestroy();
	}

	EditText edit_pass_0, edit_pass_1;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_pass_0 = (EditText) findViewById(R.id.edit_pass_0);
		edit_pass_1 = (EditText) findViewById(R.id.edit_pass_1);

		View button_ok = findViewById(R.id.button_ok);
		button_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.button_ok:
				send();
				break;
		}
	}

	String strPass_0 = "";
	String strPass_1 = "";

	void send()
	{
		strPass_0 = edit_pass_0.getText().toString();
		strPass_1 = edit_pass_1.getText().toString();

		if (TextUtils.isEmpty(strPass_0)) {
			errorMsg("请输入登录密码");
		} else if (TextUtils.isEmpty(strPass_1)) {
			errorMsg("请再次输入密码");
		} else if (!strPass_0.equals(strPass_1)) {
			errorMsg("两次密码不一致");
		} else if (strPass_0.length() < 6 || strPass_0.length() > 15)
			errorMsg("输入6—15位密码");
		else
		{
//			request.password = strPass_0;
			loginModel.signup3rd(request);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.endsWith(ApiInterface.USER_SIGNUP_3RD))
		{
			if (loginModel.lastStatus.error_code == 200)
			{
				Intent intent = new Intent();
				intent.putExtra("login", true);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			else
				errorMsg(loginModel.lastStatus.error_desc);
		}
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		edit_pass_0.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_pass_0.getWindowToken(), 0);
	}
}
