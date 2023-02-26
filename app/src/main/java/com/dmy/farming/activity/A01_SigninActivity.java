package com.dmy.farming.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.signin3rdRequest;
import com.external.androidquery.callback.AjaxStatus;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.api.ApiInterface;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class A01_SigninActivity extends BaseActivity implements OnClickListener, BusinessResponse,SeekBar.OnSeekBarChangeListener{

	CommonModel commonModel;
	LoginModel loginModel;
	//ConversationModel conversationModel;
	signin3rdRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a01_signin);

		initView();

		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);
		loginModel = new LoginModel(this);
		loginModel.addResponseListener(this);

		request = new signin3rdRequest();

		tv = (TextView) findViewById(R.id.tv);
		seekBar = (SeekBar) findViewById(R.id.sb);
		seekBar.setOnSeekBarChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		if (commonModel != null) {
			commonModel.removeResponseListener(this);
			commonModel = null;
		}
		if (loginModel != null) {
			loginModel.removeResponseListener(this);
			loginModel = null;
		}
		super.onDestroy();
		SMSSDK.unregisterEventHandler(eventHandler);
	}

	EditText edit_phone_signin, edit_password, edit_phone_signup, edit_code, edit_recode;
	Button btn_reqcode;
    View signin, signup,img_weixin_lasttime,img_qq_lasttime;
	TextView text_tab_1, text_tab_2;
	ImageView showPwdImageView;
	View img_line_1, img_line_2;
	boolean isChecked = false;
	TextView tv;
	SeekBar seekBar;
	EventHandler eventHandler;

	void initView() {

        text_tab_1 = (TextView) findViewById(R.id.text_tab_1);
        text_tab_1.setOnClickListener(this);

		text_tab_2 = (TextView) findViewById(R.id.text_tab_2);
		text_tab_2.setOnClickListener(this);

		img_line_1 = findViewById(R.id.img_line_1);
		img_line_2 = findViewById(R.id.img_line_2);
		findViewById(R.id.img_back).setOnClickListener(this);


		View text_forget = findViewById(R.id.text_forget);
		text_forget.setOnClickListener(this);

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);

		edit_phone_signin = (EditText) signin.findViewById(R.id.edit_phone);

		// 登录页
		View button_login = signin.findViewById(R.id.button_login);
		button_login.setOnClickListener(this);
		edit_password = (EditText) signin.findViewById(R.id.edit_password);
		showPwdImageView = (ImageView)findViewById(R.id.showPwdImageView);
		showPwdImageView.setClickable(true);
		showPwdImageView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				isChecked = !isChecked;
				if(isChecked)
				{
					showPwdImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_display));
					edit_password.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else
				{
					showPwdImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
					edit_password.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		View img_weixin = signin.findViewById(R.id.img_weixin);
		img_weixin.setOnClickListener(this);
		View img_qq = signin.findViewById(R.id.img_qq);
		img_qq.setOnClickListener(this);

		img_weixin_lasttime = signin.findViewById(R.id.img_weixin_lasttime);
		img_qq_lasttime = signin.findViewById(R.id.img_qq_lasttime);
		if ("0".equals(AppUtils.getStringValue(this,"login_type"))){
			img_weixin_lasttime.setVisibility(View.VISIBLE);
			img_qq_lasttime.setVisibility(View.INVISIBLE);
		}else if ("1".equals(AppUtils.getStringValue(this,"login_type"))){
			img_weixin_lasttime.setVisibility(View.INVISIBLE);
			img_qq_lasttime.setVisibility(View.VISIBLE);
		}else {
			img_weixin_lasttime.setVisibility(View.INVISIBLE);
			img_qq_lasttime.setVisibility(View.INVISIBLE);
		}

		View button_login1  = signup.findViewById(R.id.button_login1);
		button_login1.setOnClickListener(this);

		// 注册页
		btn_reqcode = (Button) signup.findViewById(R.id.btn_reqcode);
		btn_reqcode.setOnClickListener(this);
		edit_phone_signup = (EditText) signup.findViewById(R.id.edit_phone);
		edit_code = (EditText) signup.findViewById(R.id.edit_code);
		edit_recode = (EditText) signup.findViewById(R.id.edit_recode);
		seekBar = (SeekBar) findViewById(R.id.sb);
		seekBar.setOnSeekBarChangeListener(this);

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
									tv.setText("完成验证");
									seekBar.setEnabled(false);
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

	final static int REQUEST_LOGIN = 1;
	final static int REQUEST_SIGNUP = 2;

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.img_weixin:
				thirdLogin(SHARE_MEDIA.WEIXIN);
				break;
			case R.id.img_qq:
				thirdLogin(SHARE_MEDIA.QQ);
				break;
            case R.id.text_tab_1:
            	text_tab_1.setTextColor(getResources().getColor(R.color.green));
				img_line_1.setVisibility(View.VISIBLE);
            	text_tab_2.setTextColor(getResources().getColor(R.color.black));
				img_line_2.setVisibility(View.GONE);
                signin.setVisibility(View.VISIBLE);
                signup.setVisibility(View.GONE);
                break;
			case R.id.text_tab_2:
				text_tab_1.setTextColor(getResources().getColor(R.color.black));
				img_line_1.setVisibility(View.GONE);
				text_tab_2.setTextColor(getResources().getColor(R.color.green));
				img_line_2.setVisibility(View.VISIBLE);
                signin.setVisibility(View.GONE);
                signup.setVisibility(View.VISIBLE);
				break;
			case R.id.text_forget:
				intent = new Intent(this, A03_FindPassPhoneActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_reqcode:
				if ("".equals(edit_phone_signup.getText().toString()))
					errorMsg("请输入手机号");
				else
					loginModel.verificationPhone(AppConst.info_from,edit_phone_signup.getText().toString());

				break;
			case R.id.button_login:
				login();
				break;
			case R.id.button_login1:
				String reqCode = edit_recode.getText().toString();
				if (TextUtils.isEmpty(strPhone)){
					errorMsg("请输入手机号");
				}else if (TextUtils.isEmpty(strCode)){
					errorMsg("请输入验证码");
				}else if (seekBar.getProgress() != seekBar.getMax()){
					errorMsg("请完成验证");
				}else {
					intent = new Intent(this, A02_SignupActivity.class);
					intent.putExtra("phone", strPhone);
					intent.putExtra("verificationCode", strCode);
					intent.putExtra("reqCode", reqCode);
					startActivityForResult(intent,REQUEST_SIGNUP);
					strPhone = "";
					strCode = "";
					edit_phone_signup.setText("");
					edit_code.setText("");
					edit_recode.setText("");
					seekBar.setProgress(0);
					seekBar.setEnabled(true);
				}
				break;

		}
	}

	String strPhone = "", strPass;
	String strCode = "";

	private void getCode() {
		strPhone = edit_phone_signup.getText().toString();
		if (!AppUtils.isMobileNum(strPhone))
		{
			errorMsg("请确认手机号码~");
		}
		else
		{
//			commonModel.reqCode(strPhone, 0);
			SMSSDK.getVerificationCode("86",strPhone);
		}
	}

     void login()
	    {
			strPhone = edit_phone_signin.getText().toString();
			strPass = edit_password.getText().toString();

			if (!AppUtils.isMobileNum(strPhone))
				errorMsg("请确认手机号");
			else if (TextUtils.isEmpty(strPass) || strPass.length() < 6 || strPass.length() > 17)
				errorMsg("请输入6—17位密码");
			else
				loginModel.signin(strPhone, strPass);
	    }

	boolean isLogined = false;

