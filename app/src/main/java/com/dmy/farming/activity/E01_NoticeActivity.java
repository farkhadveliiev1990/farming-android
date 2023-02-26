package com.dmy.farming.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.model.CommonModel;
import com.dmy.farming.api.model.LoginModel;
import com.dmy.farming.fragment.B00_ActivityFragment;
import com.dmy.farming.fragment.B00_HomeFragment;
import com.dmy.farming.fragment.E01_HuDongFragment;
import com.dmy.farming.fragment.E01_MyCollectionExpertFragment;
import com.dmy.farming.fragment.E01_MyCollectionVideoFragment;
import com.dmy.farming.fragment.E01_NoticeFragment;
import com.dmy.farming.fragment.E01_NoticeItemFragment;
import com.dmy.farming.fragment.E01_WarnFragment;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

import java.util.ArrayList;

public class E01_NoticeActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_notice);
		//noticeItemFragment = (E01_NoticeItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_01);
		initView();

	}

	TextView text_tab_0, text_tab_1, text_tab_2;
	View img_line_0, img_line_1, img_line_2;
	ImageView img_menu_0, img_menu_1, img_menu_2;
	View layout_0,layout_1,layout_2;
    LinearLayout deleted;

	XListView list_black;
	View null_pager;
	ViewPager mPager;

	private ArrayList<Fragment> fragmentsList;

	E01_NoticeFragment noticeFragment;
	E01_WarnFragment warnFragment;

	E01_HuDongFragment huDongFragment;
	E01_WarnFragment.B00_WarningListAdapter warningListAdapter;
	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		layout_0 = findViewById(R.id.layout_0);
		layout_1 = findViewById(R.id.layout_1);
		layout_2 = findViewById(R.id.layout_2);

		layout_0.setOnClickListener(this);
		layout_1.setOnClickListener(this);
		layout_2.setOnClickListener(this);
		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		//null_pager = (TextView)findViewById(R.id.null_pager);
		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView)findViewById(R.id.text_tab_1);
		text_tab_2 = (TextView)findViewById(R.id.text_tab_2);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		text_tab_2.setOnClickListener(this);

		deleted = (LinearLayout)findViewById(R.id.deleted);
		deleted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(cur_tab==1){
                  //  getFragmentManager().findFragmentById(R.id.fragment_01).getView().findViewById(R.id.checklayout).setVisibility(View.VISIBLE);
					warnFragment.getView().findViewById(R.id.checklayout).setVisibility(View.VISIBLE);
					warnFragment.initBoxBeanList();

				}
				if(cur_tab==0){
					//  getFragmentManager().findFragmentById(R.id.fragment_01).getView().findViewById(R.id.checklayout).setVisibility(View.VISIBLE);
					noticeFragment.getView().findViewById(R.id.checklayout).setVisibility(View.VISIBLE);
					noticeFragment.initBoxBeanList();

				}
			}
		});

		img_line_0 = findViewById(R.id.img_line_0);
		img_line_1 = findViewById(R.id.img_line_1);
		img_line_2 = findViewById(R.id.img_line_2);

		//InitViewPager(mainView);
		//updateBottomLine();
		img_menu_0 = (ImageView)findViewById(R.id.img_menu_0);
		img_menu_1 = (ImageView)findViewById(R.id.img_menu_1);
		img_menu_2 = (ImageView)findViewById(R.id.img_menu_2);
		null_pager = findViewById(R.id.null_pager);

		mPager = (ViewPager)findViewById(R.id.vPager);
		fragmentsList = new ArrayList<>();
//		clickTab(0);

		noticeFragment = new E01_NoticeFragment();
		fragmentsList.add(noticeFragment);
		warnFragment  = new E01_WarnFragment();
		fragmentsList.add(warnFragment);
		huDongFragment = new  E01_HuDongFragment();
		fragmentsList.add(huDongFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mPager.setOffscreenPageLimit(0);
		mPager.setCurrentItem(1);




		// text_unread_cnt = (TextView) mainView.findViewById(R.id.text_unread_cnt);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				setResult(Activity.RESULT_OK);
				finish();
				break;
			case R.id.layout_0:
				clickTab(0);
				break;
			case R.id.layout_1:
				clickTab(1);
				break;
			case R.id.layout_2:
				clickTab(2);
				break;
			case R.id.text_tab_0:
				clickTab(0);
				break;
			case R.id.text_tab_1:
				clickTab(1);
				break;
			case R.id.text_tab_2:
				clickTab(2);
				break;
			case R.id.img_menu_0:
				clickTab(0);
				break;
			case R.id.img_menu_1:
				clickTab(1);
				break;
			case R.id.img_menu_2:
				clickTab(2);
				break;
		}
	}

	public int cur_tab = -1;
	int cur_index = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
			tabSelected(cur_tab);
		}
	}


	E01_NoticeItemFragment noticeItemFragment;

	public void tabSelected(int index)
	{
		//if (cur_index == index) return;
		cur_index = index;
		mPager.setCurrentItem(index);
	}


	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);
		img_line_2.setVisibility(View.INVISIBLE);
		this.img_menu_0.setImageResource(R.drawable.m_icon_system_default);
		this.img_menu_1.setImageResource(R.drawable.m_icon_warning_default);
		this.img_menu_2.setImageResource(R.drawable.m_icon_interaction_default);

		if (cur_tab == 0) {
			this.img_menu_0.setImageResource(R.drawable.m_icon_system);
			img_line_0.setVisibility(View.VISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			img_line_0.setBackgroundColor(getResources().getColor(R.color.green));

		}
		else if (cur_tab == 1) {
			this.img_menu_1.setImageResource(R.drawable.m_icon_warning);
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			text_tab_2.setTextColor(getResources().getColor(R.color.black));
			img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if (cur_tab == 2) {
			this.img_menu_2.setImageResource(R.drawable.m_icon_interaction);
			img_line_2.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.black));
			text_tab_0.setTextColor(getResources().getColor(R.color.black));
			img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
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
	protected void onPause() {
		closeKeyBoard();
		super.onPause();
	}

	// 关闭键盘
	public void closeKeyBoard() {
		/*edit_phone.clearFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_phone.getWindowToken(), 0);*/
	}





	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		/*if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}*/
	}


}