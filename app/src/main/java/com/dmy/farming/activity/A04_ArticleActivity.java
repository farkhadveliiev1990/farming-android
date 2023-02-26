package com.dmy.farming.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.bee.Utils.Utils;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.model.ServiceAgreementModel;
import com.external.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

public class A04_ArticleActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	ServiceAgreementModel dataModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a04_article);

		dataModel = new ServiceAgreementModel(this);
		dataModel.addResponseListener(this);

		initView();

		updateData();
		requestData();
	}

	WebView web_content;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		web_content = (WebView) findViewById(R.id.web_content);
 	}

	void updateData() {
		if (!TextUtils.isEmpty(dataModel.data.content)) {
			updateHtml(web_content, dataModel.data.content);
		}
	}

	boolean isSetHtml;

	void updateHtml(WebView webView, String html) {
		if (webView.getMeasuredHeight() < Utils.convertDpToPixel(this, 20))
			isSetHtml = false;

		if (!isSetHtml)
		{
			isSetHtml = true;

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

			webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
		}
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

	void requestData() {
		if (TextUtils.isEmpty(dataModel.data.content))
			dataModel.getServiceAgreement();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.endsWith(ApiInterface.USER_SERVICEAGREEMENT)) {
			if (dataModel.lastStatus.error_code == 200) {
				updateData();
			} else {
				errorMsg(dataModel.lastStatus.error_desc);
			}
		}
	}
}
