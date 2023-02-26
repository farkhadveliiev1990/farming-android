package com.dmy.farming.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.UserInfoModel;
import com.dmy.farming.api.userUpdatePhoneRequest;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class E03_ResetPhoneActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	CommonModel commonModel;
	UserInfoModel infoModel;

	userUpdatePhoneRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e03_reset_phone);

		initView();

		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);
		infoModel = new UserInfoModel(this);
		infoModel.addResponseListener(this);

		request = new userUpdatePhoneRequest();
	}

	@Override
	protected void onDestroy() {
		if (commonModel != null)
		{
			commonModel.removeResponseListener(this);
			commonModel = null;
		}
		if (infoModel != null)
		{
			infoModel.removeResponseListener(this);
			infoModel = null;
		}
		super.onDestroy();
	}

	EditText edit_pass, edit_phone, edit_code;
	TextView text_reqcode;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_pass = (EditText) findViewById(R.id.edit_pass);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_code = (EditText) findViewById(R.id.edit_code);
		text_reqcode = (TextView) findViewById(R.id.text_reqcode);
		text_reqcode.setOnClickListener(this);

		View button_ok = findViewById(R.id.button_ok);
		button_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_reqcode:
				getCode();
				break;
			case R.id.button_ok:
				send();
				break;
		}
	}

	String strPhone = "";
	String strCode = "";
	String strPass = "";

	private void getCode() {
		strPhone = edit_phone.getText().toString();
		if (AppUtils.isMobileNum(strPhone))
			commonModel.reqCode(strPhone, 1);
		else
			errorMsg("请确认手机号码~");
	}

	void send()
	{
		strCode = edit_code.getText().toString();
		strPass = edit_pass.getText().toString();
		if (TextUtils.isEmpty(strCode))
		{
			errorMsg("请输入验证码");
		}
		else if (TextUtils.isEmpty(strPass) || strPass.length() < 6 || strPass.length() > 15)
			errorMsg("输入6—15位密码");
		else
		{
			request.newphone = strPhone;
			request.password = strPass;
			request.idcode = strCode;
//			infoModel.updatePhone(request);
		}
	}

	Timer timer;
	int timeCount = 60;

	void startCount()
	{
		stopCount();
		text_reqcode.setEnabled(false);
		timeCount = 60;
		timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {

				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}

	void stopCount()
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
		text_reqcode.setEnabled(true);
		text_reqcode.setText("发送验证码");
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch(msg.what) {
				case 1:
					timeCount --;
					if (timeCount <= 0)
					{
						stopCount();
					}
					else
					{
						text_reqcode.setText(timeCount+" s");
					}
					break;
			}
		}
	};

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.endsWith(ApiInterface.USER_REQCODE))
		{
			if (commonModel.lastStatus.error_code == 200)
			{
				startCount();
				errorMsg("验证码发送成功。请查收.");
			}
			else
				errorMsg(commonModel.lastStatus.error_desc);
		}
//		else if (url.endsWith(ApiInterface.USER_UPDATE_PHONE))
//		{
//			if (infoModel.lastStatus.succeed == 1)
//			{
//				Intent intent = new Intent();
//				intent.putExtra("value", strPhone);
//				setResult(Activity.RESULT_OK, intent);
//				finish();
//			}
//			else
//				errorMsg(infoModel.lastStatus.error_desc);
//		}
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		edit_code.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_code.getWindowToken(), 0);
	}
}
