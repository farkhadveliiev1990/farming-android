package com.dmy.farming.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.api.signin3rdRequest;
import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.bee.view.MyDialog;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class E01_InvitationActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_invitation);

		initView();

	}

	XListView list_black;
	View null_pager;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		null_pager = findViewById(R.id.null_pager);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
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


}