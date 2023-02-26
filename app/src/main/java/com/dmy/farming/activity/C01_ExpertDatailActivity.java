package com.dmy.farming.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.QUESTIONLIST;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.expertRequest;
import com.dmy.farming.api.model.ExpertDetailModel;
import com.dmy.farming.api.model.QuestionListModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_ExpertDatailActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {

	ExpertDetailModel expertDetailModel;
	QuestionListModel questionListModel;
	String id;
	expertRequest request;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_expert_detail);

		id = getIntent().getStringExtra("id");

		initView();

		expertDetailModel = new ExpertDetailModel(this,id);
		expertDetailModel.addResponseListener(this);
		expertDetailModel.getExpertDetail(id);
		questionListModel = new QuestionListModel(this,0);
		questionListModel.addResponseListener(this);
		request  = new expertRequest();


		clickTab(0);
	}

	XListView list_black;
	View null_pager,line1,line2,expert1,expert2;
	TextView home,answer,text_name,text_answer_num,text_adopt_num,text_job_title,text_position,text_history_answer_num,text_video,text_online;
	ListView list_answer;
	C01_ExpertDeatilAnswerAdapter listAdapter;
    LinearLayout layout_goodcrop;
	ImageView img_head,img_level,collection;
	WebView web_introduction,web_achievement;
	ImageLoader imageLoader = ImageLoader.getInstance();
	int iscollection  = 0;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		home = (TextView) findViewById(R.id.home);
		home.setOnClickListener(this);
		answer = (TextView)findViewById(R.id.answer);
		answer.setOnClickListener(this);
		line1 = findViewById(R.id.line1);
		line2 = findViewById(R.id.line2);
		expert1 = findViewById(R.id.expert1);
		expert2 = findViewById(R.id.expert2);

		null_pager = findViewById(R.id.null_pager);

		img_head = (ImageView) findViewById(R.id.img_head);
		img_level = (ImageView) findViewById(R.id.img_level);
		collection = (ImageView)findViewById(R.id.collection);
		collection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(id.equals(SESSION.getInstance().uid)){
					errorMsg("您不能收藏自己");
				}else{
					if(iscollection==1){
						expertDetailModel.cancelcollection(id);
					}else{
						expertDetailModel.collection(id);
					}
				}
			}
		});

		text_name = (TextView) findViewById(R.id.text_name);
		text_answer_num = (TextView) findViewById(R.id.text_answer_num);
		text_adopt_num = (TextView) findViewById(R.id.text_adopt_num);
		text_job_title = (TextView) findViewById(R.id.text_job_title);
		text_position = (TextView) findViewById(R.id.text_position);
		text_video = (TextView) findViewById(R.id.text_video);
		text_video.setOnClickListener(this);
		text_online = (TextView) findViewById(R.id.text_online);
		text_online.setOnClickListener(this);

		// 主页
		web_introduction = (WebView) expert1.findViewById(R.id.web_introduction);
		web_achievement = (WebView) expert1.findViewById(R.id.web_achievement);
		layout_goodcrop = (LinearLayout) expert1.findViewById(R.id.layout_goodcrop);

		// 历史解答
		text_history_answer_num = (TextView) findViewById(R.id.text_history_answer_num);

		list_black = (XListView) expert2.findViewById(R.id.list_black);

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
			case R.id.answer:
				request.expert_id = id;
				request.info_from = AppConst.info_from;
				questionListModel.API_getMysolved(request,true);
				clickTab(1);
				break;
			case R.id.home:
				clickTab(0);
				break;
			case R.id.text_video:
				if (checkLogined()) {

				}
				break;
			case R.id.text_online:
				if (checkLogined()) {

				}
				break;
		}
	}

	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
		}
	}

	void updateBottomLine() {

		if (cur_tab == 0) {
			home.setTextColor(getResources().getColor(R.color.green));
			line1.setVisibility(View.VISIBLE);
			answer.setTextColor(getResources().getColor(R.color.black));
			line2.setVisibility(View.GONE);
			expert1.setVisibility(View.VISIBLE);
			expert2.setVisibility(View.INVISIBLE);

		}
		else if (cur_tab == 1) {
			answer.setTextColor(getResources().getColor(R.color.green));
			line2.setVisibility(View.VISIBLE);
			home.setTextColor(getResources().getColor(R.color.black));
			line1.setVisibility(View.GONE);
			expert2.setVisibility(View.VISIBLE);
			expert1.setVisibility(View.GONE);
		}


	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void updateData() {
		if (expertDetailModel.data != null){
            imageLoader.displayImage(expertDetailModel.data.img_url,img_head, FarmingApp.options_head);
			String level = expertDetailModel.data.levels;
			if(level.equals("0")){
				img_level.setBackgroundResource(R.drawable.n_icon_tag1);
			}else if (level.equals("1")){
				img_level.setBackgroundResource(R.drawable.n_icon_tag2);
			}else if (level.equals("2")){
				img_level.setBackgroundResource(R.drawable.n_icon_tag3);
			}else if (level.equals("3")){
				img_level.setBackgroundResource(R.drawable.n_icon_tag4);
			}
			text_name.setText(expertDetailModel.data.nick_name);
			text_answer_num.setText(expertDetailModel.data.aswner_num);
			text_adopt_num.setText(expertDetailModel.data.accept_num);
			text_job_title.setText(expertDetailModel.data.jobTitle);
			iscollection = expertDetailModel.data.iscollection;
			text_position.setText(expertDetailModel.data.province+expertDetailModel.data.city+expertDetailModel.data.district);
			if (cur_tab == 0){
				WebSettings webSettings = web_introduction.getSettings();
				// User settings
				webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
				webSettings.setUseWideViewPort(true);//关键点
				webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
				webSettings.setDisplayZoomControls(false);
				webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
				webSettings.setAllowFileAccess(true); // 允许访问文件
				webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
				webSettings.setSupportZoom(true); // 支持缩放
				webSettings.setLoadWithOverviewMode(true);
				webSettings.setTextZoom(250);
				web_introduction.loadDataWithBaseURL(null,expertDetailModel.data.details,"text/html","utf-8",null);

				WebSettings webSetting = web_achievement.getSettings();
				// User settings
				webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
				webSetting.setUseWideViewPort(true);//关键点
				webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
				webSetting.setDisplayZoomControls(false);
				webSetting.setJavaScriptEnabled(true); // 设置支持javascript脚本
				webSetting.setAllowFileAccess(true); // 允许访问文件
				webSetting.setBuiltInZoomControls(false); // 设置显示缩放按钮
				webSetting.setSupportZoom(true); // 支持缩放
				webSetting.setLoadWithOverviewMode(true);
				webSetting.setTextZoom(250);
				web_achievement.loadDataWithBaseURL(null,expertDetailModel.data.achievementPersonal,"text/html","utf-8",null);

				layout_goodcrop.removeAllViews();
                for (int i=0;i<expertDetailModel.data.goodcrop.size();i++){
					View view = LayoutInflater.from(this).inflate(R.layout.c01_expert_detail_goodcrop,null);
					((TextView)view.findViewById(R.id.text_name)).setText(expertDetailModel.data.goodcrop.get(i).name);
					layout_goodcrop.addView(view);
				}

			}else if (cur_tab == 1){
				updateAnswer();
			}
			if (expertDetailModel.data.iscollection==1){
				collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
			}
			else{
				collection.setBackground(getResources().getDrawable(R.drawable.ndetail_icon_collection));
			}
		}
	}

	private void updateAnswer() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (questionListModel.data.questions.size() > 0)
		{
			text_history_answer_num.setText("（" + questionListModel.data.questions.size() +"）");
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new C01_ExpertDeatilAnswerAdapter(this,questionListModel.data.questions);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
			/*if (0 == rentModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}*/
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	class C01_ExpertDeatilAnswerAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<QUESTIONLIST> dataList;

		public C01_ExpertDeatilAnswerAdapter(Context context,ArrayList<QUESTIONLIST> dataList) {
			this.mContext = context;
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public QUESTIONLIST getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		ImageLoader imageLoader = ImageLoader.getInstance();

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_expert_answer_detail_list_item, null);
			viewHolder.layout_1 = convertView.findViewById(R.id.layout_1);
			viewHolder.img_head = (ImageView) convertView.findViewById(R.id.img_head);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.nickname = (TextView) convertView.findViewById(R.id.nickname);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.position = (TextView) convertView.findViewById(R.id.position);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.keyword = (TextView) convertView.findViewById(R.id.keyword);

			convertView.setTag(viewHolder);
			} else {
			viewHolder =(ViewHolder) convertView.getTag();
			}

		final QUESTIONLIST item = getItem(position);
		if (item != null)
		{
			viewHolder.nickname.setText(item.userName);
			imageLoader.displayImage(item.headurl, viewHolder.img_head, FarmingApp.options_head);
			if (TextUtils.isEmpty(item.imgUrl))
				viewHolder.img.setVisibility(View.GONE);
			else {
				viewHolder.img.setVisibility(View.VISIBLE);
				if (item.imgUrl.contains(","))
					imageLoader.displayImage(item.imgUrl.substring(0,item.imgUrl.indexOf(",")),viewHolder.img, FarmingApp.options);
				else
					imageLoader.displayImage(item.imgUrl, viewHolder.img, FarmingApp.options);
			}
			viewHolder.time.setText(AppUtils.time(item.createTime));
			viewHolder.position.setText(item.city + item.district);
			if(item.content.length()>14){
				viewHolder.content.setText(item.content.substring(0,14)+"...");
			}else{
				viewHolder.content.setText(item.content);
			}
			viewHolder.keyword.setText(item.problemType);
			viewHolder.layout_1.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					if (item != null)
					{
						Intent intent = new Intent(mContext, D01_QuestionDetailActivity.class);
						intent.putExtra("id", item.faq_id);
						intent.putExtra("type","question");
						intent.putExtra("answer", false);
						mContext.startActivity(intent);
					}
				}

			});
		}
			return convertView;
		}

		class ViewHolder {
			ImageView img_head,img;
			TextView nickname,time,position,content,keyword;
			View layout_1;
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.EXPERTDETAIL)) {
			updateData();
		}else if(url.contains(ApiInterface.COLLECTION)){
			errorMsg("收藏成功");
			collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
			iscollection = 1;
//			expertDetailModel.getExpertDetail(id);
//			listAdapter=null;
		}
		else if(url.contains(ApiInterface.CANCELCOLLECTION)){
			errorMsg("取消收藏成功");
			collection.setBackground(getResources().getDrawable(R.drawable.v_icon_collection));
			iscollection = 0;
//			expertDetailModel.getExpertDetail(id);
//			listAdapter=null;
		}else if(url.contains(ApiInterface.EXPERTSOLVED)){
			updateAnswer();
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