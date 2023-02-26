package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.B01_KnowledgeArticleAdapter;
import com.dmy.farming.adapter.B01_KnowledgeDiagnosisAdapter;
import com.dmy.farming.adapter.B01_KnowledgeVideoAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.diagnosticRequest;
import com.dmy.farming.api.farmarticleRequest;
import com.dmy.farming.api.model.CropCycleModel;
import com.dmy.farming.api.model.DiagnosticModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.FarmArticleModel;
import com.dmy.farming.api.model.FarmVideoModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class B01_KnowledgeActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b01_activity_knowledge);

		followModel = new DictionaryModel(this);
		followModel.addResponseListener(this);
		cropCycleModel = new CropCycleModel(this);
		cropCycleModel.addResponseListener(this);
		farmVideoModel = new FarmVideoModel(this);
		farmVideoModel.addResponseListener(this);
		farmArticleModel = new FarmArticleModel(this);
		farmArticleModel.addResponseListener(this);
		diagnosticModel = new DiagnosticModel(this);
		diagnosticModel.addResponseListener(this);
		request = new farmarticleRequest();
		diarequest = new diagnosticRequest();

		initView();
		initView1();

        updateData();
        followModel.followType(AppConst.info_from,true);


	}

	DictionaryModel followModel;
	CropCycleModel cropCycleModel;
	FarmVideoModel farmVideoModel;
	FarmArticleModel farmArticleModel;
	DiagnosticModel diagnosticModel;
	View layout_video,layout_article,layout_diagnosis;
	GridView grid_video,grid_article;
	B01_KnowledgeVideoAdapter videoAdapter;
	B01_KnowledgeArticleAdapter articleAdapter;
	B01_KnowledgeDiagnosisAdapter diagnosisAdapter;
	XListView list_knowledge;
	String crop_type = "";
	String cycle_type = "";
	String code_type="";
	int m;
	String  codetype;
	private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] codesubname;
	private String[] diccode;
	private String[] cyclecode;
	TextView more,videomore,diamore,sort,text_shot;
	farmarticleRequest request;
	ImageView img_search;
	diagnosticRequest diarequest;
//	View null_pager;
	int textsort = 0;
	int page = 1;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		list_knowledge = (XListView) findViewById(R.id.list_knowledge);

		View headerView = LayoutInflater.from(this).inflate(R.layout.b01_knowledge_header, null);

		layout_video = headerView.findViewById(R.id.layout_video);
		grid_video = (GridView) layout_video.findViewById(R.id.grid_video);

		layout_article = headerView.findViewById(R.id.layout_article);
		grid_article = (GridView) layout_article.findViewById(R.id.grid_article);

		layout_diagnosis = headerView.findViewById(R.id.layout_diagnosis);

		text_shot = (TextView) headerView.findViewById(R.id.text_shot);
		sort = (TextView) headerView.findViewById(R.id.sort);
		sort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupWindow == null) {
					sort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_pack), null);
					showSelectDialog(v);
				}

			}
		});

		//		null_pager = findViewById(R.id.null_pager);
		mGallery = (LinearLayout) headerView.findViewById(R.id.id_gallery);
		mGallery1 = (LinearLayout) headerView.findViewById(R.id.subcrop);

		img_search = (ImageView)findViewById(R.id.img_search) ;
		img_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(B01_KnowledgeActivity.this,B01_SearchActivity.class);
				startActivity(intent);
			}
		});

		more = (TextView)  headerView.findViewById(R.id.more);
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(B01_KnowledgeActivity.this,C01_AgrotechniqueArticleActivity.class);
				startActivity(intent);
			}
		});
		videomore = (TextView)  headerView.findViewById(R.id.videomore);
		videomore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(B01_KnowledgeActivity.this,C01_AgrotechniqueVideoActivity.class);
				startActivity(intent);
			}
		});
		diamore = (TextView)  headerView.findViewById(R.id.diamore);
		diamore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(B01_KnowledgeActivity.this,C01_DiagnosticLibActivity.class);
				startActivity(intent);
			}
		});

		headerView.findViewById(R.id.img_add).setOnClickListener(this);

		list_knowledge.addHeaderView(headerView);

		list_knowledge.setPullLoadEnable(false);
		list_knowledge.setPullRefreshEnable(false);
		list_knowledge.setXListViewListener(this,0);
		list_knowledge.setAdapter(null);

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
				WindowManager manager =getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				manager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				int a = width/5;
				textView.setWidth(a);
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
					//	crop_type = mImgIds[m];
						crop_type = diccode[m];
						mGallery1.removeAllViews();
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
						cyclename = codesubname[m];
						code_type = cyclecode[m];
						videoAdapter = null;
						articleAdapter = null;

						requestarticle(true);
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


	PopupWindow popupWindow;
	private void showSelectDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.c01_diagnostic_item, null);
		final View collect = view.findViewById(R.id.layout_collect);
		collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textsort = 0;
