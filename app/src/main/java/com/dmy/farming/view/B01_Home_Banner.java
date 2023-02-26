package com.dmy.farming.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.activity.ImagePreviewActivity;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.photopicker.PhotoPreviewActivity;
import com.external.viewpagerindicator.PageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.Utils.Utils;
import org.bee.activity.BaseActivity;
import org.bee.adapter.CycleViewPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("HandlerLeak")
public class B01_Home_Banner extends LinearLayout {
	Context mContext;
	Handler mHandler;

	private CycleViewPager bannerViewPager;
	private PageIndicator mIndicator;

	private PagerAdapter bannerPageAdapter = new PagerAdapter() {

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			if (bannerList != null)
				return bannerList.size();
			else
				return 0;
		}

		public Object instantiateItem(android.view.ViewGroup container, final int position) {
			ImageView viewOne =  (ImageView) LayoutInflater.from(mContext).inflate(R.layout.b01_home_banner_cell,null);
			if (position >= 0 && position < bannerList.size()) {

				final ADVER banner = bannerList.get(position);

				imageLoader.displayImage(banner.adver_img.url, viewOne, FarmingApp.options);

				viewOne.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ADVER banner = bannerList.get(position);
						if (!"0".equals(banner.target) && !TextUtils.isEmpty(banner.target)) {
							//((BaseActivity)mContext).clickAdver(banner);
//							if ("1".equals(banner.target)){
//                                ArrayList<String> imgs = new ArrayList<String>();
//                                for (ADVER ad:bannerList){
//                                    imgs.add(ad.adver_img.url);
//                                }
//                                Intent intent = new Intent(mContext,ImagePreviewActivity.class);
//                                intent.putExtra(PhotoPreviewActivity.EXTRA_CURRENT_ITEM, position);
//                                intent.putStringArrayListExtra(PhotoPreviewActivity.EXTRA_PHOTOS, imgs);
//                                mContext.startActivity(intent);
//							}
						}
						else
						{
							((BaseActivity)mContext).errorMsg("暂没有推荐内容");
						}
					}
				});
			}

			container.addView(viewOne);
			return viewOne;
		}

		public void destroyItem(android.view.ViewGroup container, int position, Object object) {
			container.removeView( (View)object);
		}
	};

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	ArrayList<ADVER> bannerList = new ArrayList<ADVER>();

	public int dpPadding = 0;

	public B01_Home_Banner(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				bindDataDelay();
			}
		};
	}

	Timer timer;
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int flag = msg.what;
			if (flag == 5)
			{
				try {
					nextPage();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				return;
			}
		}
	};

	protected void nextPage() {
		// TODO Auto-generated method stub
		//
		if (bannerList.size() > 0 && bannerViewPager != null)
		{
			int currentItem = bannerViewPager.getCurrentItem();

			if (currentItem >= bannerViewPager.getAdapter().getCount() - 1)
				currentItem = 0;
			else {
				currentItem = currentItem + 1;
			}
			bannerViewPager.setCurrentItem(currentItem);
		}
	}


	void bindDataDelay()
	{
		init();

		//stopReply();
		bannerViewPager.setAdapter( bannerPageAdapter);
		mIndicator.setViewPager(bannerViewPager, 1);
		mIndicator.notifyDataSetChanged();

		bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
		{
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				//AppUtils.setBannerPage(mContext, position);
				mIndicator.setCurrentItem(position);
			}
		});

		if (bannerViewPager != null)
			bannerViewPager.setScanScroll(bannerList.size() > 1);
	}

	public void startReply()
	{
		stopReply();

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run()
			{
				Message msg = handler.obtainMessage();
				msg.what = 5;
				handler.sendMessageDelayed(msg,5000);
			}
		},new Date(),5000);
	}

	public void stopReply()
	{
		if (timer != null)
		{
			timer.cancel();
			timer = null;
		}
	}

	void init()
	{
		if (bannerViewPager == null)
		{
			bannerViewPager = (CycleViewPager)findViewById(R.id.banner_viewpager);
			android.view.ViewGroup.LayoutParams params1 = bannerViewPager.getLayoutParams();
			params1.width = (int) (getDisplayMetricsWidth() - Utils.convertDpToPixel(mContext, dpPadding));
			params1.height = (int) (params1.width*1.0/720*330);

			bannerViewPager.setLayoutParams(params1);

			mIndicator = (PageIndicator)findViewById(R.id.indicator);
			mIndicator.setIsCycle(true);
		}
	}

	//获取屏幕宽度
	public int getDisplayMetricsWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int i = dm.widthPixels;
		int j = dm.heightPixels;
		return Math.min(i, j);
	}


	public void bindData(ArrayList<ADVER> bannerList)
	{
		boolean isChanged = false;

		if (this.bannerList.size() == bannerList.size())
		{
			for(int i = 0 ; i < bannerList.size(); i++)
			{
				if (!this.bannerList.get(i).adver_img.url.equals(bannerList.get(i).adver_img.url) || !this.bannerList.get(i).target.equals(bannerList.get(i).target))
				{
					isChanged = true;
					break;
				}
			}
		}
		else
		{
			isChanged = true;
		}

		if (isChanged)
		{
			this.bannerList.clear();
			this.bannerList.addAll(bannerList);

			mHandler.removeMessages(0);
			mHandler.sendEmptyMessage(0);
		}
	}
}