package com.dmy.farming.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ReportActivity extends BaseActivity implements View.OnClickListener,BusinessResponse {

	ImageView img_back;
	DictionaryModel dictionaryModel;
	TextView text_reason1,text_reason2,text_reason3,text_reason4,text_reason5,text_reason6;
	CheckBox checkbox1,checkbox2,checkbox3,checkbox4,checkbox5,checkbox6;
	EditText editText;
	Button btn_submit;
	String id = "";
	String from = "";
	String iscomment = "";
	String about_user = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);

		initView();

		id = getIntent().getStringExtra("id");
		from = getIntent().getStringExtra("from");
		iscomment = getIntent().getStringExtra("iscomment");
		about_user = getIntent().getStringExtra("about_user");

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);


	}

	private void initView() {
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		text_reason1 = (TextView)findViewById(R.id.text_reason1);
		text_reason2 = (TextView)findViewById(R.id.text_reason2);
		text_reason3 = (TextView)findViewById(R.id.text_reason3);
		text_reason4 = (TextView)findViewById(R.id.text_reason4);
		text_reason5 = (TextView)findViewById(R.id.text_reason5);
		text_reason6 = (TextView)findViewById(R.id.text_reason6);

		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
		checkbox2 = (CheckBox) findViewById(R.id.checkbox2);
		checkbox3 = (CheckBox) findViewById(R.id.checkbox3);
		checkbox4 = (CheckBox) findViewById(R.id.checkbox4);
		checkbox5 = (CheckBox) findViewById(R.id.checkbox5);
		checkbox6 = (CheckBox) findViewById(R.id.checkbox6);

		editText = (EditText) findViewById(R.id.edit_reason);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.btn_submit:
				submit();
				break;
		}
	}

	private void submit() {
		String report_content = "";
		if (checkbox1.isChecked()){
			report_content += text_reason1.getText().toString();
		}
		if (checkbox2.isChecked()){
			report_content += "," + text_reason2.getText().toString();
		}
		if (checkbox3.isChecked()){
			report_content += "," + text_reason3.getText().toString();
		}
		if (checkbox4.isChecked()){
			report_content += "," + text_reason4.getText().toString();
		}
		if (checkbox5.isChecked()){
			report_content += "," + text_reason5.getText().toString();
		}
		if (checkbox6.isChecked()){
			report_content += "," + text_reason6.getText().toString();
		}
		if (!checkbox1.isChecked() && !checkbox2.isChecked() && !checkbox3.isChecked() && !checkbox4.isChecked() && !checkbox5.isChecked() && !checkbox6.isChecked())
			errorMsg("至少选择一项举报理由");
		else {
			String content = editText.getText().toString();
			dictionaryModel.report(AppConst.info_from, id, report_content, from, report_content, iscomment, content, SESSION.getInstance().sid, about_user);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.REPORT)){
			if (dictionaryModel.lastStatus.error_code == 200)
			{
				errorMsg("举报成功");
				finish();
			}else
				errorMsg(dictionaryModel.lastStatus.error_desc);

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
	}
}


