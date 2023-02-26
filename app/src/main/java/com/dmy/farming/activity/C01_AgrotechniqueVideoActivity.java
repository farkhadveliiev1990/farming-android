package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_AgrotechniqueVideoAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.farmarticleRequest;
import com.dmy.farming.api.model.CropCycleModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.FarmVideoModel;
import com.dmy.farming.view.mzbanner.MZBannerView;
import com.dmy.farming.view.mzbanner.holder.MZHolderCreator;
import com.dmy.farming.view.mzbanner.holder.MZViewHolder;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class C01_AgrotechniqueVideoActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_activity_agrotechnique_video);

		followModel = new DictionaryModel(this);
		followModel.addResponseListener(this);
		cropCycleModel = new CropCycleModel(this);
		cropCycleModel.addResponseListener(this);
		farmVideoModel = new FarmVideoModel(this);
		farmVideoModel.addResponseListener(this);

		request  = new farmarticleRequest();

		initView();
		initView1();

		followModel.followType(AppConst.info_from,true);
	}

	View null_pager;
	GridView grid_video;
	C01_AgrotechniqueVideoAdapter videoAdapter;
	DictionaryModel followModel;
	CropCycleModel cropCycleModel;
	FarmVideoModel farmVideoModel;
	String  crop_type = "";
	String  cycle_type = "";
	String code_type="";
	String  codetype;
	private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] codesubname;
	private String[] diccode;
	private String[] cyclecode;
	ImageLoader imageLoader = ImageLoader.getInstance();
	farmarticleRequest request;
	TextView textsort,text_shot;
	ImageView img_search;
	MZBannerView mMZBanner;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);
		mGallery = (LinearLayout) findViewById(R.id.id_gallery);
		mGallery1 = (LinearLayout) findViewById(R.id.subcrop);

		findViewById(R.id.img_add).setOnClickListener(this);

		img_search = (ImageView)findViewById(R.id.img_search) ;
		img_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(C01_AgrotechniqueVideoActivity.this,B01_SearchActivity.class);
				startActivity(intent);
			}
		});
		grid_video = (GridView) findViewById(R.id.grid_video);

		mMZBanner = (MZBannerView) findViewById(R.id.banner);
		mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
			@Override
			public void onPageClick(View view, int position) {
				errorMsg("click page:"+position);
			}
		});

		int []BANNER = new int[]{R.mipmap.banner1,R.mipmap.banner2,R.mipmap.banner3,R.mipmap.banner4};
		List<Integer> bannerList = new ArrayList<>();
		for(int i=0;i<BANNER.length;i++){
			bannerList.add(BANNER[i]);
		}
		mMZBanner.setIndicatorVisible(true);
		// 代码中更改indicator 的位置
		//mMZBanner.setIndicatorAlign(MZBannerView.IndicatorAlign.LEFT);
		//mMZBanner.setIndicatorPadding(10,0,0,150);

