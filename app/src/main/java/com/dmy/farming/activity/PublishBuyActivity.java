package com.dmy.farming.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.PublishModel;
import com.dmy.farming.api.publishBuyRequest;
import com.dmy.farming.api.publishQuestionRequest;
import com.dmy.farming.photopicker.PhotoPickerActivity;
import com.dmy.farming.photopicker.PhotoPreviewActivity;
import com.dmy.farming.photopicker.SelectModel;
import com.dmy.farming.photopicker.intent.PhotoPickerIntent;
import com.dmy.farming.photopicker.intent.PhotoPreviewIntent;
import com.dmy.farming.view.addressselector.BottomDialog;
import com.dmy.farming.view.addressselector.OnAddressSelectedListener;
import com.dmy.farming.view.addressselector.model.City;
import com.dmy.farming.view.addressselector.model.County;
import com.dmy.farming.view.addressselector.model.Province;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PublishBuyActivity extends BaseActivity implements OnClickListener, BusinessResponse, OnAddressSelectedListener
{
	GridView gridView;
	GridAdapter gridAdapter;
	ArrayList<String> imagePaths = new ArrayList<>();
	private static final int REQUEST_CAMERA_CODE = 1;
	private static final int REQUEST_PREVIEW_CODE = 2;
	private static final int REQUEST_BUY_TYPE = 3;
	private static final int REQUEST_UNIT = 4;
	TextView text_subtitle,text_describe,text_type,text_price,text_contacts,text_phone,text_unit,text_position;
	PublishModel publishModel;
	BottomDialog positionDialog;
	String province,city,county;
	publishBuyRequest request;
	String publish_code;
	CommonModel commonModel;
	String imgurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.e01_public_sell);
		setContentView(R.layout.publish_buy);

		request = new publishBuyRequest();

		initView();

		publishModel = new PublishModel(this);
		publishModel.addResponseListener(this);
		commonModel = new CommonModel(this);
		commonModel.addResponseListener(this);
	}


	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		text_subtitle = (TextView) findViewById(R.id.text_subtitle);
		text_describe = (TextView) findViewById(R.id.text_describe);
		text_type = (TextView) findViewById(R.id.text_type);
		text_type.setOnClickListener(this);
		text_price = (TextView) findViewById(R.id.text_price);
		text_unit = (TextView) findViewById(R.id.text_unit);
		text_unit.setOnClickListener(this);
		text_contacts = (TextView) findViewById(R.id.text_contacts);
		text_phone = (TextView) findViewById(R.id.text_phone);
		text_phone.setText(SESSION.getInstance().sid);
		text_position = (TextView) findViewById(R.id.text_position);
		text_position.setText(AppUtils.getFullAddr(this).length() > 2 ? AppUtils.getFullAddr(this).substring(2):AppUtils.getFullAddr(this));
		text_position.setOnClickListener(this);

		findViewById(R.id.btn_submit).setOnClickListener(this);

		gridView = (GridView) findViewById(R.id.gridView);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String imgs = (String) parent.getItemAtPosition(position);
				if ("000000".equals(imgs) ){
					PhotoPickerIntent intent = new PhotoPickerIntent(PublishBuyActivity.this);
					intent.setSelectModel(SelectModel.MULTI);
					intent.setShowCarema(true); // 是否显示拍照
					intent.setMaxTotal(3); // 最多选择照片数量，默认为3
					intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
					startActivityForResult(intent, REQUEST_CAMERA_CODE);
				}else{
					PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishBuyActivity.this);
					intent.setCurrentItem(position);
					ArrayList<String> imgurls = new ArrayList<String>();
					for (String img:imagePaths){
						if (!"000000".equals(img))
							imgurls.add(img);
					}
					intent.setPhotoPaths(imgurls);
					startActivityForResult(intent, REQUEST_PREVIEW_CODE);
				}
			}
		});
		imagePaths.add("000000");
		gridAdapter = new GridAdapter(imagePaths);
		gridView.setAdapter(gridAdapter);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.btn_submit:
				List<String> imgurl = new ArrayList();
				for (String img: imagePaths) {
					if (!"000000".equals(img))
						imgurl.add(img);
				}
				if (imgurl.size() > 0) {
					commonModel.uploadMultiFile(imgurl);
				}else {
					submit();
				}
				break;
			case R.id.text_position:
				positionDialog = new BottomDialog(PublishBuyActivity.this);
				positionDialog.setOnAddressSelectedListener(PublishBuyActivity.this);
				positionDialog.show();
				break;
			case R.id.text_type:
//				ArrayList<String> datalist = new ArrayList<String>();
//				datalist.add("农资");
//				datalist.add("农机");
//				datalist.add("农产品");
//				showDialog("选择类别",datalist);

