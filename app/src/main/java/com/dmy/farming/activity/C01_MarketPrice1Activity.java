package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_MarketPrice1ListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.MarkerPriceRequest;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.PRICELIST;
import com.dmy.farming.api.model.MarketPriceModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONObject;

public class C01_MarketPrice1Activity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener {

	TextView text_tab_0, text_tab_1;
	View img_line_0, img_line_1;
	XListView list_price;
	MarketPriceModel listModel_0;
	MarketPriceModel listModel_1;
	View null_pager;
	PRICELIST data;
	LinearLayout expert_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c01_market_price1);

		data = (PRICELIST)getIntent().getSerializableExtra("price_data");

		listModel_0 = new MarketPriceModel(this, 2);
		listModel_1 = new MarketPriceModel(this, 3);
		listModel_0.addResponseListener(this);
		listModel_1.addResponseListener(this);
		markerPriceRequest = new MarkerPriceRequest();
		initView();


	}


	C01_MarketPrice1ListAdapter listAdapter0;
	C01_MarketPrice1ListAdapter listAdapter1;
	MarkerPriceRequest markerPriceRequest;


	void initView() {
		View img_back = findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

//		listAdapter0 = new C01_MarketPrice1ListAdapter(this);

		findViewById(R.id.layout_publish_price).setOnClickListener(this);

		text_tab_0 = (TextView) findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView) findViewById(R.id.text_tab_1);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		img_line_0 = findViewById(R.id.img_line_0);
		img_line_1 = findViewById(R.id.img_line_1);

		null_pager = findViewById(R.id.null_pager);
		list_price = (XListView) findViewById(R.id.list_price);
		list_price.setPullLoadEnable(false);
		list_price.setPullRefreshEnable(true);
		list_price.setXListViewListener(this,0);

		clickTab(0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				clickTab(0);
				break;
			case R.id.text_tab_1:
				clickTab(1);
				break;
			case R.id.layout_publish_price:
				if (checkLogined()) {
					if ("".equals(SESSION.getInstance().sid) || SESSION.getInstance().sid == null) {
						startActivity(new Intent(this, A02_ThirdSignupActivity.class));
						return;
					}
					Intent intent = new Intent(C01_MarketPrice1Activity.this, PricePublishActivity.class);
					startActivityForResult(intent, 1);
				}
				break;
		}
	}

	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			if (cur_tab == 0) {
				img_line_0.setVisibility(View.VISIBLE);
				img_line_1.setVisibility(View.INVISIBLE);
			} else {
				img_line_1.setVisibility(View.VISIBLE);
				img_line_0.setVisibility(View.INVISIBLE);
			}

			updateData();
			requestData();
		}
	}

	int page = 1;
	void requestData() {
		if (!AppUtils.isLogin(this))
			return;
		if (cur_tab == 0) {
			listAdapter0=null;
			markerPriceRequest.info_from = AppConst.info_from;
			markerPriceRequest.crop_type = data.crop_code;
			markerPriceRequest.provience = data.provience;
			markerPriceRequest.city = data.city;
			markerPriceRequest.district = data.district;
			page= 1;
			markerPriceRequest.page = page;
			markerPriceRequest.this_app = 1;
			markerPriceRequest.type = "0";
			listModel_0.getpriceList(markerPriceRequest);
		} else {
			page= 1;
			listAdapter1 =null;
			markerPriceRequest.info_from = AppConst.info_from;
			markerPriceRequest.crop_type = data.crop_code;
			markerPriceRequest.provience = data.provience;
			markerPriceRequest.city = data.city;
			markerPriceRequest.district = data.district;
			markerPriceRequest.page = page;
			markerPriceRequest.this_app = 1;
			markerPriceRequest.type = "1";
			listModel_1.getpriceList(markerPriceRequest);
		}
	}
	void updateData() {

		list_price.stopRefresh();
		list_price.stopLoadMore();

		if (cur_tab == 0)
		{
			if(listModel_0.data.prices.size() > 0){
				null_pager.setVisibility(View.GONE);
				if (listAdapter0 ==  null) {
					listAdapter0 = new C01_MarketPrice1ListAdapter(this, listModel_0.type, listModel_0.data.prices);
					list_price.setAdapter(listAdapter0);
				} else if (((HeaderViewListAdapter)list_price.getAdapter()).getWrappedAdapter() != listAdapter0) {
					list_price.setAdapter(listAdapter0);
				} else {
					listAdapter0.notifyDataSetChanged();
				}
				if (0 == listModel_0.data.paginated.more) {
					list_price.setPullLoadEnable(false);
				} else {
					list_price.setPullLoadEnable(true);
				}
			}else {
				listAdapter0 = null;
				list_price.setAdapter(null);
				null_pager.setVisibility(View.VISIBLE);
			}
		}
		else {
			if(listModel_1.data.prices.size() > 0){
				null_pager.setVisibility(View.GONE);
				if (listAdapter1 ==  null) {
					listAdapter1 = new C01_MarketPrice1ListAdapter(this, listModel_1.type, listModel_1.data.prices);
					list_price.setAdapter(listAdapter1);
				} else if (((HeaderViewListAdapter)list_price.getAdapter()).getWrappedAdapter() != listAdapter1) {
					list_price.setAdapter(listAdapter1);
				} else {
					listAdapter1.notifyDataSetChanged();
				}
				if (0 == listModel_1.data.paginated.more) {
					list_price.setPullLoadEnable(false);
				} else {
					list_price.setPullLoadEnable(true);
				}
			}else {
				listAdapter1 = null;
				list_price.setAdapter(null);
				null_pager.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			requestData();
			updateData();
		}
	}


	@Override
	public void onRefresh(int id) {
		requestData();
	}

	@Override
	public void onLoadMore(int id) {
		if (cur_tab == 0) {
			markerPriceRequest.info_from = AppConst.info_from;
			markerPriceRequest.crop_type = data.crop_type;
			markerPriceRequest.provience = data.provience;
			markerPriceRequest.city = data.city;
			markerPriceRequest.district = data.district;
			page =page+1;
			markerPriceRequest.page =page;
			markerPriceRequest.this_app =1;
			markerPriceRequest.type = "0";
			listModel_0.getpriceListMore(markerPriceRequest,false);
		} else {
			markerPriceRequest.info_from = AppConst.info_from;
			markerPriceRequest.crop_type = data.crop_type;
			markerPriceRequest.provience = data.provience;
			markerPriceRequest.city = data.city;
			markerPriceRequest.district = data.district;
			page =page+1;
			markerPriceRequest.page = page;
			markerPriceRequest.this_app = 1;
			markerPriceRequest.type = "1";
			listModel_1.getpriceListMore(markerPriceRequest,false);
		}
	}

	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		if (url.endsWith(ApiInterface.PRICE)) {
			updateData();
		}
	}

}