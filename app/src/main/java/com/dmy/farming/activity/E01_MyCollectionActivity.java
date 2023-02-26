package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.model.CollectionModel;
import com.dmy.farming.api.model.FarmVideoModel;
import com.dmy.farming.fragment.E01_MyCollectionActicleFragment;
import com.dmy.farming.fragment.E01_MyCollectionBuyFragment;
import com.dmy.farming.fragment.E01_MyCollectionDiagnosticFragment;
import com.dmy.farming.fragment.E01_MyCollectionFarmArticleFragment;
import com.dmy.farming.fragment.E01_MyCollectionFindHelpFragment;
import com.dmy.farming.fragment.E01_MyCollectionQuestionFragment;
import com.dmy.farming.fragment.E01_MyCollectionExpertFragment;
import com.dmy.farming.fragment.E01_MyCollectionRentFragment;
import com.dmy.farming.fragment.E01_MyCollectionSellFragment;
import com.dmy.farming.fragment.E01_MyCollectionVideoFragment;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

import java.util.ArrayList;

public class E01_MyCollectionActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	public CollectionModel collectionModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_collection);

		collectionModel = collectionModel.getInstance(this);
		collectionModel.addResponseListener(this);

		initView();

	}


	XListView list_black;
	int cur_index = -1;
	View null_pager;
	ViewPager mPager;
	TextView text_tab_0, text_tab_1, text_tab_2, text_tab_3,text_tab_4,text_tab_5,text_tab_6,text_tab_7,text_tab_8,text_tab_9;
	View img_line_0, img_line_1, img_line_2, img_line_3,img_line4,img_line5,img_line6,img_line7,img_line8,img_line9;

	private ArrayList<Fragment> fragmentsList;

	E01_MyCollectionExpertFragment chatItemFragment;
	E01_MyCollectionVideoFragment videoFragment;
	E01_MyCollectionActicleFragment acticleFragment;
	E01_MyCollectionQuestionFragment questionFragment;
	E01_MyCollectionSellFragment sellFragment;
	E01_MyCollectionBuyFragment buyFragment;
	E01_MyCollectionRentFragment rentFragment;
	E01_MyCollectionFindHelpFragment findHelpFragment;
	E01_MyCollectionFarmArticleFragment farmArticleFragment;
    E01_MyCollectionDiagnosticFragment diagnosticFragment;
	FarmVideoModel farmVideoModel;


	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		text_tab_0 = (TextView)findViewById(R.id.expert);
		text_tab_1 = (TextView)findViewById(R.id.video);
		text_tab_2 = (TextView)findViewById(R.id.wenzhang);
		text_tab_3 = (TextView)findViewById(R.id.chat);
		text_tab_4 = (TextView)findViewById(R.id.gongqiu);
		text_tab_5 = (TextView)findViewById(R.id.buy);
		text_tab_6 = (TextView)findViewById(R.id.rent);
		text_tab_7 = (TextView)findViewById(R.id.findhelp);
		text_tab_8 = (TextView)findViewById(R.id.farmarticle);
		text_tab_9 = (TextView)findViewById(R.id.diagnostic);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		text_tab_2.setOnClickListener(this);
		text_tab_3.setOnClickListener(this);
		text_tab_4.setOnClickListener(this);
		text_tab_5.setOnClickListener(this);
		text_tab_6.setOnClickListener(this);
		text_tab_7.setOnClickListener(this);
		text_tab_8.setOnClickListener(this);
		text_tab_9.setOnClickListener(this);

		img_line_0 = findViewById(R.id.line1);
		img_line_1 = findViewById(R.id.line2);
		img_line_2 = findViewById(R.id.line3);
		img_line_3 = findViewById(R.id.line4);
		img_line4 =  findViewById(R.id.line5);
		img_line5 =  findViewById(R.id.line6);
		img_line6 =  findViewById(R.id.line7);
		img_line7 =  findViewById(R.id.line8);
		img_line8 =  findViewById(R.id.line9);
		img_line9 =  findViewById(R.id.line10);

		null_pager = findViewById(R.id.null_pager);
		mPager = (ViewPager)findViewById(R.id.vPager);

		InitViewPager();
		clickTab(0);

	}

	private void InitViewPager() {
		fragmentsList = new ArrayList<>();
		chatItemFragment = new E01_MyCollectionExpertFragment();
		videoFragment = new E01_MyCollectionVideoFragment();
		acticleFragment = new E01_MyCollectionActicleFragment();
		questionFragment = new E01_MyCollectionQuestionFragment();
		sellFragment = new E01_MyCollectionSellFragment();
		buyFragment = new E01_MyCollectionBuyFragment();
		rentFragment = new E01_MyCollectionRentFragment();
		findHelpFragment = new E01_MyCollectionFindHelpFragment();
		farmArticleFragment = new E01_MyCollectionFarmArticleFragment();
		diagnosticFragment = new E01_MyCollectionDiagnosticFragment();

		fragmentsList.add(chatItemFragment);
		fragmentsList.add(videoFragment);
		fragmentsList.add(acticleFragment);
		fragmentsList.add(questionFragment);
		fragmentsList.add(sellFragment);
		fragmentsList.add(buyFragment);
		fragmentsList.add(rentFragment);
		fragmentsList.add(findHelpFragment);
		fragmentsList.add(farmArticleFragment);
		fragmentsList.add(diagnosticFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setOffscreenPageLimit(10);
		mPager.setCurrentItem(0);
	}

	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageSelected(int pageIndex) {
			cur_tab = pageIndex;
			updateBottomLine();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.expert:
				clickTab(0);
				break;
			case R.id.video:
				clickTab(1);
				break;
			case R.id.wenzhang:
				clickTab(2);
				break;
			case R.id.chat:
				clickTab(3);
				break;
			case R.id.gongqiu:
				clickTab(4);
				break;
			case R.id.buy:
				clickTab(5);
				break;
			case R.id.rent:
				clickTab(6);
				break;
			case R.id.findhelp:
				clickTab(7);
				break;
			case R.id.farmarticle:
				clickTab(8);
				break;
			case R.id.diagnostic:
				clickTab(9);
				break;

		}
	}



	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		collectionModel.removeResponseListener(this);
	}


	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
			tabSelected(cur_tab);
		}
	}



	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);
		img_line_2.setVisibility(View.INVISIBLE);
		img_line_3.setVisibility(View.INVISIBLE);
		img_line4.setVisibility(View.INVISIBLE);
		img_line5.setVisibility(View.INVISIBLE);
		img_line6.setVisibility(View.INVISIBLE);
		img_line7.setVisibility(View.INVISIBLE);
		img_line8.setVisibility(View.INVISIBLE);
		img_line9.setVisibility(View.INVISIBLE);

		if (cur_tab == 0) {
			img_line_0.setVisibility(View.VISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line_0.setBackgroundColor(getResources().getColor(R.color.green));

		}
		else if (cur_tab == 1) {
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if (cur_tab == 2) {
			img_line_2.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 3){
			img_line_3.setVisibility(View.VISIBLE);
			text_tab_3.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line_3.setBackgroundColor(getResources().getColor(R.color.green));
		}else if(cur_tab == 4){
			img_line4.setVisibility(View.VISIBLE);
			text_tab_4.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line4.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 5){
			img_line5.setVisibility(View.VISIBLE);
			text_tab_5.setTextColor(getResources().getColor(R.color.green));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line5.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 6){
			img_line6.setVisibility(View.VISIBLE);
			text_tab_6.setTextColor(getResources().getColor(R.color.green));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line6.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 7){
			img_line7.setVisibility(View.VISIBLE);
			text_tab_7.setTextColor(getResources().getColor(R.color.green));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line7.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 8){
			img_line8.setVisibility(View.VISIBLE);
			text_tab_8.setTextColor(getResources().getColor(R.color.green));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_9.setTextColor(getResources().getColor(R.color.black));
			img_line8.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if(cur_tab == 9){
			img_line9.setVisibility(View.VISIBLE);
			text_tab_9.setTextColor(getResources().getColor(R.color.green));
			text_tab_4.setTextColor(getResources().getColor(R.color.black));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_3.setTextColor(getResources().getColor(R.color.black));
			text_tab_6.setTextColor(getResources().getColor(R.color.black));
			text_tab_5.setTextColor(getResources().getColor(R.color.black));
			text_tab_7.setTextColor(getResources().getColor(R.color.black));
			text_tab_8.setTextColor(getResources().getColor(R.color.black));
			img_line9.setBackgroundColor(getResources().getColor(R.color.green));
		}
	}


	public void tabSelected(int index)
	{
		//if (cur_index == index) return;
		cur_index = index;
		mPager.setCurrentItem(index);
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


}