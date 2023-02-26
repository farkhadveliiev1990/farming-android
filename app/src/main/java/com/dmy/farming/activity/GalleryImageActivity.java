package com.dmy.farming.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.bee.activity.BaseActivity;
import com.dmy.farming.R;
import com.dmy.farming.adapter.GalleryImageAdapter;

public class GalleryImageActivity extends BaseActivity implements OnGestureListener, OnTouchListener {

	private ImageButton button_start;

	private ViewPager imagePager;
	private GalleryImageAdapter galleryImageAdapter;

	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
	private int pager_num;
	FrameLayout backgroundLayout;
	int  backgoundWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_image);

		shared = getSharedPreferences("userInfo", 0);
		editor = shared.edit();

		editor.putBoolean("bCheckUpdate", true);
		editor.commit();

		boolean isFirstRun = shared.getBoolean("isFirstRun", true);
		if(!isFirstRun) {
			Intent it = new Intent(this,MainActivity.class);
			startActivity(it);
			finish();
		}

		initLayout();
		backgroundLayout = (FrameLayout)findViewById(R.id.backgroundLayout);
		imagePager = (ViewPager) findViewById(R.id.image_pager);

		galleryImageAdapter = new GalleryImageAdapter(this);
		imagePager.setAdapter(galleryImageAdapter);
		galleryImageAdapter.parentHandler = m_handler;
		imagePager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				pager_num = position + 1;
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
									   int positionOffsetPixels)
			{

			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		imagePager.setOnTouchListener(this);

		button_start = (ImageButton) findViewById(R.id.button_start);
		button_start.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				nextStep();
			}
		});
	}

	GestureDetector mygesture = new GestureDetector(this);
	public boolean onTouch(View v, MotionEvent event) {
		return mygesture.onTouchEvent(event);
	}
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						   float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			if(pager_num == GalleryImageAdapter.SCREEN_CNT) {
				nextStep();
			}
		}
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	private static final int sleepTime = 1000;


	Handler m_handler = new Handler() {

		public void handleMessage(final Message msg) {
			if (msg.what == 1) {
				nextStep();
			}
		}
	};

	void nextStep()
	{
		editor.putBoolean("isFirstRun", false);
		editor.commit();
		finish();

		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	void initLayout()
	{
		DisplayMetrics dm = new DisplayMetrics();
		//取得窗口属性
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		backgoundWidth = dm.widthPixels*GalleryImageAdapter.SCREEN_CNT;
	}
}
