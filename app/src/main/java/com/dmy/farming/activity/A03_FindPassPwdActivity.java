package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class A03_FindPassPwdActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	LoginModel loginModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a03_findpasspwd);

		phone = getIntent().getStringExtra("phone");

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

	EditText edit_pass_0,edit_pass_1;
	Button button_reset;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_pass_0 = (EditText) findViewById(R.id.edit_pass_0);
		edit_pass_1 = (EditText) findViewById(R.id.edit_pass_1);
		button_reset = (Button) findViewById(R.id.button_reset);
		button_reset.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.button_reset:
				resetPass();
				break;
		}
	}

	String phone = "";
	String strPass_0 = "";
	String strPass_1 = "";

	private void resetPass() {
		strPass_0 = edit_pass_0.getText().toString();
		strPass_1 = edit_pass_1.getText().toString();

	    if (TextUtils.isEmpty(strPass_0)) {
			errorMsg("请输入新密码");
		} else if (TextUtils.isEmpty(strPass_1)) {
			errorMsg("请再次输入新密码");
		} else if (!strPass_0.equals(strPass_1)) {
			errorMsg("两次密码不一致");
		} else if (strPass_0.length() < 6 || strPass_0.length() > 17)
			errorMsg("输入6—17位密码");
		else
		{
			loginModel.changePassword(phone, strPass_0);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.CHANGE_PWD))
		{
			if (loginModel.lastStatus.error_code==200)
			{
//				Intent intent = new Intent();
//				intent.putExtra("set", true);
//				setResult(Activity.RESULT_OK, intent);
//				finish();
				errorMsg("修改密码成功");
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
		edit_pass_1.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_pass_1.getWindowToken(), 0);
	}
}
