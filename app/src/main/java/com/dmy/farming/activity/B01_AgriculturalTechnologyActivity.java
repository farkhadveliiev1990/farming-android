package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dmy.farming.R;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;

public class B01_AgriculturalTechnologyActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b01_agricultural_technology);

		initView();
		updateData();
	}

	XListView list_black;
	View null_pager;
	TextView home;
	WebView webView;
	String title,content;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		content = intent.getStringExtra("content");

		null_pager = findViewById(R.id.null_pager);
		webView = (WebView) findViewById(R.id.web);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}


	void updateData() {
		WebSettings webSettings = webView.getSettings();
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

//		webView.loadDataWithBaseURL(null,content,"text/html","utf-8",null);
		webView.loadUrl(content);
		//覆盖WebView默认通过第三方或者是系统浏览器打开网页的行为，使得网页可以在WebView中打开
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//返回值是true的时候是控制网页在WebView中去打开，如果为false调用系统浏览器或第三方浏览器打开
				view.loadUrl(url);
				return true;
			}
			//WebViewClient帮助WebView去处理一些页面控制和请求通知
		});
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


}