package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.api.helpcenterRequest;
import com.dmy.farming.api.helpcenterResponse;
import com.dmy.farming.api.model.BuyDetailModel;
import com.dmy.farming.api.model.BuyResponse;
import com.dmy.farming.api.model.HelpCenterDetailModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class E01_HelpCenterDetailActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

    String textcontent/*title,time,price,address_detail,content*/;
    MainActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e01_helpcenter_detail);

        textcontent = getIntent().getStringExtra("content");
		/*title = getIntent().getStringExtra("title");
		time = getIntent().getStringExtra("time");
		price = getIntent().getStringExtra("price");
		address_detail = getIntent().getStringExtra("address_detail");
		content = getIntent().getStringExtra("content");*/

        initView();



    }

    XListView list_black;
    WebView  content;
    View img;

    void initView() {
        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        content = (WebView)findViewById(R.id.content);

        String c = textcontent.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
        //	content = route.content.replace("<img", "<img style=\"display:        ;max-width:100%;\"");
        content.loadDataWithBaseURL(null, c, "text/html", "utf-8", null);
      //  content.loadDataWithBaseURL(null,textcontent, "text/html" , "utf-8", null);

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
                finish();
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }







	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		if (url.contains(ApiInterface.USERHELPDETAIL)) {

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