//				diagnosticAdapter = null;
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
				textsort = 1;
//				diagnosticAdapter = null;
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
				textsort = 2;
//				diagnosticAdapter = null;
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
				sort.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.v_icon_open), null);
			}
		});

		popupWindow.showAsDropDown(v,-140,-15);

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.img_add:
				if(checkLogined()) {
					intent = new Intent(B01_KnowledgeActivity.this, ChooseCropActivity.class);
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

	public void requestarticle(boolean bShow) {
		request.info_from = AppConst.info_from;
		request.crop_type = crop_type;
		request.cycle_type = code_type;
		request.page = 1;
		request.provice =AppUtils.getCurrProvince(this);
		request.city = AppUtils.getCurrCity(this);
		farmVideoModel.getFarmVideo(request,true);
		farmArticleModel.getFarmArticle(request,true);
	}

	public void requestData(boolean bShow) {
		diarequest.crop_type = crop_type;
		diarequest.cycle_code = code_type;
		diarequest.info_from = AppConst.info_from;
		diarequest.city = AppUtils.getCurrCity(this);
		diarequest.sort = textsort;
		diarequest.page = page;
		diarequest.provice = AppUtils.getCurrProvince(this);
		diagnosticModel.getDiagnosticList(diarequest,bShow);
	}

	private ArrayList<DICTIONARY> ad;
	private void updatecroyData() {
		if (followModel.data.follow_type.size() > 0) {
//			null_pager.setVisibility(View.GONE);
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
//			null_pager.setVisibility(View.VISIBLE);

//			Intent intent = new Intent(B01_KnowledgeActivity.this,AddCropActivity.class);

			Intent intent = new Intent(B01_KnowledgeActivity.this,ChooseCropActivity.class);
			ArrayList<String> attentname = new ArrayList<>();
			ArrayList<String> attentcode = new ArrayList<>();
			intent.putStringArrayListExtra("attentname",attentname);
			intent.putStringArrayListExtra("attentcode",attentcode);
			startActivity(intent);
		}
	}

	String cyclename = "";
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
			articleAdapter = null;
			diagnosisAdapter = null;
			cyclename = ad1.get(0).dicname;

			requestarticle(true);
			requestData(true);

		} else {
		}
	}

//	E10_MyQuestionListAdapter listAdapter;

	@Override
	protected void onResume() {
		super.onResume();
	}

	void updateData()
	{
		updateVideo();
		updateArticle();
		updateDiagnosis();
	}

	private void updateVideo() {
		int size = farmVideoModel.data.videolists.size();
		if (size > 0) {
			layout_video.setVisibility(View.VISIBLE);
			if (videoAdapter == null) {
				videoAdapter = new B01_KnowledgeVideoAdapter(this,farmVideoModel.data.videolists,code_type,cyclename);
				grid_video.setAdapter(videoAdapter);
			} else {
				videoAdapter.notifyDataSetChanged();
			}
		} else {
			videoAdapter = null;
			layout_video.setVisibility(View.GONE);
		}
	}

	private void updateArticle() {
		int size = farmArticleModel.data.farmarticles.size();
		if (size > 0) {
			layout_article.setVisibility(View.VISIBLE);
			if (articleAdapter == null) {
				articleAdapter = new B01_KnowledgeArticleAdapter(this , farmArticleModel.data.farmarticles,code_type,cyclename);
				grid_article.setAdapter(articleAdapter);
			} else {
				articleAdapter.notifyDataSetChanged();
			}
		} else {
			articleAdapter = null;
			layout_article.setVisibility(View.GONE);
		}
	}

	private void updateDiagnosis() {
		int size = diagnosticModel.data.diagnostics.size();
		if (size > 0) {
			layout_diagnosis.setVisibility(View.VISIBLE);
			if (diagnosisAdapter == null) {
				diagnosisAdapter = new B01_KnowledgeDiagnosisAdapter(this , diagnosticModel.data.diagnostics,code_type,cyclename);
				list_knowledge.setAdapter(diagnosisAdapter);
			} else {
				diagnosisAdapter.notifyDataSetChanged();
			}
		} else {
			layout_diagnosis.setVisibility(View.GONE);
			diagnosisAdapter = null;
			list_knowledge.setAdapter(null);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.FOLLOWTYPE)) {
			updatecroyData();
		}else if(url.contains(ApiInterface.cropcycle)){
			updatecropctcleData();
		}else if(url.contains(ApiInterface.VIDEO)){
			updateVideo();
		}else if(url.contains(ApiInterface.ARTICLE)){
			updateArticle();
		}else if(url.contains(ApiInterface.DIAGNOSTICLIST)){
			updateDiagnosis();
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


	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {

	}
}