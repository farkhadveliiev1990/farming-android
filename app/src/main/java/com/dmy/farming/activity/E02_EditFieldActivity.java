package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
//import com.beidou.wukong.api.groupReqRequest;
//import com.beidou.wukong.api.model.GroupModel;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.utils.IDCard;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class E02_EditFieldActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	int type;
	String value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e02_edit_field);

		type = getIntent().getIntExtra("type", 0);
		value = getIntent().getStringExtra("value");
		// target_id <---- groupid;

		initView();
	}

	EditText edit_value;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		edit_value = (EditText) findViewById(R.id.edit_value);
		edit_value.setText(value);

		TextView text_title = (TextView) findViewById(R.id.text_title);
		TextView text_hint = (TextView) findViewById(R.id.text_hint);
		if (type == E01_InfoActivity.WAIT_EDIT_NICK) {
			text_title.setText("昵称更改");
			text_hint.setText("起个酷炫的昵称可以吸引更多人哦！");
		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_REALNAME) {
//			text_title.setText("真实姓名更改");
//			text_hint.setText("请您输入真实的名字！");
//		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_IDCARD) {
//			text_title.setText("身份证号更改");
//			text_hint.setText("请您输入真实的身份证号！");
//		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_ALIPAY) {
//			text_title.setText("支付宝账号更改");
//			text_hint.setText("为了提现，需要设置支付宝账号");
//			InputFilter[] filters = {new InputFilter.LengthFilter(30)};
//			edit_value.setFilters(filters);
//		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_TAG) {
//			edit_value.setSingleLine(false);
//			edit_value.setMaxLines(2);
//			InputFilter[] filters = {new InputFilter.LengthFilter(36)};
//			edit_value.setFilters(filters);
//			text_title.setText("签名更改");
//			text_hint.setText("用一句话来简单介绍自己吧~");
//		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_JOB) {
//			text_title.setText("职业更改");
//			text_hint.setText("请输入您的职业, 以便找到志同道合的人~");
//		}
//		else if (type == E01_InfoActivity.WAIT_EDIT_GROUP_DESC) {
//			edit_value.setSingleLine(false);
//			edit_value.setMaxLines(2);
//			InputFilter[] filters = {new InputFilter.LengthFilter(36)};
//			edit_value.setFilters(filters);
//			text_title.setText("简介更改");
//			text_hint.setText("请输入群简介");
//		}

		View button_save = findViewById(R.id.button_save);
		button_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.button_save:
				clickOK();
				break;
		}
	}

	//
//	GroupModel groupModel;
//	groupReqRequest request;
	//

	String strValue = "";
	void clickOK()
	{
		strValue = edit_value.getText().toString();
		if (value.equals(strValue))
			finish();
		else
		{
			if (!TextUtils.isEmpty(strValue))
			{
				if (type == E01_InfoActivity.WAIT_EDIT_REALNAME) {
					if (!AppUtils.isChineseName(strValue)){
						errorMsg("请确认真实名字");
						return;
					}
				} else if (type == E01_InfoActivity.WAIT_EDIT_IDCARD) {
					try {
						String errorString = IDCard.IDCardValidate(strValue);
						if (!TextUtils.isEmpty(errorString)) {
							errorMsg(errorString);
							return;
						}
					} catch (ParseException e) {
						e.printStackTrace();
						errorMsg("请确认身份证号码");
						return;
					}
				}
				successClose();
			}else
				errorMsg("输入内容不能为空！");

			/*if (type == E01_InfoActivity.WAIT_EDIT_GROUP_DESC) {
				if (groupModel == null) {
					groupModel = new GroupModel(this, getIntent().getLongExtra("target_id", 0L));
					groupModel.addResponseListener(this);
					request = new groupReqRequest();
					request.reqtype = 6;
					request.group_id = groupModel.group_id;
				}
				request.comment = strValue;
				groupModel.req(request);
			} else {
				successClose();
			}*/
		}
	}

	void successClose() {
		Intent intent = new Intent();
		intent.putExtra("value", strValue);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
//		if (url.endsWith(ApiInterface.GROUP_REQ)) {
//			if (groupModel.lastStatus.succeed == 1) {
//				errorMsg("提交成功");
//				successClose();
//			} else {
//				errorMsg(groupModel.lastStatus.error_desc);
//			}
//		}
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		edit_value.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_value.getWindowToken(), 0);
	}
}
