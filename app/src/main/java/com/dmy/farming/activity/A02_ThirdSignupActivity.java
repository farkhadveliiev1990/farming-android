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

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.api.signin3rdRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.umeng.socialize.UMShareAPI;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class A02_ThirdSignupActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	CommonModel commonModel;
	LoginModel loginModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a02_third);

		initView();

		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);
		loginModel = new LoginModel(this);
		loginModel.addResponseListener(this);
	}

	TextView text_reqcode;
	EditText edit_phone, edit_code;
	View button_signup;
	EventHandler eventHandler;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_phone = (EditText) findViewById(R.id.edit_phone);

		text_reqcode = (TextView) findViewById(R.id.text_reqcode);
		text_reqcode.setOnClickListener(this);

		edit_code = (EditText) findViewById(R.id.edit_code);

		button_signup = findViewById(R.id.button_signup);
		button_signup.setOnClickListener(this);

		// 创建EventHandler对象
		eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, final Object data) {
				if (data instanceof Throwable) {
					Throwable throwable = (Throwable)data;
					final String msg = throwable.getMessage();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (msg.contains("No address associated with hostname"))
								errorMsg("请检查网络");
							else
								errorMsg(msg);
						}
					});
				} else {
					if (result == SMSSDK.RESULT_COMPLETE){
						//回调完成
						if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
							//提交验证码成功
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
//									errorMsg("验证成功");
									signup();
								}
							});

						}else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									errorMsg("语音验证发送");
								}
							});
						}
						else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
							//获取验证码成功
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									startCount();
									errorMsg("验证码已发送");
								}
							});
						}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
							//获取支持的国家码

						}
					}
				}
			}
		};

		// 注册监听器
		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_reqcode:
				if ("".equals(edit_phone.getText().toString()))
					errorMsg("请输入手机号");
				else
					loginModel.verificationPhone(AppConst.info_from,edit_phone.getText().toString());

				break;
			case R.id.button_signup:
				strPhone = edit_phone.getText().toString();
				strCode = edit_code.getText().toString();
				SMSSDK.submitVerificationCode("86",strPhone,strCode);
				break;
		}
	}

	String strPhone = "";
	String strCode = "";

	private void getCode() {
		strPhone = edit_phone.getText().toString();
		if (!AppUtils.isMobileNum(strPhone))
		{
			errorMsg("请确认手机号码~");
		}
		else
		{
//			commonModel.reqCode(strPhone, 2);
			SMSSDK.getVerificationCode("86",strPhone);
		}
	}

	void signup()
	{
		if (!AppUtils.isMobileNum(strPhone))
			errorMsg("请确认手机号码~");
		else if (TextUtils.isEmpty(strCode))
			errorMsg("请输入短信验证码");
		else
		{
			loginModel.bindPhone(AppConst.info_from,strPhone);
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
			if (commonModel.lastStatus.error_code == 200) {
				startCount();
				errorMsg("验证码发送成功。请查收.");
			} else {
				errorMsg(commonModel.lastStatus.error_desc);
			}
		}
		else if(url.endsWith(ApiInterface.USER_CHECK_3RD))
		{
			if (loginModel.lastStatus.error_code == 200) {
				loginSucc();
			} else if (loginModel.lastStatus.error_code == 2) {
//				goto3rdPassword();
			} else {
				errorMsg(loginModel.lastStatus.error_desc);
			}
		}
		else if (url.contains(ApiInterface.USER_VERIFICATIONPHONE)){
			if (loginModel.lastStatus.error_code == 200) {
				getCode();
			}else {
				errorMsg(loginModel.lastStatus.error_desc);
			}
		}
		else if (url.contains(ApiInterface.USER_BINDPHONE)){
			if (loginModel.lastStatus.error_code == 200) {
				errorMsg("绑定成功");
				finish();
			}else
				errorMsg(loginModel.lastStatus.error_desc);
		}
	}

	void loginSucc()
	{
		errorMsg("登录成功");
		Intent intent = new Intent();
		intent.putExtra("login", true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	final static int REQUEST_LOGIN = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
		if(data != null)
		{
			if (requestCode == REQUEST_LOGIN) {
				loginSucc();
			}
		}
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		edit_phone.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		commonModel.removeResponseListener(this);
		loginModel.removeResponseListener(this);
		SMSSDK.unregisterEventHandler(eventHandler);
	}
}
