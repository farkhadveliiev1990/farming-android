package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_RentListAdapter;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.RentModel;
import com.dmy.farming.api.publishRentRequest;
import com.dmy.farming.fragment.C01_RentFragment;
import com.dmy.farming.fragment.C01_SellFragment;
import com.dmy.farming.fragment.C01_SellNongZiFragment;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_RentActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener , BusinessResponse {

	MainActivity mActivity;
	RentModel rentModel;
	DictionaryModel dictionaryModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_rent);
		rentModel = new RentModel(this);
		rentModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		request = new publishRentRequest();
		initView();

	}

	TextView text_tab_0,text_tab_1,text_tab_2,text_tab_3,text;
	ImageView img_line_0,img_line_1,img_line_2,imageView;

	XListView list_black;
	View null_pager;
	ViewPager mPager;
	C01_RentListAdapter listAdapter;
//	C00_MySellListAdapter listAdapter;
    private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] diccode;
	ImageView add;
	publishRentRequest request;
	EditText edit_keyword;
	String keyword = "";
	TextView text_city;
	int REQUEST_CITY = 2;

	void initView()
	{

		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		list_black = (XListView)findViewById(R.id.list_black);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);

		null_pager = findViewById(R.id.null_pager);
		mGallery = (LinearLayout)findViewById(R.id.id_gallery);

		add = (ImageView)findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkLogined()) {
					if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null){
						startActivity(new Intent(C01_RentActivity.this,A02_ThirdSignupActivity.class));
						return;
					}
					Intent intent = new Intent(C01_RentActivity.this, PublishRentActivity.class);
					startActivityForResult(intent,REQUEST_MONEY);
				}
			}
		});
		text_city = (TextView)findViewById(R.id.text_city);
		text_city.setOnClickListener(this);
		edit_keyword = (EditText)findViewById(R.id.edit_keyword);
		edit_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					keyword = edit_keyword.getText().toString();
					requestData(true);
				}
				return false;
			}
		});

		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView)findViewById(R.id.text_tab_1);
		text_tab_2 = (TextView)findViewById(R.id.text_tab_2);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		text_tab_2.setOnClickListener(this);

		img_line_0 = (ImageView)findViewById(R.id.img_line_0);
		img_line_1 =  (ImageView)findViewById(R.id.img_line_1);
		img_line_2 =  (ImageView)findViewById(R.id.img_line_2);

		clickTab(0);

	}

	int m;
	String  attention = "";

	private void initView1()// 填充数据
	{
		if(mImgIds != null || mImgIds.length != 0) {
			for ( int i = 0; i < mImgIds.length; i++) {
				/*final  View item =  LayoutInflater.from(this).inflate(R.layout.question_tab_item,null);
				final TextView text = (TextView)item.findViewById(R.id.text_tab);
				imageView = (ImageView) item.findViewById(R.id.img_line);
				imageView.setVisibility(View.GONE);
				text.setText(mImgIds[i]);
				text.setTag(i);
				text.setId(i);
				if (i == 0){
					imageView.setVisibility(View.VISIBLE);
					text.setTextColor(getResources().getColor(R.color.green));
				}else {
					imageView.setVisibility(View.GONE);
					text.setTextColor(getResources().getColor(R.color.black));
				}
				mGallery.addView(item);*/

				final TextView textView = new TextView(this);
				textView.setText(mImgIds[i]);
				textView.setId(i);
				textView.setTextSize(16);
				textView.setGravity(Gravity.CENTER);
				WindowManager manager =getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				textView.setWidth(width/3);
				textView.setPadding(5,10,5,5);
				//  textView.setLayoutParams(params);
				if(i==0){
					textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
					textView.setTextColor(getResources().getColor(R.color.green));
					type_code = diccode[i];
					requestData(true);
				}
				mGallery.addView(textView);
				textView.setTag(i);

				textView.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {

						v.setBackground(getResources().getDrawable(R.drawable.watermellon));
						textView.setTextColor(getResources().getColor(R.color.green));

						int id = v.getId();
						selectTab(id);
                        type_code = diccode[id];
						keyword = edit_keyword.getText().toString();
						requestData(true);


					}
				});

			}

		}
	}
	private void selectTab(int position) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mGallery.getChildCount(); i++) {
			//TextView childAt = (TextView) mGallery.getChildAt(position);
			TextView child = (TextView) mGallery.getChildAt(i);
			if (position == i) {
				child.setTextColor(getResources().getColor(R.color.green));
			} else {
				child.setTextColor(getResources().getColor(R.color.text_blackgrey));
				child.setBackgroundResource(0);
			}

		}

	}

	public void setChangeCity(boolean bMust) {
		String newCity = AppUtils.getCurrCity(this);   //GLOBAL_DATA.getInstance(mActivity).currCity
		String prevCity = text_city.getText().toString();

		if (TextUtils.isEmpty(newCity) || newCity.equals(prevCity)) {
			if (!bMust) return;
		} else {
			text_city.setText(newCity);
			prevCity = newCity;
			city = newCity;
			requestData(true);
		}

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				cur_tab = -1;
				type_code = "0";
				keyword = edit_keyword.getText().toString();
				clickTab(0);
				mGallery.removeAllViews();
				mGallery.setVisibility(View.GONE);
				break;
			case R.id.text_tab_1:
				cur_tab = -1;
				clickTab(1);
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
			case R.id.text_tab_2:
				cur_tab = -1;
				clickTab(2);
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
			case R.id.text_city:
				Intent	intent = new Intent(this, C00_CityActivity.class);
				startActivityForResult(intent, REQUEST_CITY);
				break;
		}
	}

	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
			//tabSelected(cur_tab);
		}
	}


	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);
		img_line_2.setVisibility(View.INVISIBLE);

		if (cur_tab == 0) {
			img_line_0.setVisibility(View.VISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_0.setBackgroundColor(getResources().getColor(R.color.green));

		}
		else if (cur_tab == 1) {
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if (cur_tab == 2) {
			img_line_2.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else{
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
		}

		if (cur_tab == 0){
			requestData(true);
		}else if (cur_tab == 1){
			dictionaryModel.getPublishTypeList(AppConst.info_from,"ZHAOZU_CODE");
//			requestData(true);
		}else if (cur_tab == 2){
			dictionaryModel.getPublishTypeList(AppConst.info_from,"CHUZU_CODE");
//			requestData(true);
		}
	}

	@Override
	protected void onResume() {
		setChangeCity(false);
	/*	updateData();
		requestData(true);*/
		super.onResume();
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (rentModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C01_RentListAdapter(this, rentModel.data.data);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == rentModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	String type_code = "0";
	String provience = "1";
	String city =  AppUtils.getCurrCity(this);
	String district = "1";
	float lon = 15;
	float lat = 12;
	int page = 1;
	String this_app = "1";




	private ArrayList<DICTIONARY> ad;
	private void updatecroyData() {
		if (dictionaryModel.data.gongqiu_label.size() > 0) {
			null_pager.setVisibility(View.GONE);
			ad =  dictionaryModel.data.gongqiu_label;
			diccode = new String[dictionaryModel.data.gongqiu_label.size()];
			if(dictionaryModel.data.gongqiu_label.size()>6){
				mImgIds = new String[6];
				for(int i = 0 ;i<6;i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}else{
				mImgIds = new String[dictionaryModel.data.gongqiu_label.size()];
				for(int i = 0 ;i<dictionaryModel.data.gongqiu_label.size();i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}
			initView1();
		} else {
			null_pager.setVisibility(View.VISIBLE);
		}
	}
	public void requestData(boolean bShow)
	{
		request.info_from = AppConst.info_from;
		request.city =city;
		request.provience = "";
		request.district = "";
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = 1;
		request.this_app = "1";
		request.keyword = keyword;
		request.type_code = type_code;
		rentModel.getrentList(request,bShow);
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.RENT)) {
			list_black.setRefreshTime();
			updateData();
		}
		if (url.contains(ApiInterface.GONGQIULABEL)) {
			updatecroyData();
		}
	}





	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_MONEY) {
				requestData(true);
			}
		}
		if (requestCode == REQUEST_CITY) {
			if(data!=null) {
				setChangeCity(false);
			}
		}
	}

	@Override
	public void onRefresh(int id) {
		   requestData(true);
	}

	@Override
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	@Override
	public void onLoadMore(int id) {
		page = page+1;
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(this);
		request.provience = "";
		request.district = "";
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = page;
		request.this_app = "1";
		request.keyword = keyword;
		request.type_code = type_code;
		rentModel.getrentListMore(request,false);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		rentModel.removeResponseListener(this);
		dictionaryModel.removeResponseListener(this);
	}

	public void closeKeyBoard() {
		edit_keyword.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
	}

}