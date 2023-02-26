package com.dmy.farming.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.pickerview.popwindow.DatePickerPopWin;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCropActivity extends BaseActivity implements View.OnClickListener {

	private static final int REQUEST_CROP = 1;
	private static final int REQUEST_CROP_NAME = 2;
	TextView text_crop_category,text_crop_name,text_position,text_time;
	EditText edit_area;
	String crop_code = "";
	String sub_crop_code = "";
	DictionaryModel dictionaryModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_crop);

		initView();

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
	}

	private void initView() {
		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_crop_category).setOnClickListener(this);
		findViewById(R.id.layout_crop_name).setOnClickListener(this);
		findViewById(R.id.btn_submit).setOnClickListener(this);

		text_crop_category = (TextView) findViewById(R.id.text_crop_category);
		text_crop_name = (TextView) findViewById(R.id.text_crop_name);
		text_time = (TextView) findViewById(R.id.text_time);
		text_time.setOnClickListener(this);
		text_position = (TextView) findViewById(R.id.text_position);
		text_position.setText(AppUtils.getFullAddr(this).length() > 2 ? AppUtils.getFullAddr(this).substring(2):AppUtils.getFullAddr(this));
		edit_area = (EditText) findViewById(R.id.edit_area);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_crop_category:
				intent = new Intent(this,ChooseCropTypeActivity.class);
				startActivityForResult(intent,REQUEST_CROP);
				break;
			case R.id.layout_crop_name:
				if (TextUtils.isEmpty(crop_code))
					errorMsg("请选择作物类别");
				else {
					intent = new Intent(this, ChooseCropNameActivity.class);
					intent.putExtra("crop_code",crop_code);
					startActivityForResult(intent, REQUEST_CROP_NAME);
				}
				break;
			case R.id.text_time:
				SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
				String date = formater.format(new Date());
				DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(AddCropActivity.this, new DatePickerPopWin.OnDatePickedListener() {
					@Override
					public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
						text_time.setText(dateDesc);
					}
				}).textConfirm("确认") //text of confirm button
						.textCancel("取消") //text of cancel button
						.btnTextSize(16) // button text size
						.viewTextSize(20) // pick view text size
						.colorCancel(Color.parseColor("#999999")) //color of cancel button
						.colorConfirm(Color.parseColor("#009900"))//color of confirm button
						.minYear(1990) //min year in loop
						.maxYear(2550) // max year in loop
						.dateChose(date) // date chose when init popwindow
						.build();
				pickerPopWin.showPopWin(AddCropActivity.this);
				break;
			case R.id.btn_submit:
				submit();
				break;
		}
	}

	private void submit() {

		String crop_category = text_crop_category.getText().toString();
		String crop_name = text_crop_name.getText().toString();
		String area = edit_area.getText().toString();
		String time = text_time.getText().toString();
		String position = text_position.getText().toString();

		if (TextUtils.isEmpty(crop_category))
			errorMsg("请选择作物类别");
		else if (TextUtils.isEmpty(crop_name))
			errorMsg("请选择作物名称");
		else if (TextUtils.isEmpty(area))
			errorMsg("请输入种植面积");
		else if (TextUtils.isEmpty(time))
			errorMsg("请选择种植时间");
		else if (TextUtils.isEmpty(position))
			errorMsg("请选择地区");
		else {
			dictionaryModel.addCrop(AppConst.info_from,sub_crop_code,crop_code,area,time,position);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CROP){
				String crop = data.getStringExtra("crop");
				crop_code = data.getStringExtra("crop_code");
				if (!"".equals(crop))
					text_crop_category.setText(crop);
			}else if (requestCode == REQUEST_CROP_NAME){
				String crop = data.getStringExtra("crop");
				sub_crop_code = data.getStringExtra("crop_code");
				if (!"".equals(crop))
					text_crop_name.setText(crop);
			}
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.ADDCROP))
		{
			if (dictionaryModel.lastStatus.error_code == 200)
			{
				errorMsg("添加成功");
				finish();
			}
			else
				errorMsg(dictionaryModel.lastStatus.error_desc);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
	}

}