//				intent = new Intent(this,ChoosePublishTypeActivity.class);
				intent = new Intent(this,PublishTypeChooseActivity.class);
				intent.putExtra("type_code","BUY_TYPE");
				startActivityForResult(intent,REQUEST_BUY_TYPE);
				break;
			case R.id.text_unit:
				intent = new Intent(this,ChooseUnitActivity.class);
				startActivityForResult(intent,REQUEST_UNIT);
				break;
		}
	}

	private void submit() {
		String subtitle = text_subtitle.getText().toString();
		String describe = text_describe.getText().toString();
		String type = text_type.getText().toString();
		String price = text_price.getText().toString();
		String priceunit = text_unit.getText().toString();
		String contacts = text_contacts.getText().toString();
		String phone = text_phone.getText().toString();
		String address_details = text_position.getText().toString();
		if (province == null)
			province = AppUtils.getCurrProvince(this);
		if (city == null)
			city = AppUtils.getCurrCity(this);
		if (county == null)
			county = AppUtils.getCurrDistrict(this);

		if (TextUtils.isEmpty(subtitle))
			errorMsg("请输入标题");
		else if (subtitle.length() > 15)
			errorMsg("输入标题过长，最多15个字");
		else if (TextUtils.isEmpty(describe))
			errorMsg("请输入描述信息");
		else if (describe.length() > 60)
			errorMsg("输入描述信息过长，最多60个字");
		else if (TextUtils.isEmpty(type))
			errorMsg("请选择类型");
		else if (TextUtils.isEmpty(price))
			errorMsg("请输入价格");
		else if (TextUtils.isEmpty(contacts))
			errorMsg("请输入联系人");
		else if (TextUtils.isEmpty(phone))
			errorMsg("请输入联系电话");
		else if (!AppUtils.isMobileNum(phone)){
			errorMsg("请确认手机号码");
		}
		else {
			if (price.contains(".")){
				if (price.substring(0,price.indexOf(".")).length() > 5) {
					errorMsg("输入的价格小数点前最多为5位整数");
					return;
				}
				else if (price.substring(price.indexOf(".")+1,price.length()).length() > 2) {
					errorMsg("输入的价格小数点后最多为2位整数");
					return;
				}

			} else {
				if (price.length() > 5) {
					errorMsg("输入的价格最多为5位整数");
					return;
				}
			}
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = formater.format(new Date());
			request.info_from = AppConst.info_from;
			request.title = subtitle;
			request.type_code = publish_code;
			if (province.contains("省"))
				request.provience = province.replace("省","");
			else
				request.provience = province;
			if (city.contains("市"))
				request.city = city;
			else
				request.city = city + "市";
			if (county.contains("县") || county.contains("区"))
				request.district = county;
			else
				request.district = county + "区";
			request.content = describe;
			request.price = Double.parseDouble(price);
			request.unit = priceunit;
			request.link_name = contacts;
			request.link_phone = phone;
			request.lon = AppUtils.getCurrLon(this);
			request.lat = AppUtils.getCurrLat(this);
			request.address_details = address_details;
			request.img_url = imgurl;
			request.phone = SESSION.getInstance().sid;
			request.source = "1";
			publishModel.publishBuy(request);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	AlertDialog dialog;
	private void showDialog(String title, final ArrayList<String> datalist) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PublishBuyActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(PublishBuyActivity.this).inflate(R.layout.dialog,null);
		builder.setView(view);
		((TextView)view.findViewById(R.id.text_title)).setText(title);
		ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(PublishBuyActivity.this,datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				text_type.setText(datalist.get(position));
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK) {
			switch (requestCode) {
				// 选择照片
				case REQUEST_CAMERA_CODE:
					ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
					loadAdpater(list);
					break;
				// 预览
				case REQUEST_PREVIEW_CODE:
					ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
					loadAdpater(ListExtra);
					break;
			}
		}

		if (data != null) {
			if (requestCode == REQUEST_BUY_TYPE) {
				String question = data.getStringExtra("publish_type");
				if (!"".equals(question)) {
					text_type.setText(question);
					publish_code = data.getStringExtra("publish_code");
				}
			}else if (requestCode == REQUEST_UNIT){
				String unit = data.getStringExtra("unit");
				if (!"".equals(unit)) {
					text_unit.setText(unit);
				}
			}
		}

	}

	private void loadAdpater(ArrayList<String> paths){
		if (imagePaths!=null&& imagePaths.size()>0){
			imagePaths.clear();
		}
		if (paths.contains("000000")){
			paths.remove("000000");
		}
		paths.add("000000");
		imagePaths.addAll(paths);
		gridAdapter  = new GridAdapter(imagePaths);
		gridView.setAdapter(gridAdapter);
		try{
			JSONArray obj = new JSONArray(imagePaths);
			Log.e("--", obj.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
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
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.PUBLISHBUY))
		{
			if (publishModel.lastStatus.error_code == 200)
			{
				errorMsg("发布成功");
				setResult(RESULT_OK);
				finish();
			}
			else
				errorMsg(publishModel.lastStatus.error_desc);
		}else if (url.endsWith(ApiInterface.UPLOAD_FILE)) {
			if (commonModel.lastStatus.error_code == 200) {
				imgurl = commonModel.fullPath;
				submit();
			} else {
				errorMsg(commonModel.lastStatus.error_desc);
			}
		}
	}

	private class GridAdapter extends BaseAdapter {
		private ArrayList<String> listUrls;
		private LayoutInflater inflater;

		public GridAdapter(ArrayList<String> listUrls) {
			this.listUrls = listUrls;
			if(listUrls.size() == 4){
				listUrls.remove(listUrls.size()-1);
			}
			inflater = LayoutInflater.from(PublishBuyActivity.this);
		}

		public int getCount(){
			return  listUrls.size();
		}
		@Override
		public String getItem(int position) {
			return listUrls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_image, parent,false);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}

			final String path=listUrls.get(position);
			if (path.equals("000000")){
				holder.image.setImageResource(R.drawable.q_icon_add);
			}else {
				Glide.with(PublishBuyActivity.this)
						.load(path)
						.placeholder(R.drawable.default_error)
						.error(R.drawable.default_error)
						.centerCrop()
						.crossFade()
						.into(holder.image);
			}
			return convertView;
		}
		class ViewHolder {
			ImageView image;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		publishModel.removeResponseListener(this);
		commonModel.removeResponseListener(this);
	}

}