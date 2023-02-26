package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.UserInfoModel;
import com.umeng.socialize.UMShareAPI;

import org.bee.activity.BaseActivity;
import org.bee.view.MyDialog;
import org.bee.view.ToastView;

public class E01_SettingActivity extends BaseActivity implements OnClickListener {

	View layout_menu_exit;
	UserInfoModel userInfoModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_setting);

		initView();

		userInfoModel = new UserInfoModel(this);
		userInfoModel.addResponseListener(this);

	}

	TextView text_value_1;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		View layout_menu_0 = findViewById(R.id.layout_menu_0);
		layout_menu_0.setOnClickListener(this);
		View layout_menu_1 = findViewById(R.id.layout_menu_1);
		layout_menu_1.setOnClickListener(this);
		View layout_menu_2 = findViewById(R.id.layout_menu_2);
		layout_menu_2.setOnClickListener(this);
		View layout_menu_3 = findViewById(R.id.layout_menu_3);
		layout_menu_3.setOnClickListener(this);

		layout_menu_exit = findViewById(R.id.layout_menu_exit);
		layout_menu_exit.setOnClickListener(this);

		text_value_1 = (TextView) findViewById(R.id.text_value_1);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_menu_0:
				intent = new Intent(this, E03_ChangePassActivity.class);
				startActivityForResult(intent, REQUEST_MODIFYPASS);
				break;
			case R.id.layout_menu_1:
				dispDlg();
				break;
			case R.id.layout_menu_2:

				break;
			case R.id.layout_menu_exit:
				logoutDlg();
				break;
		}
	}

	MyDialog mDialog1;
	void dispDlg()
	{
		mDialog1 = new MyDialog(this, "", "确定清除缓存？");
		mDialog1.show();
		mDialog1.positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog1.dismiss();
				clearCache();
			}
		});
		mDialog1.negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog1.dismiss();
			}
		});
	}

	private Handler handler;
	private Runnable runnable;

	private void clearCache()
	{
		handler = new Handler();
		runnable = new Runnable () {
			public void run () {

				DataCleanManager.cleanApplicationData(E01_SettingActivity.this);

				ToastView.cancel();
				ToastView toast1 = new ToastView(E01_SettingActivity.this,"清理完毕！");
				toast1.setGravity(Gravity.CENTER, 0, 0);
				toast1.show();

				handler.removeCallbacks(this);
				refresh();
				return;
			}
		};

		handler.postDelayed(runnable, 5);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	void refresh()
	{
		String size = DataCleanManager.getCacheSize(this);
		if ("0B".equals(size))
		{
			text_value_1.setText("已清空缓存");
		}
		else
		{
			text_value_1.setText(size);
		}
	}

	MyDialog mDialog;
	void logoutDlg() {
		mDialog = new MyDialog(E01_SettingActivity.this, "", "确定退出该账户?");
		mDialog.show();
		mDialog.negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				userInfoModel.userLogout();

				SESSION.getInstance().updateValue(E01_SettingActivity.this, "", "", "", "",-1,"");
				Intent intent = new Intent(E01_SettingActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}
		});
	}

	private final static int REQUEST_MODIFYPASS = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null) {
			if (requestCode == REQUEST_MODIFYPASS) {
				errorMsg("修改密码成功");
			}
		}
	}

}
