package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_RecommendArticleAdapter;
import com.dmy.farming.adapter.MyFragmentPagerAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.ARTICLELIST;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.model.ArticleDetailModel;
import com.dmy.farming.fragment.C01_FarmNewsFragment;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class C01_FarmNewsItemDetailActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {

	MainActivity mActivity;
	ArticleDetailModel articleDetailModel;
	XListView list_black;
	String id ;
	String iscollection  = "0";
	ImageView collectionimg;
	C01_RecommendArticleAdapter listAdapter;
	View layout_recommend;
	WebView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_farmnews_item_detail);

		id = getIntent().getStringExtra("id");
		articleDetailModel = new ArticleDetailModel(this,id);
		articleDetailModel.addResponseListener(this);
		initView();

	}

	TextView  title, infofrom, time;

	WebSettings settings;
	View more;
	void initView() {
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		title = (TextView)findViewById(R.id.title);
		infofrom = (TextView)findViewById(R.id.infofrom);
		time = (TextView)findViewById(R.id.time);
		content = (WebView)findViewById(R.id.content);
		settings = content.getSettings();
		settings.setSupportZoom(true);
		View more = findViewById(R.id.more);
		more.setOnClickListener(this);

		layout_recommend = findViewById(R.id.layout_recommend);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.more:
				showSelectDialog(v);
				break;
		}
	}

	int cur_tab = -1;

	void clickTab(int tab_index) {
		if (tab_index != cur_tab) {
			cur_tab = tab_index;
			//tabSelected(cur_tab);
		}
	}
	View view;
	int textsize = 18;
	int fontSize = 3;
	private void showSelectDialog(View view1) {
		//自定义布局，显示内容
		view  = LayoutInflater.from(this).inflate(R.layout.d00_fram_news_share, null);
		collectionimg = (ImageView)view.findViewById(R.id.collectionimg);
		if (iscollection.equals("1")){
			collectionimg.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
		}
		else{
			collectionimg.setBackground(getResources().getDrawable(R.drawable.ndetail_icon_collection));
		}
		collectionimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkLogined() && id.equals(articleDetailModel.id)){
					if(iscollection.equals("1")){
						articleDetailModel.cancelcollection(id);
					}else{
						articleDetailModel.collection(id);
					}
				}
			}
		});
		View size = view.findViewById(R.id.size);
		size.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fontSize++;
				if (fontSize < 0) {
					fontSize = 1;
				}
				switch (fontSize) {

					case 1:
						settings.setTextSize(WebSettings.TextSize.SMALLEST);
						break;
					case 2:
						settings.setTextSize(WebSettings.TextSize.SMALLER);
						break;
					case 3:
						settings.setTextSize(WebSettings.TextSize.NORMAL);
						break;
					case 4:
						settings.setTextSize(WebSettings.TextSize.LARGER);
						break;
					case 5:
						settings.setTextSize(WebSettings.TextSize.LARGEST);
						break;
				}
				//content.setTextSize(++textsize);
			}
		});

		PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

		window.setTouchable(true);
		window.setTouchInterceptor(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				//这里如果返回true的话，touch事件将被拦截
				//拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		//（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
		window.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.color_trans));

		window.showAsDropDown(view1);

	}

	@Override
	protected void onResume() {
		requestData();
		super.onResume();
	}

	void requestData() {
		articleDetailModel .getRoute(AppConst.info_from, AppUtils.getCurrCity(this));
//		groupListModel.getGroupList(request, false);
	}


	void updateData() {
		if (id.equals(articleDetailModel.id)) {
			ARTICLELIST route = articleDetailModel.data;
			title.setText(route.title);
			time.setText(route.publish_time);
		//	content.setText(route.content);
			String c = route.content.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
		//	content = route.content.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
			content.loadDataWithBaseURL(null, c, "text/html", "utf-8", null);
			infofrom.setText(route.info_from);
			iscollection=route.iscollection;

			//  img.set
		}

		updateRecArticle();

	}

	private void updateRecArticle() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (articleDetailModel.dataList.size() > 0)
		{
			layout_recommend.setVisibility(View.VISIBLE);
			if (listAdapter == null) {
				listAdapter = new C01_RecommendArticleAdapter(this, articleDetailModel.dataList);
				list_black.setAdapter(listAdapter);

			} else {
				listAdapter.notifyDataSetChanged();
			}
//			if (0 == articleDetailModel.data.paginated.more) {
//				list_black.setPullLoadEnable(false);
//			} else {
//				list_black.setPullLoadEnable(true);
//			}
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			layout_recommend.setVisibility(View.GONE);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.ARTICLEDETAILE)) {
			if (articleDetailModel.lastStatus.error_code==200) {
				updateData();
			} else {
				errorMsg(articleDetailModel.lastStatus.error_desc);
			}
		}else if(url.contains(ApiInterface.COLLECTION)){
			errorMsg("收藏成功");
			collectionimg.setBackground(getResources().getDrawable(R.drawable.v_icon_collection_h));
			iscollection = "1";
		}else if(url.contains(ApiInterface.CANCELCOLLECTION)){
			errorMsg("取消收藏成功");
			collectionimg.setBackground(getResources().getDrawable(R.drawable.v_icon_collection));
			/*requestData();
			listAdapter = null;*/
			iscollection = "0";
		}
	}
	private void InitViewPager() {
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