//		mMZBanner.setPages(bannerList, new MZHolderCreator<BannerViewHolder>() {
//			@Override
//			public BannerViewHolder createViewHolder() {
//				return new BannerViewHolder();
//			}
//		});

		textsort = (TextView)findViewById(R.id.sort);
		text_shot = (TextView)findViewById(R.id.text_shot);
		textsort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupWindow == null) {
					textsort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_pack), null);
					showSelectDialog(v);
				}

			}
		});


	}

	public static class BannerViewHolder implements MZViewHolder<Integer> {
		private ImageView mImageView;
		@Override
		public View createView(Context context) {
			// 返回页面布局文件
			View view = LayoutInflater.from(context).inflate(R.layout.banner_item_padding,null);
			mImageView = (ImageView) view.findViewById(R.id.banner_image);
			return view;
		}

		@Override
		public void onBind(Context context, int position, Integer data) {
			// 数据绑定
			mImageView.setImageResource(data);
		}
	}

	private void initView1()// 填充数据
	{
		mGallery.removeAllViews();
		if(mImgIds != null) {
			for (int i = 0; i < mImgIds.length; i++) {
				final TextView textView = new TextView(this);
				textView.setText(mImgIds[i]);
				textView.setId(i);
				textView.setTextSize(18);
				textView.setGravity(Gravity.CENTER);
				textView.setPadding(10,10,10,10);
				WindowManager manager =getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				textView.setWidth(width/5);
				textView.setTextColor(getResources().getColor(R.color.text_grey));
				//  textView.setLayoutParams(params);
				if(i==0){
					textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
					textView.setTextColor(getResources().getColor(R.color.green));
				}
				mGallery.addView(textView);
				textView.setTag(i);
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setBackground(getResources().getDrawable(R.drawable.watermellon));
						textView.setTextColor(getResources().getColor(R.color.green));
						m=(Integer) textView.getTag();
						selectTab(m);
						crop_type = mImgIds[m];
						mGallery1.removeAllViews();
						crop_type = diccode[m];
						cropCycleModel.cropcycleType(AppConst.info_from,crop_type,true);
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
				child.setTextColor(getResources().getColor(R.color.text_grey));
				child.setBackgroundResource(0);
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

	private void initView2()// 填充数据
	{
		mGallery1.removeAllViews();
		if(codesubname != null) {
			for (int i = 0; i < codesubname.length; i++) {
				final TextView textView = new TextView(this);
				textView.setText(codesubname[i]);
				textView.setId(i);
				textView.setTextSize(16);
				textView.setGravity(Gravity.CENTER);
				textView.setPadding(10,5,10,5);
				WindowManager manager =getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				textView.setWidth(width/5);
				textView.setTextColor(getResources().getColor(R.color.text_grey));
				if(i==0){
					textView.setTextColor(getResources().getColor(R.color.green));
				}
				mGallery1.addView(textView);
				textView.setTag(i);
				textView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						textView.setTextColor(getResources().getColor(R.color.green));
						m=(Integer) textView.getTag();
						selectTab1(m);
					//	code_type = codesubname[m];
						code_type = cyclecode[m];
						videoAdapter = null;

						request.info_from = AppConst.info_from;
						request.crop_type = crop_type;
						request.cycle_type = code_type;
						request.page = 1;
						request.provice = GLOBAL_DATA.getInstance(C01_AgrotechniqueVideoActivity.this).currProvince;
						request.city = GLOBAL_DATA.getInstance(C01_AgrotechniqueVideoActivity.this).currCity;
						farmVideoModel.getFarmVideo(request,true);
					}
				});
			}


		}
	}

	/**
	 * 获取随机颜色，便于区分
	 * @return
	 */
	public static String getRandColorCode(){
		String r,g,b;
		Random random = new Random();
		r = Integer.toHexString(random.nextInt(256)).toUpperCase();
		g = Integer.toHexString(random.nextInt(256)).toUpperCase();
		b = Integer.toHexString(random.nextInt(256)).toUpperCase();

		r = r.length()==1 ? "0" + r : r ;
		g = g.length()==1 ? "0" + g : g ;
		b = b.length()==1 ? "0" + b : b ;

		return r+g+b;
	}
	int m = 0;

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.img_add:
				if(checkLogined()) {
					intent = new Intent(C01_AgrotechniqueVideoActivity.this, ChooseCropActivity.class);
					ArrayList<String> attentname = new ArrayList<>();
					ArrayList<String> attentcode = new ArrayList<>();
					if (ad != null) {
						for (DICTIONARY dictionary : ad) {
							attentname.add(dictionary.name);
							attentcode.add(dictionary.aboutCode);
						}
					}
					intent.putStringArrayListExtra("attentname", attentname);
					intent.putStringArrayListExtra("attentcode", attentcode);
					startActivityForResult(intent,REQUEST_MONEY);
				}
				break;
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	PopupWindow popupWindow;
	private void showSelectDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.c01_diagnostic_item, null);
		final View collect = view.findViewById(R.id.layout_collect);
		collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sort = 0;
				videoAdapter = null;
				text_shot.setText("当前为按收藏量排序");
				requestData(true);
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
		final View pagenum = view.findViewById(R.id.layout_pagenum);
		pagenum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sort = 1;
				videoAdapter = null;
				text_shot.setText("当前为按浏览量排序");
				requestData(true);
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});
		final View time = view.findViewById(R.id.layout_time);
		time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sort = 2;
				videoAdapter = null;
				text_shot.setText("当前为按发布时间排序");
				requestData(true);
				if (popupWindow != null)
					popupWindow.dismiss();
			}
		});

		popupWindow = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				//这里如果返回true的话，touch事件将被拦截
				//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow = null;
				textsort.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.v_icon_open), null);
			}
		});

		popupWindow.showAsDropDown(v,-160,-30);

	}

	void requestData(Boolean show){
		request.info_from = AppConst.info_from;
		request.crop_type = ad1.get(0).cropDicid;
		request.cycle_type = ad1.get(0).cycleType;
		request.page = 1;
		request.sort = sort;
		request.provice = GLOBAL_DATA.getInstance(C01_AgrotechniqueVideoActivity.this).currProvince;
		request.city = GLOBAL_DATA.getInstance(C01_AgrotechniqueVideoActivity.this).currCity;
		farmVideoModel.getFarmVideo(request,true);

	}
	void updateData()
	{
		updateVideo();
	}

	private void updateVideo() {
		int size = farmVideoModel.data.videolists.size();
		if (size > 0) {
			if (videoAdapter == null) {
				videoAdapter = new C01_AgrotechniqueVideoAdapter(this ,farmVideoModel.data.videolists);
				grid_video.setAdapter(videoAdapter);
			} else {
				videoAdapter.notifyDataSetChanged();
			}

			// 初始化数据


		}else{
			videoAdapter = null;
			grid_video.setAdapter(null);
		}
	}

	final static int REQUEST_MONEY = 1;


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_MONEY) {
				followModel.followType(AppConst.info_from, true);
			}
		}
	}

	private ArrayList<DICTIONARY> ad;
	private void updatecroyData() {
		if (followModel.data.follow_type.size() > 0) {
			null_pager.setVisibility(View.GONE);
			mGallery1.setVisibility(View.VISIBLE);
			ad =  followModel.data.follow_type;
			diccode = new String[followModel.data.follow_type.size()];
			if(followModel.data.follow_type.size()>6){
				mImgIds = new String[6];
				for(int i = 0 ;i<6;i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).aboutCode;
				}
			}else{
				mImgIds = new String[followModel.data.follow_type.size()];
				for(int i = 0 ;i<followModel.data.follow_type.size();i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).aboutCode;
				}
			}
			codetype =  ad.get(0).name;
			// code_type = ad.get(0).aboutCode;
			crop_type = ad.get(0).aboutCode;
			initView1();
			cropCycleModel.cropcycleType(AppConst.info_from,crop_type,true);
		} else {
			mGallery1.setVisibility(View.GONE);
			null_pager.setVisibility(View.VISIBLE);

			Intent intent = new Intent(C01_AgrotechniqueVideoActivity.this,ChooseCropActivity.class);
			ArrayList<String> attentname = new ArrayList<>();
			ArrayList<String> attentcode = new ArrayList<>();
			intent.putStringArrayListExtra("attentname",attentname);
			intent.putStringArrayListExtra("attentcode",attentcode);
			startActivity(intent);
		}
	}

	int sort = 0;
	private ArrayList<CROPCYCLE> ad1;
	private void updatecropctcleData() {
		if (cropCycleModel.data.crop_cycle.size() > 0) {
			ad1 =  cropCycleModel.data.crop_cycle;
			codesubname = new String[cropCycleModel.data.crop_cycle.size()];
			cyclecode = new String[cropCycleModel.data.crop_cycle.size()];
			for(int i = 0 ;i<ad1.size();i++){
				codesubname[i] = ad1.get(i).dicname;
				cyclecode[i] = ad1.get(i).cycleType;
			}
			code_type = ad1.get(0).cycleType;
			initView2();
			videoAdapter = null;
			requestData(true);
		} else {
		}
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.FOLLOWTYPE)) {
			updatecroyData();
		}
		if(url.contains(ApiInterface.cropcycle)){
			updatecropctcleData();
		}
		if(url.contains(ApiInterface.VIDEO)){
			updateData();
		}
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		farmVideoModel.removeResponseListener(this);
		followModel.removeResponseListener(this);
		cropCycleModel.removeResponseListener(this);
	}
}