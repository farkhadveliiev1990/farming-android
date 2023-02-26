package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_ArticleListAdapter;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.articleListRequest;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.ArticleListModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.fragment.C01_FarmNewsFragment;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_FarmNewsActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {

	DictionaryModel dictionaryModel;
	ArticleListModel articleListModel;
	LinearLayout layout_tab;
	articleListRequest request;
	String type_code = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_farmnews);

		initView();

		type_code = getIntent().getStringExtra("type_code");
		request = new articleListRequest();

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		dictionaryModel.getArticleLabel(AppConst.info_from,"WENZHANGFENLEI");

		if (TextUtils.isEmpty(type_code) || type_code.equals("JIAJING")) {
			clickTab(0);
			requestData(true);
		}
	}

	XListView list_black;
	View null_pager;
	TextView text_tab_0, text_tab_1;
	View img_line_0, img_line_1;
	C01_ArticleListAdapter listAdapter;
	ImageView imageView;
	LinearLayout layout_1;
	 View item;

	void initView() {
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);

		null_pager = findViewById(R.id.null_pager);
		text_tab_0 = (TextView) findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView) findViewById(R.id.text_tab_1);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);

		img_line_0 = findViewById(R.id.img_line_0);
		img_line_1 = findViewById(R.id.img_line_1);

		layout_1 = (LinearLayout)findViewById(R.id.layout_1);

		layout_tab = (LinearLayout) findViewById(R.id.layout_tab);

//		InitViewPager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				cur_tab = -1;
				clickTab(0);
				if(mImgIds!=null) {
					for (int i = 0; i < mImgIds.length; i++) {
						((TextView)findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.black));
						findViewById(i).findViewById(R.id.img_line).setVisibility(View.INVISIBLE);
					//	v.findViewById(R.id.img_line).setVisibility(View.GONE);
					}
				}
				type_code = "JIAJING";
				listAdapter = null;
                requestData(true);
				break;
			case R.id.text_tab_1:
				cur_tab = -1;
				clickTab(1);
				if(mImgIds!=null) {
					for (int i = 0; i < mImgIds.length; i++) {
						((TextView)findViewById(i).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.black));
						findViewById(i).findViewById(R.id.img_line).setVisibility(View.INVISIBLE);
						//	v.findViewById(R.id.img_line).setVisibility(View.GONE);
					}
				}

				type_code = "HOT";
				listAdapter = null;
                requestData(true);
				break;

		}
	}

	private ArrayList<DICTIONARY> ad;
	private String[] mImgIds;
	private String[] diccode;

	private void updateTabData() {
		if (dictionaryModel.data.article_label.size() > 0) {
			ad =  dictionaryModel.data.article_label;
			diccode = new String[dictionaryModel.data.article_label.size()];
			if(dictionaryModel.data.article_label.size()>6){
				mImgIds = new String[6];
				for(int i = 0 ;i<6;i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}else{
				mImgIds = new String[dictionaryModel.data.article_label.size()];
				for(int i = 0 ;i<dictionaryModel.data.article_label.size();i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}
			initView1();
		} else {
		}
	}

	private void initView1()// 填充数据
	{
		int left, top, right, bottom;
		left = top = right = bottom = 20;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
		if(mImgIds != null) {
			for ( int i = 0; i < mImgIds.length; i++) {
				item =  LayoutInflater.from(this).inflate(R.layout.article_tab_item,null);
				TextView text = (TextView)item.findViewById(R.id.text_tab);
				imageView = (ImageView) item.findViewById(R.id.img_line);
				text.setText(mImgIds[i]);
				item.setId(i);
				layout_tab.addView(item);
				if(type_code.equals(diccode[i])){
					text.setTextColor(getResources().getColor(R.color.green));
					imageView.setVisibility(View.VISIBLE);
					articleListModel = new ArticleListModel(C01_FarmNewsActivity.this,diccode[i]);
					articleListModel.addResponseListener(C01_FarmNewsActivity.this);
					requestData(true);
				}
				item.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						int id = v.getId();
						for(int j = 0 ; j < mImgIds.length ;j++){
							if(j == id){
								((TextView)v.findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.green));
								v.findViewById(R.id.img_line).setVisibility(View.VISIBLE);
								clickTab(-1);
							}else{
								((TextView)findViewById(j).findViewById(R.id.text_tab)).setTextColor(getResources().getColor(R.color.black));
								findViewById(j).findViewById(R.id.img_line).setVisibility(View.INVISIBLE);
								clickTab(-1);
							}
						}
						type_code = diccode[id];
						articleListModel = new ArticleListModel(C01_FarmNewsActivity.this,diccode[id]);
						articleListModel.addResponseListener(C01_FarmNewsActivity.this);
						listAdapter = null;
						requestData(true);
					}
				});
			}
		}
	}

	String provience = "辽宁";
	String city = "沈阳";
	String district = "浑南";
	int page = 1;
	String this_app = "0";

	public void requestData(boolean bShow)
	{
		provience = AppUtils.getCurrProvince(this);
		city = AppUtils.getCurrCity(this);
		district = AppUtils.getCurrDistrict(this);
		request.info_from = AppConst.info_from;
		request.type_code = type_code;
		request.provience = provience;
		request.city = city;
		request.district = district;
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = page;
		request.this_app = this_app;
		articleListModel.getArticleList(request,bShow);
	}

	int cur_tab = -1;

	void clickTab(int tab_index) {
		if (tab_index != cur_tab) {
			cur_tab = tab_index;
			updateBottomLine();
			//tabSelected(cur_tab);
		}else{
			img_line_0.setVisibility(View.INVISIBLE);
			img_line_1.setVisibility(View.INVISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
		}
		if (cur_tab == 0 || cur_tab == 1) {
			articleListModel = new ArticleListModel(C01_FarmNewsActivity.this, cur_tab+"");
			articleListModel.addResponseListener(C01_FarmNewsActivity.this);

		}
	}

	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);
		text_tab_0.setTextColor(getResources().getColor(R.color.black));
		text_tab_1.setTextColor(getResources().getColor(R.color.black));

		if (cur_tab == 0) {
			img_line_0.setVisibility(View.VISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));

		} else if (cur_tab == 1) {
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
		}
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (articleListModel.data.articleList.size() > 0)
		{

			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C01_ArticleListAdapter(this, articleListModel.data.articleList);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == articleListModel.data.paginated.more) {
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

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dictionaryModel != null)
			dictionaryModel.removeResponseListener(this);
		if (articleListModel != null)
			articleListModel.removeResponseListener(this);
	}

	private ArrayList<Fragment> fragmentsList;
	C01_FarmNewsFragment c01_FarmNewsFragment;
	ViewPager mPager;

	private void InitViewPager() {
		mPager = (ViewPager)findViewById(R.id.vPager);
		fragmentsList = new ArrayList<>();
		c01_FarmNewsFragment = new C01_FarmNewsFragment();


		fragmentsList.add(c01_FarmNewsFragment);
		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setOffscreenPageLimit(4);
		mPager.setCurrentItem(0);
	}


	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {
		page = page + 1;
		request.page = page;
		articleListModel.getArticleListMore(request,true);
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int pageIndex) {
			cur_tab = pageIndex;
			updateBottomLine();
			if (pageIndex == 0) {

			} else {

			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.ARTICLELABEL)) {
			updateTabData();

		} else if (url.contains(ApiInterface.ARTICLELIST)){
			list_black.setRefreshTime();
			updateData();
		}
	}
}