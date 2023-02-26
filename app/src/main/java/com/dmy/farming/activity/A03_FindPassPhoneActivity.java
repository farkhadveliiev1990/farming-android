package com.dmy.farming.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.api.model.LoginModel;
import com.external.androidquery.callback.AjaxStatus;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class A03_FindPassPhoneActivity extends BaseActivity implements OnClickListener, BusinessResponse,SeekBar.OnSeekBarChangeListener {

	CommonModel commonModel;
	LoginModel loginModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a03_findpass_phone);

		initView();
	/*	refreshState();

		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);*/

		loginModel = new LoginModel(this);
		loginModel.addResponseListener(this);

	}

/*	@Override
	protected void onDestroy() {
		if (commonModel != null)
		{
			commonModel.removeResponseListener(this);
			commonModel = null;
		}
		super.onDestroy();
	}
*/
	EditText edit_phone,edit_code;
	Button btn_reqcode,btn_next;
	EventHandler eventHandler;
	SeekBar seekBar;
	TextView tv;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_code = (EditText) findViewById(R.id.edit_code);
		btn_reqcode = (Button) findViewById(R.id.btn_reqcode);
		btn_reqcode.setOnClickListener(this);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_next.setEnabled(false);
		tv = (TextView) findViewById(R.id.tv);
		seekBar = (SeekBar) findViewById(R.id.sb);
		seekBar.setOnSeekBarChangeListener(this);

		// 创建EventHandler对象
		eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				if (data instanceof Throwable) {
					Throwable throwable = (Throwable)data;
					final String msg = throwable.getMessage();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
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
									tv.setText("完成验证");
									seekBar.setEnabled(false);
									btn_next.setEnabled(true);
//									errorMsg("验证成功");
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
									seekBar.setEnabled(true);
									seekBar.setProgress(0);
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
			case R.id.btn_reqcode:
				if (TextUtils.isEmpty(edit_phone.getText().toString()))
					errorMsg("请输入手机号");
				else
//					loginModel.verificationPhone(AppConst.info_from,edit_phone.getText().toString());
					getCode();

				break;
			case R.id.btn_next:
				if (TextUtils.isEmpty(strPhone)) {
					errorMsg("请确认手机号码~");
				}else {
					Intent intent = new Intent(A03_FindPassPhoneActivity.this, A03_FindPassPwdActivity.class);
					intent.putExtra("phone", strPhone);
					startActivity(intent);
					finish();
				}
				break;
		}
	}

        String strPhone = "",strCode = "";

        private void getCode() {
            strPhone = edit_phone.getText().toString();
            if (!AppUtils.isMobileNum(strPhone))
            {
                errorMsg("请确认手机号码~");
            }
            else
            {
				SMSSDK.getVerificationCode("86",strPhone);
            }
        }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (seekBar.getProgress() == seekBar.getMax()) {
			tv.setVisibility(View.VISIBLE);
			tv.setTextColor(getResources().getColor(R.color.white));
			strCode = edit_code.getText().toString();
			if (TextUtils.isEmpty(strCode)) {
				errorMsg("请输入验证码");
				seekBar.setProgress(0);
				return;
			}
			SMSSDK.submitVerificationCode("86",strPhone,strCode);
		} else {
			tv.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.getProgress() != seekBar.getMax()) {
			seekBar.setProgress(0);
			tv.setVisibility(View.VISIBLE);
			tv.setTextColor(getResources().getColor(R.color.text_grey));
			tv.setText("验证请按住滑块，拖动到最右边");
		}
	}

	Timer timer;
	int timeCount = 60;

	void startCount()
	{
		stopCount();
		btn_reqcode.setEnabled(false);
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
						btn_reqcode.setText(timeCount+"秒后重发");
					}
					break;
			}
		}
	};

	void stopCount()
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
		btn_reqcode.setEnabled(true);
		btn_reqcode.setText("获取验证码");
	}


	void setSucc()
	{
		Intent intent = new Intent();
		intent.putExtra("set", true);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.endsWith(ApiInterface.USER_REQCODE))
		{
			if (commonModel.lastStatus.succeed == 1)
			{
				Intent intent = new Intent(this, A02_SignupActivity.class);
				intent.putExtra("phone", strPhone);
				startActivityForResult(intent, REQUEST_SETPWD);
			}
			else
				errorMsg(commonModel.lastStatus.error_desc);
		}
//		else if (url.contains(ApiInterface.USER_VERIFICATIONPHONE)){
//			if (loginModel.lastStatus.error_code == 200) {
//				getCode();
//			}else {
//				errorMsg(loginModel.lastStatus.error_desc);
//			}
//		}
	}

	final static int REQUEST_SETPWD = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null)
		{
			if (requestCode == REQUEST_SETPWD) {
				setSucc();
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
}
