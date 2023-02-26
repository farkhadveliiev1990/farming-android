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
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.model.CropCycleModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.SaleModel;
import com.dmy.farming.api.saleRequest;
import com.dmy.farming.fragment.C01_SellFragment;
import com.dmy.farming.fragment.C01_SellNongZiFragment;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_SellActivity extends BaseActivity implements OnClickListener,  XListView.IXListViewListener ,BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_sell);

		saleModel = new SaleModel(this);
		saleModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		dictionaryModel.getPublishTypeList(AppConst.info_from,"SALE_TYPE");
		cropCycleModel = new CropCycleModel(this);
		cropCycleModel.addResponseListener(this);
		request = new saleRequest();
		initView();
		requestData(true);

	}

	TextView text_tab_0, text_tab_1,text;

	XListView list_black;
	View null_pager;
	ViewPager mPager;
	SaleModel saleModel;
	DictionaryModel dictionaryModel;
	CropCycleModel cropCycleModel;
	private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] diccode;
	private String[] codesubname;//二级名称
	private String[] cyclecode;//二级代码


	saleRequest request;
	ImageView add;
	EditText edit_keyword;
	String keyword = "";
	TextView text_city;
	int REQUEST_CITY = 2;

	C00_MySellListAdapter listAdapter;

	void initView()
	{

		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_0.setOnClickListener(this);
		clickTab(0);

		add = (ImageView)findViewById(R.id.add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkLogined()) {
					if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null){
						startActivity(new Intent(C01_SellActivity.this,A02_ThirdSignupActivity.class));
						return;
					}
					Intent intent = new Intent(C01_SellActivity.this,E01PubilcSellActivity.class);
					startActivityForResult(intent,REQUEST_MONEY);
				}

			}
		});

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
		text_city = (TextView)findViewById(R.id.text_city);
		text_city.setOnClickListener(this);

		mGallery = (LinearLayout)findViewById(R.id.id_gallery);
		mGallery1 = (LinearLayout)findViewById(R.id.subcrop);
		list_black = (XListView)findViewById(R.id.list_black);
		null_pager = findViewById(R.id.null_pager);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	int m;
	String  attention = "";
	int p  = 1 ;
	private void initView1()// 填充数据
	{

		int left, top, right, bottom;
		left = top = right = bottom = 20;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
		int childsize = mGallery.getChildCount();
		if (childsize >1){
			for(int j = childsize;j<=childsize&&j>=0;j--){
				if(j>1){
					mGallery.removeViewAt(j-1);
				}
			}
		}
		if(mImgIds != null) {
			for ( int i = 0; i < mImgIds.length; i++) {
				final  View item =  LayoutInflater.from(this).inflate(R.layout.gongqiu_tab_item,null);
				text = (TextView)item.findViewById(R.id.text_tab);
				text.setText(mImgIds[i]);
				text.setTag(i);
				text.setId(i);
				mGallery.addView(item);
				text.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						int id = v.getId();
						for(int j = 0 ; j < mImgIds.length ;j++){
							if(j == id){
								((TextView)findViewById(id)).setTextColor(getResources().getColor(R.color.green));
								clickTab(1);
							}else{
								((TextView)findViewById(j)).setTextColor(getResources().getColor(R.color.text_blackgrey));
								clickTab(1);
							}
						}
						type_code = diccode[id];
						keyword = edit_keyword.getText().toString();
						mGallery1.setVisibility(View.VISIBLE);
						mGallery1.removeAllViews();
						cropCycleModel.getsaleTypeList(AppConst.info_from,type_code);
						//requestData(true);
                     /*   text.setTextColor(getResources().getColor(R.color.green));
                        m=(Integer) text.getTag();
                        selectTab(m);*/
						//  imageView.setVisibility(View.VISIBLE);
					}
				});

			}


		}
	}

	private void initView2()// 填充数据
	{
		mGallery1.removeAllViews();
		if(codesubname != null) {
			for (int i = 0; i < codesubname.length; i++) {
				final TextView textView = new TextView(this);
				textView.setText(codesubname[i]);
				textView.setId(i);
				textView.setTextSize(16);
				WindowManager manager =getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				textView.setWidth(width/4);
				textView.setGravity(Gravity.CENTER);
				textView.setPadding(10,5,10,5);
				textView.setTextColor(getResources().getColor(R.color.text_grey));
				if(i==0){
					textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
					textView.setTextColor(getResources().getColor(R.color.green));
				}
				mGallery1.addView(textView);
				textView.setTag(i);
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						textView.setTextColor(getResources().getColor(R.color.green));
						textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
						m=(Integer) textView.getTag();
						selectTab1(m);
						type_code = cyclecode[m];
						listAdapter = null;
						requestData(true);

					}
				});
			}


		}
	}

    private void selectTab1(int position) {
        // TODO Auto-generated method stub
        for (int i = 0; i < mGallery1.getChildCount(); i++) {
            //TextView childAt = (TextView) mGallery.getChildAt(position);
            TextView child = (TextView) mGallery1.getChildAt(i);
            if (position == i) {
                child.setTextColor(getResources().getColor(R.color.green));
            } else {
                child.setTextColor(getResources().getColor(R.color.text_grey));
                child.setBackgroundResource(0);
            }

        }

    }



	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				type_code = "";
				for(int i = 0;i<mImgIds.length;i++){
					((TextView)findViewById(i)).setTextColor(getResources().getColor(R.color.text_blackgrey));
				}
				keyword = edit_keyword.getText().toString();
				requestData(true);
				clickTab(0);
				mGallery1.removeAllViews();
				mGallery1.setVisibility(View.GONE);

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
	public void setChangeCity(boolean bMust) {
		String newCity = AppUtils.getCurrCity(this);   //GLOBAL_DATA.getInstance(mActivity).currCity
		String prevCity = text_city.getText().toString();

		if (TextUtils.isEmpty(newCity) || newCity.equals(prevCity)) {
			if (!bMust) return;
		} else {
			text_city.setText(newCity);
			prevCity = newCity;
			city = newCity;
			listAdapter=null;
			requestData(true);
		}

	}

	String type_code = "";
	String provience = "辽宁";
	String city = AppUtils.getCurrCity(this);
	String district = "浑南";
	double lon = 1;
	double lat = 1;
	int page = 1;
	String this_app = "1";


	public void requestData(boolean bShow)
	{
		request.info_from = AppConst.info_from;
		request.city = city;
		request.provience = "";
		request.district = "";
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = 1;
		request.this_app = "1";
		request.keyword = keyword;
		request.type_code = type_code;
		saleModel.getSaleList(request,bShow);
	}

	void updateBottomLine() {
		/*img_line_1.setVisibility(View.INVISIBLE);
		img_line_2.setVisibility(View.INVISIBLE);
		img_line_3.setVisibility(View.INVISIBLE);
*/
		if (cur_tab == 0) {
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			/*text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));*/

		}
		/*else if (cur_tab == 1) {
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if (cur_tab == 2) {
			img_line_2.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
		}*/
		else{
		/*	text_tab_3.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));*/
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
		//	img_line_0.setBackgroundColor(getResources().getColor(R.color.green));
		}
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (saleModel.data.routes.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C00_MySellListAdapter(this, saleModel.data.routes);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == saleModel.paginated.more) {
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

	private ArrayList<DICTIONARY> ad;

	private void updatecroyData() {
		if (dictionaryModel.data.gongqiu_label.size() > 0) {
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
		}
	}

	private ArrayList<DICTIONARY> ad1;
	private void updatecropctcleData() {
		if (cropCycleModel.saletype.sale_label.size() > 0) {
			ad1 =  cropCycleModel.saletype.sale_label;
			codesubname = new String[cropCycleModel.saletype.sale_label.size()];
			cyclecode = new String[cropCycleModel.saletype.sale_label.size()];
			for(int i = 0 ;i<ad1.size();i++){
				codesubname[i] = ad1.get(i).name;
				cyclecode[i] = ad1.get(i).code;
			}
			type_code = ad1.get(0).code;
			initView2();
			requestData(true);
		} else {
		}
	}


	@Override
	protected void onResume() {
		setChangeCity(true);
		updateData();

		super.onResume();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.SALE)) {
			list_black.setRefreshTime();
			updateData();
		}
		if (url.contains(ApiInterface.GONGQIULABEL)) {
			updatecroyData();
		}
		if (url.contains(ApiInterface.SALELABEL)) {
			updatecropctcleData();
		}
	}



	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		requestData(true);
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
		request.district ="";
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = page;
		request.this_app = "1";
		request.keyword = keyword;
		request.type_code = type_code;
		saleModel.getBuyListMore(request,false);
	}

	public void closeKeyBoard() {
		edit_keyword.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
	}



}