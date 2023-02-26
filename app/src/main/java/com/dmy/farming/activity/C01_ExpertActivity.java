package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_MyBuyListAdapter;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.protocol.PHOTO;
import com.dmy.farming.view.B01_Home_Banner;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

import java.util.ArrayList;

public class C01_ExpertActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_expert);

		initView();

	}

	XListView list_black;
	View null_pager;
	B01_Home_Banner mainBanner;


	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		View answer = findViewById(R.id.answer);
		answer.setOnClickListener(this);
		View fans = findViewById(R.id.fans);
		fans.setOnClickListener(this);
		View time = findViewById(R.id.time);
		time.setOnClickListener(this);
		View layout_0 = findViewById(R.id.layout_0);
		layout_0.setOnClickListener(this);

		mainBanner = (B01_Home_Banner)findViewById(R.id.layout_banner);
		initBanner();
	}

	Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.answer:
				intent = new Intent(C01_ExpertActivity.this,C01_ExpertAnswerListItemActivity.class);
				startActivity(intent);
				 break;
			case R.id.layout_0:
				intent = new Intent(C01_ExpertActivity.this,C01_ExpertDatailActivity.class);
				startActivity(intent);
				break;
		}
	}

	E01_MyBuyListAdapter listAdapter;

	@Override
	protected void onResume() {
		super.onResume();
		updateData();
	}
	void updateData()
	{
		upadteBanner();
	}

	ArrayList<ADVER> adver_top = new ArrayList<>();

	String[] urls = new String[]{
			"https://cdn.pixabay.com/photo/2017/03/30/13/33/photography-2188440_960_720.jpg",
			"https://cdn.pixabay.com/photo/2014/09/22/00/56/photographer-455747_960_720.jpg",
			"https://thumb9.shutterstock.com/display_pic_with_logo/1619858/520127938/stock-photo-stylish-woman-photographer-with-retro-camera-on-the-yellow-wall-background-image-with-copy-space-520127938.jpg"};
	void initBanner()
	{
		for (int i = 0; i < urls.length; i++)
		{
			ADVER adver = new ADVER();
			PHOTO photo = new PHOTO();
			photo.url = urls[i];
			adver.adver_img = photo;
			adver.adver_id = (i + 5000) + "";
			adver.target = (i + 5000) + "";
			adver_top.add(adver);
		}
	}

	void upadteBanner()
	{
		if (adver_top != null && adver_top.size() > 0)
		{
			mainBanner.bindData(adver_top);

			if (adver_top.size() == 1)
				mainBanner.stopReply();
			else
				mainBanner.startReply();

		}

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

	}
}