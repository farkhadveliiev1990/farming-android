package com.dmy.farming.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.view.addressselector.BottomDialog;
import com.dmy.farming.view.addressselector.OnAddressSelectedListener;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PriceFilterActivity extends BaseActivity implements View.OnClickListener, OnAddressSelectedListener {

	TextView text_crop,text_position,textid;

	BottomDialog positionDialog;
	String province,city,county;

	DictionaryModel dictionaryModel;
	public ArrayList<DICTIONARY> crop_type = new ArrayList<DICTIONARY>();
	final static int REQUEST_CROP = 1;
	String cropcode;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_filter);

		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_crop).setOnClickListener(this);
		findViewById(R.id.layout_position).setOnClickListener(this);
		findViewById(R.id.btn_sure).setOnClickListener(this);

		text_crop = (TextView) findViewById(R.id.text_crop);
		text_position = (TextView) findViewById(R.id.text_position);
		textid = (TextView) findViewById(R.id.id);

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		dictionaryModel.cropType(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.layout_crop:
			/*	ArrayList<String> datalist = new ArrayList<String>();
				ArrayList<String> datacode = new ArrayList<String>();
				for (DICTIONARY crop:crop_type){
					datalist.add(crop.name);
					datacode.add(crop.code);
				}
				showDialog(v,"选择作物",datalist,datacode);*/
				startActivityForResult(new Intent(PriceFilterActivity.this,C04_ChooseCropActivity.class),REQUEST_CROP);
				break;
			case R.id.layout_position:
				positionDialog = new BottomDialog(PriceFilterActivity.this);
				positionDialog.setOnAddressSelectedListener(PriceFilterActivity.this);
				positionDialog.show();
				break;
			case R.id.btn_sure:
				String crop = text_crop.getText().toString();
				String position = text_position.getText().toString();
				String code = textid.getText().toString();
				if (TextUtils.isEmpty(cropcode))
					errorMsg("请选择作物");
				else if (TextUtils.isEmpty(position))
					errorMsg("请选择位置");
				else {
					Intent intent = new Intent(PriceFilterActivity.this, C01_MarketPriceActivity.class);
					intent.putExtra("crop", cropcode);
					intent.putExtra("province",province);
					intent.putExtra("city",city);
					intent.putExtra("district",county);
					setResult(RESULT_OK,intent);
					finish();
				}
				break;
		}
	}

	AlertDialog dialog;
	private void showDialog(final View v, String title, final ArrayList<String> datalist, final ArrayList<String> datacode) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PriceFilterActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(PriceFilterActivity.this).inflate(R.layout.dialog,null);
		builder.setView(view);
		((TextView)view.findViewById(R.id.text_title)).setText(title);
		ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(PriceFilterActivity.this,datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				text_crop.setText(datalist.get(position));
				textid.setText(datacode.get(position));
				dialog.dismiss();
			}
		});

		dialog = builder.create();

		Window dialogWindow = dialog.getWindow();

		dialog.show();

		Display d = getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.gravity = Gravity.CENTER;
		p.width = (int)(d.getWidth()*0.8);

		dialogWindow.setAttributes(p);
	}

	@Override
	public void onAddressSelected(Province province, City city, County county) {
		this.province = (province == null ? "" : province.name);
		this.city = (city == null ? "" : city.name);
		this.county = (county == null ? "" : county.name);

		text_position.setText(this.province + this.city + this.county);

		if (positionDialog != null)
			positionDialog.dismiss();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null)
		{
			if (requestCode == REQUEST_CROP) {
				String crop = data.getStringExtra("crop");
				if (!TextUtils.isEmpty(crop)) {
					text_crop.setText(crop);
					cropcode = data.getStringExtra("crop_code");
				}
			}
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.CROPTYPE)){
			if (dictionaryModel.data.crop_type.size() > 0)
			{
				crop_type = dictionaryModel.data.crop_type;
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
	}
}
