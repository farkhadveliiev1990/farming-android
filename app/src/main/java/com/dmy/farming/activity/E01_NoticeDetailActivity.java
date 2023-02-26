package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.chat.NOTICELIST;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.api.model.BuyDetailModel;
import com.dmy.farming.api.model.BuyResponse;
import com.dmy.farming.api.model.NoticeDetailModel;
import com.dmy.farming.api.model.NoticeModel;
import com.dmy.farming.api.model.WarnResponse;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.Utils.Utils;
import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class E01_NoticeDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

    String id/*title,time,price,address_detail,content*/;
    MainActivity mActivity;
    NoticeDetailModel noticeModel;
    WarnResponse warnResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e01_notice_item_detail);

        id = getIntent().getStringExtra("id");
		/*title = getIntent().getStringExtra("title");
		time = getIntent().getStringExtra("time");
		price = getIntent().getStringExtra("price");
		address_detail = getIntent().getStringExtra("address_detail");
		content = getIntent().getStringExtra("content");*/

        initView();
        noticeModel = new NoticeDetailModel(this, id);
        noticeModel.addResponseListener(this);


    }

    XListView list_black;
    TextView phone, title, keyword, time, source;
    WebView content;
    ImageView img;
    View null_pager;


    LinearLayout text_keyword;
    void initView() {
        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        null_pager = findViewById(R.id.null_pager);
        null_pager.setVisibility(View.VISIBLE);

        title = (TextView) findViewById(R.id.title);
        keyword = (TextView) findViewById(R.id.keyword);
        time = (TextView) findViewById(R.id.time);
        source = (TextView) findViewById(R.id.publish_user);
        content = (WebView) findViewById(R.id.content);
     //   imageLoader = ImageLoader.getInstance();
        text_keyword = (LinearLayout)findViewById(R.id.text_keyword);

        img = (ImageView)findViewById(R.id.img);

		/*saleModel = new SaleModel(mActivity);
		saleModel.addResponseListener(this);*/

        //list_black = (XListView) findViewById(R.id.list_black);
        //list_black.setXListViewListener(this, 1);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.img_back:
                setResult(Activity.RESULT_OK);
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }


	void requestData() {
        noticeModel .getRoute();
//		groupListModel.getGroupList(request, false);
	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.NOTICEDETAIL)) {
			if (noticeModel.lastStatus.error_code == 200) {
				updateData();
			} else {
				errorMsg(noticeModel.lastStatus.error_desc);
			}
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

    ImageLoader imageLoader = ImageLoader.getInstance();
	void updateData() {

		if (id.equals(noticeModel.id)) {
            null_pager.setVisibility(View.GONE);
            NOTICELIST route = noticeModel.data;
			title.setText(route.title);
            text_keyword.removeAllViews();
            final  String[] chrstr = route.keyWord.split(",");
            if(chrstr.length>0){
                for(int i = 0;i<chrstr.length;i++){
                    final TextView textView = new TextView(this);
                    textView.setText("#"+chrstr[i]+"#   ");
                    textView.setTag(chrstr[i]);
                    text_keyword.addView(textView);
                    textView.setTextColor(getResources().getColor(R.color.green));
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(E01_NoticeDetailActivity.this,B01_SearchActivity.class);
                            intent.putExtra("keyword",v.getTag().toString());
                            startActivity(intent);
                        }
                    });
                }
            }
            time.setText(route.createTime);
            source.setText(route.publisher);
            String c = route.content.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
            updateHtml(content, c);
            imageLoader.displayImage(route.imgUrl, img, FarmingApp.options);
          //  img.set
		}else {
            null_pager.setVisibility(View.VISIBLE);
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
            webSettings.setTextZoom(300);

            webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
        }
    }


    @Override
    public void onRefresh(int id) {
        requestData();

    }

    @Override
    public void onLoadMore(int id) {

    }
}