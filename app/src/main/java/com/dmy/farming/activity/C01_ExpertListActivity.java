package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.ADVER;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.EXPERTINFO;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.ExpertModel;
import com.dmy.farming.protocol.PHOTO;
import com.dmy.farming.view.B01_Home_Banner;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_ExpertListActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

	ExpertModel expertModel;
	DictionaryModel dictionaryModel;
	String goodposition = "";
	int page = 1;
	ArrayList<DICTIONARY> question_type = new ArrayList<DICTIONARY>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_expert_list);

		initView();

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);
		dictionaryModel.questionType();

		expertModel = new ExpertModel(this);
		expertModel.addResponseListener(this);
		requestData();

	}

	void requestData(){
		expertModel.getExpertList(AppConst.info_from, AppUtils.getCurrProvince(this),AppUtils.getCurrCity(this),AppUtils.getCurrDistrict(this),goodposition,"",page+"",true);
	}

	XListView list_black;
	View null_pager;
	B01_Home_Banner mainBanner;
	ExpertListAdapter listAdapter;
	TextView text_question_type,text_sort;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		mainBanner = (B01_Home_Banner)findViewById(R.id.layout_banner);
		initBanner();

		text_question_type = (TextView) findViewById(R.id.text_question_type);
		text_question_type.setOnClickListener(this);
		text_sort = (TextView) findViewById(R.id.text_sort);
		text_sort.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(false);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	Intent intent;
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_question_type:
				if (question_type.size() > 0 && popupWindow == null) {
					showQuestionDialog(v);
					text_question_type.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.v_icon_back), null);
				}
				break;
			case R.id.text_sort:
				if (popupWindow1 == null) {
					showSortDialog(v);
					text_sort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_back), null);
				}
				break;

		}
	}

	PopupWindow popupWindow;
	private void showQuestionDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.c01_expert_list_dialog, null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_question_type);
		for (int i=0;i<question_type.size();i++) {
			View item = LayoutInflater.from(this).inflate(R.layout.c01_expert_list_question_type, null);
			TextView textview = (TextView)item.findViewById(R.id.text_name);
			textview.setText(question_type.get(i).name);
			if (i == question_type.size()-1)
				item.findViewById(R.id.line).setVisibility(View.GONE);
			layout.addView(item);
			final int finalI = i;
			textview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					goodposition = question_type.get(finalI).code;
					requestData();
					popupWindow.dismiss();
				}
			});
		}

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
				text_question_type.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.v_icon_open), null);
			}
		});

		popupWindow.showAsDropDown(v,-10,-30);

	}

	PopupWindow popupWindow1;
	private void showSortDialog(View v) {
		//自定义布局，显示内容
		View view = LayoutInflater.from(this).inflate(R.layout.c01_expert_list_sort_dialog, null);

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
				text_sort.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.v_icon_open), null);
			}
		});

		popupWindow1.showAsDropDown(v,-10,-30);

	}

	class ViewHolder {
		Button button;
		TextView text_name,text_status,text_job_title,text_keyword;
        ImageView img_head,img_level,img_status;
		View layout;
		LinearLayout layout_goodcrop;
	}

	class ExpertListAdapter extends BaseAdapter {

		ArrayList<EXPERTINFO> dataList;
		Context mContext;
		ImageLoader imageLoader = ImageLoader.getInstance();

		public ExpertListAdapter(Context context,ArrayList<EXPERTINFO> datas) {
			mContext = context;
			dataList = datas;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public EXPERTINFO getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_list_item, null);
				viewHolder.layout = convertView.findViewById(R.id.layout);
				viewHolder.text_name = (TextView) convertView.findViewById(R.id.text_name);
				viewHolder.text_status = (TextView) convertView.findViewById(R.id.text_status);
				viewHolder.text_job_title = (TextView) convertView.findViewById(R.id.text_job_title);
				viewHolder.text_keyword = (TextView) convertView.findViewById(R.id.text_keyword);
				viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
				viewHolder.img_level = (ImageView) convertView.findViewById(R.id.img_level);
				viewHolder.img_status = (ImageView) convertView.findViewById(R.id.img_status);
				viewHolder.layout_goodcrop = (LinearLayout) convertView.findViewById(R.id.layout_goodcrop);

				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			final EXPERTINFO cat = getItem(position);
			if (cat != null)
			{
				viewHolder.text_name.setText(cat.nick_name);
				if (cat.online_status == 1) {
					viewHolder.text_status.setText("[在线]");
					viewHolder.text_status.setTextColor(getResources().getColor(R.color.online_green));
					imageLoader.displayImage(cat.img_url,viewHolder.img_head, FarmingApp.options_head);
					// 头像变亮
					ColorMatrix matrix = new ColorMatrix();
					matrix.setSaturation(1);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
					viewHolder.img_head.setColorFilter(filter);

					viewHolder.img_status.setBackgroundResource(R.drawable.video_order_online);
					viewHolder.img_status.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(C01_ExpertListActivity.this,C02_ExpertVideoActivity.class);
							startActivity(intent);
						}
					});
				} else {
					viewHolder.text_status.setText("[离线]");
					viewHolder.text_status.setTextColor(getResources().getColor(R.color.text_whitegrey));
					imageLoader.displayImage(cat.img_url,viewHolder.img_head, FarmingApp.options_head);
					// 头像变灰
					ColorMatrix matrix = new ColorMatrix();
					matrix.setSaturation(0);
					ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
					viewHolder.img_head.setColorFilter(filter);

					viewHolder.img_status.setBackgroundResource(R.drawable.video_order_offline);
				}
				viewHolder.text_job_title.setText(cat.jobTitle);

				if(cat.levels.equals("0")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag1);
				}else if (cat.levels.equals("1")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag2);
				}else if (cat.levels.equals("2")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag3);
				}else if (cat.levels.equals("3")){
					viewHolder.img_level.setBackgroundResource(R.drawable.n_icon_tag4);
				}

				viewHolder.layout_goodcrop.removeAllViews();
				for (int i=0;i<cat.goodcrop.size();i++){
					View view = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_list_goodcrop,null);
					((TextView)view.findViewById(R.id.text_name)).setText(cat.goodcrop.get(i).name);
					viewHolder.layout_goodcrop.addView(view);
				}

				viewHolder.layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						intent = new Intent(C01_ExpertListActivity.this,C01_ExpertDatailActivity.class);
						intent.putExtra("id",cat.userId);
						startActivity(intent);
					}
				});
			}

			return convertView;
		}



	}

	@Override
	protected void onResume() {
		super.onResume();
		updateData();
	}
	void updateData()
	{
		upadteBanner();
		updateExpert();
	}

	private void updateExpert() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (expertModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new ExpertListAdapter(this,expertModel.data.data);

				list_black.setAdapter(listAdapter);
			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == expertModel.data.paginated.more) {
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

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.EXPERT)) {
			updateData();
		}
		if (url.contains(ApiInterface.QUESTIONTYPE)){
			if (dictionaryModel.data.question_type.size() > 0)
			{
				question_type = dictionaryModel.data.question_type;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		expertModel.removeResponseListener(this);
	}
}