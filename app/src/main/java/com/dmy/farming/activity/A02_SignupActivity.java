package com.dmy.farming.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.view.addressselector.BottomDialog;
import com.dmy.farming.view.addressselector.OnAddressSelectedListener;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.external.androidquery.callback.AjaxStatus;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import com.dmy.farming.R;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.api.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class A02_SignupActivity extends BaseActivity implements OnClickListener, BusinessResponse, OnAddressSelectedListener {

	LoginModel loginModel;
	String strPhone = "",strCode = "",strPass = "",strPass2 = "",reqCode = "";
	String province = "";
	String city = "";
	String district = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a02_signup);

		strPhone = getIntent().getStringExtra("phone");
		strCode = getIntent().getStringExtra("verificationCode");
		reqCode = getIntent().getStringExtra("reqCode");

		province = AppUtils.getCurrProvince(this);
		city = AppUtils.getCurrCity(this) +"市";
		district = AppUtils.getCurrDistrict(this) +"区";

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

	EditText edit_pass, edit_pass2;
	TextView position;
    BottomDialog dialog;
	ImageView showPwdImageView;
	boolean isChecked = false;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_pass = (EditText) findViewById(R.id.edit_pass);
		edit_pass2 = (EditText) findViewById(R.id.edit_pass2);

		View button_ok = findViewById(R.id.button_ok);
		button_ok.setOnClickListener(this);

		showPwdImageView = (ImageView) findViewById(R.id.showPwdImageView);
		showPwdImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isChecked = !isChecked;
				if (isChecked){
					showPwdImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_display));
					edit_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}else {
					showPwdImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
					edit_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});

        position = (TextView) findViewById(R.id.edit_position);
		if (AppUtils.getFullAddr(this).length() > 2)
			position.setText(AppUtils.getFullAddr(this).substring(2));

		assert position != null;
        position.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                dialog = new BottomDialog(A02_SignupActivity.this);
				dialog.setOnAddressSelectedListener(A02_SignupActivity.this);
				dialog.show();
			}
		});

		View text_serviceAgreement = findViewById(R.id.text_serviceAgreement);
		text_serviceAgreement.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.button_ok:
				send();
				break;
			case R.id.text_serviceAgreement:
				intent = new Intent(this, A04_ArticleActivity.class);
				startActivity(intent);
				break;
		}
	}

	void send()
	{
		strPass = edit_pass.getText().toString();
		strPass2 = edit_pass2.getText().toString();
		if (TextUtils.isEmpty(strPass) || strPass.length() < 6 || strPass.length() > 17)
			errorMsg("请输入6—17位密码");
		else if (TextUtils.isEmpty(strPass2) || strPass2.length() < 6 || strPass2.length() > 17)
			errorMsg("请确认输入6—17位密码");
		else if (!strPass2.equals(strPass))
			errorMsg("两次输入的密码不一致");
		else if (TextUtils.isEmpty(position.getText().toString()))
			errorMsg("请选择地址");
		else
		{
			loginModel.signup(strPhone, strPass,reqCode,province, city, district);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.USER_SIGNUP))
		{
			if (loginModel.lastStatus.error_code==200)
			{
				errorMsg("注册成功");
				Intent intent = new Intent();
				intent.putExtra("signup","注册成功");
				setResult(Activity.RESULT_OK,intent);
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
		edit_pass2.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_pass2.getWindowToken(), 0);
	}

	@Override
	public void onAddressSelected(Province p, City c, County d) {
		province = (p == null ? "" : p.name);
		city = (c == null ? "" : c.name);
		district = (d == null ? "" : d.name);

		if (!TextUtils.isEmpty(province + city + district))
			position.setText(province + city + district);

        if (dialog != null)
            dialog.dismiss();
	}
}
