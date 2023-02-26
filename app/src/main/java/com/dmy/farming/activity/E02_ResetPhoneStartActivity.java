package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.USER;
import com.external.activeandroid.query.Select;

import org.bee.activity.BaseActivity;

public class E02_ResetPhoneStartActivity extends BaseActivity implements OnClickListener {

	USER userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e02_reset_phone_start);

		userInfo = userInfo(SESSION.getInstance().uid);
		initView();
	}

	TextView text_phone;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		text_phone = (TextView) findViewById(R.id.text_phone);
		text_phone.setText(AppUtils.getPhoneWithStar(userInfo.username));

		View button_start = findViewById(R.id.button_start);
		button_start.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.button_start:
//				intent = new Intent(this, E03_ResetPhoneActivity.class);
//				startActivityForResult(intent, WAIT_EDIT_PHONE);
				break;
		}
	}

	public final static int WAIT_EDIT_PHONE = 5;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == WAIT_EDIT_PHONE) {
				Intent intent = new Intent();
				intent.putExtra("value", data.getStringExtra("value"));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	}

	public static USER userInfo(String uid) {
		return new Select().from(USER.class).where("USER_id = ?", uid).executeSingle();
	}
}
