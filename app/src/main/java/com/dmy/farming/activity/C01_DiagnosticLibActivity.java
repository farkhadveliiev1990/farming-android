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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_DiagnosticLibAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.CROPCYCLE;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.diagnosticRequest;
import com.dmy.farming.api.model.CropCycleModel;
import com.dmy.farming.api.model.DiagnosticModel;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_DiagnosticLibActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	DictionaryModel followModel;
	CropCycleModel cropCycleModel;
	DiagnosticModel diagnosticModel;
	diagnosticRequest request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_activity_diagnosticlib);

		initView();

		followModel = new DictionaryModel(this);
		followModel.addResponseListener(this);

		cropCycleModel = new CropCycleModel(this);
		cropCycleModel.addResponseListener(this);

		diagnosticModel = new DiagnosticModel(this);
		diagnosticModel.addResponseListener(this);
		request = new diagnosticRequest();

		followModel.followType(AppConst.info_from,true);


	}

	XListView listView;
	C01_DiagnosticLibAdapter diagnosticAdapter;
	View null_pager;
	LinearLayout mGallery,mGallery1;
	String[] mImgIds;
	String[] codesubname;
	String[] diccode;
	private String[] cyclecode;
	ImageView img_search,sortimg;
	TextView sort,cycle,text_shot;

	void initView()
	{
		final View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);

		mGallery = (LinearLayout) findViewById(R.id.id_gallery);
		mGallery1 = (LinearLayout) findViewById(R.id.subcrop);

		findViewById(R.id.img_add).setOnClickListener(this);
		img_search = (ImageView)findViewById(R.id.img_search) ;
		img_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(C01_DiagnosticLibActivity.this,B01_SearchActivity.class);
				startActivity(intent);
			}
		});
		sort = (TextView)findViewById(R.id.sort);
		sort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupWindow == null) {
					sort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_pack), null);
					showSelectDialog(v);
				}

			}
		});
		cycle = (TextView)findViewById(R.id.cycle) ;
		cycle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(popupWindow1 == null) {
					cycle.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_pack), null);
					showQuestionDialog(v);
				}
			}
		});

		text_shot = (TextView)findViewById(R.id.text_shot);
		listView = (XListView)findViewById(R.id.list_black);
		listView.setXListViewListener(this, 1);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);


	}

	String  codetype = "";
	private void initView1()// 填充数据
	{
		int left, top, right, bottom;
		left = top = right = bottom = 20;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
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
						final ViewGroup.LayoutParams lp =  v.getLayoutParams();
						lp.width = lp.WRAP_CONTENT;
						lp.height=lp.WRAP_CONTENT;
						int left1, top1, right1, bottom1;
						left1 = top1 = right1 = bottom1 = 20;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(left1, top1, right1, bottom1);
						v.setLayoutParams(lp);
						v.setBackground(getResources().getDrawable(R.drawable.watermellon));
						textView.setTextColor(getResources().getColor(R.color.green));
						m=(Integer) textView.getTag();
						selectTab(m);
						codetype = mImgIds[m];
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
				child.setTextColor(getResources().getColor(R.color.black));
				child.setBackgroundResource(0);
			}

		}

	}

	private void initView2()// 填充数据
	{
		int left, top, right, bottom;
		left = top = right = bottom = 10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
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
						code_type = cyclecode[m];
						diagnosticAdapter = null;
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
				child.setTextColor(getResources().getColor(R.color.black));
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
				diagnosticAdapter = null;
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
				diagnosticAdapter = null;
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
				diagnosticAdapter = null;
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

		popupWindow.showAsDropDown(v,-120,-15);

	}

	PopupWindow popupWindow1;

	private void showQuestionDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.c01_expert_list_dialog, null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_question_type);
		for (int i=0;i<cropCycleModel.data.crop_cycle.size();i++) {
			View item = LayoutInflater.from(this).inflate(R.layout.c01_expert_list_question_type, null);
			TextView textview = (TextView)item.findViewById(R.id.text_name);
			textview.setText(cropCycleModel.data.crop_cycle.get(i).dicname);
			if (i == cropCycleModel.data.crop_cycle.size()-1)
				item.findViewById(R.id.line).setVisibility(View.GONE);
			layout.addView(item);
			final int finalI = i;
			textview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					diagnosticAdapter = null;
					code_type = cropCycleModel.data.crop_cycle.get(finalI).cycleType;
					requestData(true);
					popupWindow1.dismiss();
				}
			});
		}

		popupWindow1 = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

		popupWindow1.setTouchable(true);
		popupWindow1.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				//这里如果返回true的话，touch事件将被拦截
				//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});
		//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
		popupWindow1.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

		popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow1 = null;
				cycle.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.v_icon_open), null);
			}
		});

		popupWindow1.showAsDropDown(v,8,-15);

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
					intent = new Intent(C01_DiagnosticLibActivity.this, ChooseCropActivity.class);
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
	//	updateData();
	}

	void updateData()
	{
		updateDiagnostic();
	}

	private void updateDiagnostic() {
		int size = diagnosticModel.data.diagnostics.size();
		if (size > 0) {
			null_pager.setVisibility(View.GONE);
			if (diagnosticAdapter == null) {
				diagnosticAdapter = new C01_DiagnosticLibAdapter(this ,diagnosticModel.data.diagnostics);
				listView.setAdapter(diagnosticAdapter);

			} else {
				diagnosticAdapter.notifyDataSetChanged();
			}
		}else {
			diagnosticAdapter = null;
			null_pager.setVisibility(View.VISIBLE);
		}
	}

	String  crop_type = "";
	String  type_code = "";
	String code_type = "";
	int textsort = 0;
	int page = 1;
	public void requestData(boolean bShow) {
		request.crop_type = crop_type;
		request.cycle_code = code_type;
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(this);
		request.sort = textsort;
		request.page = page;
		request.provice = AppUtils.getCurrProvince(this);
		diagnosticModel.getDiagnosticList(request,bShow);
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

			Intent intent = new Intent(C01_DiagnosticLibActivity.this,ChooseCropActivity.class);
			ArrayList<String> attentname = new ArrayList<>();
			ArrayList<String> attentcode = new ArrayList<>();
			intent.putStringArrayListExtra("attentname",attentname);
			intent.putStringArrayListExtra("attentcode",attentcode);
			startActivity(intent);
		}
	}

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
			diagnosticAdapter = null;
//			diagnosticModel.getDiagnosticList(info_from,crop_type,code_type,true);
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
		if(url.contains(ApiInterface.DIAGNOSTICLIST)){
			updateData();
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