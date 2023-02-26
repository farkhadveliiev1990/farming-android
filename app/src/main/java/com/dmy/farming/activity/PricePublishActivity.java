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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.MarkerPriceRequest;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.PublishModel;
import com.dmy.farming.view.addressselector.BottomDialog;
import com.dmy.farming.view.addressselector.OnAddressSelectedListener;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PricePublishActivity extends BaseActivity implements View.OnClickListener, OnAddressSelectedListener {

	TextView text_crop,text_position,text_unit,crop_code;
	EditText edit_price,edit_price1,edit_price2;
	View layout_price1,layout_price2;
	BottomDialog positionDialog;
	String province,city,county;
	DictionaryModel dictionaryModel;
	public ArrayList<DICTIONARY> crop_type = new ArrayList<DICTIONARY>();
	PublishModel publishModel;
	boolean priceSwitch = false;
	MarkerPriceRequest markerPriceRequest;
	String priceunit;
	String cropcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_publish);

		findViewById(R.id.img_back).setOnClickListener(this);
		findViewById(R.id.layout_crop).setOnClickListener(this);
		findViewById(R.id.layout_position).setOnClickListener(this);
		findViewById(R.id.btn_publish).setOnClickListener(this);
		findViewById(R.id.img_switch).setOnClickListener(this);

		crop_code = (TextView) findViewById(R.id.id);
		text_crop = (TextView) findViewById(R.id.text_crop);
		text_position = (TextView) findViewById(R.id.text_position);
		text_unit = (TextView) findViewById(R.id.text_unit);
		text_unit.setOnClickListener(this);


		edit_price = (EditText) findViewById(R.id.edit_price);
		edit_price1 = (EditText) findViewById(R.id.edit_price1);
		edit_price2 = (EditText) findViewById(R.id.edit_price2);
		layout_price1 = findViewById(R.id.layout_price1);
		layout_price2 = findViewById(R.id.layout_price2);


		text_position.setText(AppUtils.getFullAddr(this).length() > 2 ? AppUtils.getFullAddr(this).substring(2):AppUtils.getFullAddr(this));
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		publishModel = new PublishModel(this);
		publishModel.addResponseListener(this);
		markerPriceRequest = new MarkerPriceRequest();
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
//				ArrayList<String> datalist = new ArrayList<String>();
//				ArrayList<String> codelist = new ArrayList<String>();
//				for (DICTIONARY crop:crop_type){
//					datalist.add(crop.name);
//					codelist.add(crop.code);
//				}
//				showDialog(v,"选择作物",datalist,codelist);

				startActivityForResult(new Intent(PricePublishActivity.this,C04_ChooseCropActivity.class),REQUEST_CROP);

				break;
			case R.id.layout_position:
				positionDialog = new BottomDialog(PricePublishActivity.this);
				positionDialog.setOnAddressSelectedListener(PricePublishActivity.this);
				positionDialog.show();
				break;
			case R.id.btn_publish:
				String crop = crop_code.getText().toString();
				String pricelow;
				String pricehigh;
				if (layout_price1.getVisibility() == View.VISIBLE) {
					pricelow = "";
					pricehigh = edit_price.getText().toString();
					if (pricehigh.contains(".")){
						if (pricehigh.substring(0,pricehigh.indexOf(".")).length() > 5) {
							errorMsg("输入的价格小数点前最多为5位整数");
							return;
						}
						else if (pricehigh.substring(pricehigh.indexOf(".")+1,pricehigh.length()).length() > 2) {
							errorMsg("输入的价格小数点后最多为2位整数");
							return;
						}

					} else {
						if (pricehigh.length() > 5) {
							errorMsg("输入的价格最多为5位整数");
							return;
						}
					}
				}else {
					pricelow = edit_price1.getText().toString();
					pricehigh = edit_price2.getText().toString();
				}
				String position = text_position.getText().toString();
				if (TextUtils.isEmpty(crop))
					errorMsg("请选择作物");
				else if (TextUtils.isEmpty(pricehigh))
					errorMsg("请输入价格");
				else if (TextUtils.isEmpty(position))
					errorMsg("请选择位置");
				else {
					if (province == null || city == null || county == null){
						province = AppUtils.getCurrProvince(this);
						city = AppUtils.getCurrCity(this);
						county = AppUtils.getCurrDistrict(this);
					}
					priceunit = text_unit.getText().toString();

					markerPriceRequest.crop_type = cropcode;
					markerPriceRequest.provience = province;
					markerPriceRequest.city = city + "市";
					if (county.contains("县"))
						markerPriceRequest.district = county;
					else
						markerPriceRequest.district = county + "区";
					markerPriceRequest.price = pricelow;
					markerPriceRequest.price1 = pricehigh;
					markerPriceRequest.info_from = AppConst.info_from;
					markerPriceRequest.user_phone = SESSION.getInstance().sid;
					markerPriceRequest.priceunit = priceunit;
					markerPriceRequest.pricetype = "1";
                    publishModel.publishPrice(markerPriceRequest);
				}
				break;
			case R.id.text_unit:
				ArrayList<String> datas = new ArrayList<String>();
				ArrayList<String> datacode = new ArrayList<String>();
				datas.add("元/斤");
				datas.add("元/公斤");
				datacode.add("YUAN/JIN");
				datacode.add("YUAN/GONGJIN");
				showDialog(v,"选择单位",datas,datacode);
				break;
			case R.id.img_switch:
				priceSwitch = !priceSwitch;
                if (priceSwitch){
					layout_price1.setVisibility(View.GONE);
					layout_price2.setVisibility(View.VISIBLE);
				}else {
					layout_price1.setVisibility(View.VISIBLE);
					layout_price2.setVisibility(View.GONE);
				}
				break;

		}
	}

	AlertDialog dialog;
	private void showDialog(final View v, String title, final ArrayList<String> datalist,final ArrayList<String> codelist) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PricePublishActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(PricePublishActivity.this).inflate(R.layout.dialog,null);
		builder.setView(view);
		((TextView)view.findViewById(R.id.text_title)).setText(title);
		ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(PricePublishActivity.this,datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (v == text_unit) {
					text_unit.setText(datalist.get(position));
				}
				else {
					text_crop.setText(datalist.get(position));
					cropcode =codelist.get(position);
				}

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

	final static int REQUEST_CROP = 1;

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
		}else if (url.contains(ApiInterface.PUBLISHPRICE)){
			if (publishModel.lastStatus.error_code == 200)
			{
				errorMsg("发布成功");
				setResult(RESULT_OK);
				finish();
			}
			else
				errorMsg(publishModel.lastStatus.error_desc);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
		publishModel.removeResponseListener(this);
	}
}