@Override
public void finish() {
    if (isLogined) {
        Intent intent = new Intent();
        intent.putExtra("login", true);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    } else {
        gotoHome();
    }
}

void loginSucc()
{
	SharedPreferences.Editor editor = getSharedPreferences("userInfo", 0).edit();
	if (trd_platformType == 0){  // 微信
		editor.putString("login_type", "0");
		editor.commit();
		UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, umdelAuthListener);
	}else if (trd_platformType == 1){  // QQ
		editor.putString("login_type", "1");
		editor.commit();
		UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, umdelAuthListener);
	}else {
		editor.putString("login_type", "2");
		editor.commit();
	}
    isLogined = true;
    finish();
}

	UMAuthListener umdelAuthListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {

		}

		@Override
		public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

		}

		@Override
		public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

		}

		@Override
		public void onCancel(SHARE_MEDIA share_media, int i) {

		}
	};

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

@Override
public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
        throws JSONException {
    if (url.endsWith(ApiInterface.USER_REQCODE))
    {
        if (commonModel.lastStatus.error_code == 200) {
            startCount();
            errorMsg("验证码发送成功。请查收.");
        }
//        else if (commonModel.lastStatus.error_code == 6) {
//            gotoRegister();
//        }
        else {
            errorMsg(commonModel.lastStatus.error_desc);
        }
    }
    else if (url.contains(ApiInterface.USER_SIGNIN))
    {
        if (loginModel.lastStatus.error_code == 200) {
			loginSucc();

		}
        else
            errorMsg(loginModel.lastStatus.error_desc);
    }
    else if (url.endsWith(ApiInterface.USER_SIGNIN_3RD)) {
        if (loginModel.lastStatus.error_code == 200) {
            loginSucc();
        } else if (loginModel.lastStatus.error_code == 2) {
            goto3rdRegister();
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
}


//MyDialog mDialog;
//
//void gotoRegister() {
//    mDialog = new MyDialog(this, "", "您的手机号尚未注册过，\n您是否去注册?");
//
//    mDialog.show();
//    mDialog.negative.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mDialog.dismiss();
//        }
//    });
//    mDialog.positive.setOnClickListener(new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            mDialog.dismiss();
//            Intent intent = new Intent(A01_SigninActivity.this, A02_SignupActivity.class);
//            intent.putExtra("phone", strPhone);
//            startActivityForResult(intent, REQUEST_LOGIN);
//        }
//    });
//}

void goto3rdRegister() {
    Intent intent = new Intent(A01_SigninActivity.this, A02_ThirdSignupActivity.class);
    try {
        intent.putExtra("request", request.toJson().toString());
        intent.putExtra("phone", strPhone);
        startActivityForResult(intent, REQUEST_LOGIN);
    } catch (JSONException e) {
        e.printStackTrace();
    }
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    if(data != null)
    {
        if (requestCode == REQUEST_LOGIN) {
            loginSucc();
        }else if (requestCode == REQUEST_SIGNUP){
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			img_line_2.setVisibility(View.GONE);
			signin.setVisibility(View.VISIBLE);
			signup.setVisibility(View.GONE);
		}
    }
}

    void thirdLogin(SHARE_MEDIA platform) {
   		UMShareAPI.get(this).getPlatformInfo(this, platform, infoListener);
	}

	UMAuthListener infoListener = new UMAuthListener() {
		@Override
		public void onStart(SHARE_MEDIA share_media) {

		}

		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			if (data!=null){
				procThirdLogin(platform, data);
			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			errorMsg("登录失败:" + t.getMessage());
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			errorMsg("登录取消");
		}
	};

	int  trd_platformType = -1;
	String trd_openid;
	String trd_headimgurl;
	String trd_nickname;

	void procThirdLogin(SHARE_MEDIA platform, Map<String, String> data) {
		if (platform == SHARE_MEDIA.WEIXIN) {
			trd_platformType = 0;
			trd_openid = data.get("openid");
			trd_headimgurl = data.get("profile_image_url");
			trd_nickname = data.get("screen_name");
		} else if (platform == SHARE_MEDIA.QQ) {
			trd_openid = data.get("uid");
			trd_headimgurl = data.get("profile_image_url");
			trd_nickname = data.get("screen_name");
			trd_platformType = 1;
		} else {
			trd_openid = data.get("uid");
			trd_headimgurl = data.get("profile_image_url");
			trd_nickname = data.get("screen_name");
			trd_platformType = 2;
		}

		request.infrom = AppConst.info_from;
		request.out_account_type = trd_platformType;
		request.out_account = trd_openid;
		request.nickname = trd_nickname;
		request.headimg = trd_headimgurl;
		request.position = AppUtils.getFullAddr(this).length() > 2 ? AppUtils.getFullAddr(this).substring(2):AppUtils.getFullAddr(this);
		request.channelid = AppUtils.getChannelId(this);

		loginModel.signin3rd(request);
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		edit_phone_signup.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_phone_signup.getWindowToken(), 0);
	}

	/**
	 * seekBar进度变化时回调
	 *
	 * @param seekBar
	 * @param progress
	 * @param fromUser
	 */
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
//			tv.setText("完成验证");
		} else {
			tv.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * seekBar开始触摸时回调
	 *
	 * @param seekBar
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * seekBar停止触摸时回调
	 *
	 * @param seekBar
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBar.getProgress() != seekBar.getMax()) {
			seekBar.setProgress(0);
			tv.setVisibility(View.VISIBLE);
			tv.setTextColor(getResources().getColor(R.color.text_grey));
			tv.setText("验证请按住滑块，拖动到最右边");
		}
	}
}