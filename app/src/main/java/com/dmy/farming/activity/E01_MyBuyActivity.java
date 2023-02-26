package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_MyBuyListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.api.model.BuyModel;
import com.dmy.farming.view.SwipeMenuLayout;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class E01_MyBuyActivity extends BaseActivity implements OnClickListener,XListView.IXListViewListener, BusinessResponse {

    BuyModel buyModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_buy);

		initView();

		buyModel = new BuyModel(this);
		buyModel.addResponseListener(this);
	}

	XListView list_black;
	View null_pager;
	E01_MyBuyListAdapter listAdapter;
	CommonAdapter<BUYLIST> commonAdapter;
	List<BUYLIST> mDatas = new ArrayList<>();
	ImageLoader imageLoader = ImageLoader.getInstance();
	int pos;
	int page = 1;
	String this_app = "1";

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		null_pager = findViewById(R.id.null_pager);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setXListViewListener(this, 1);
		list_black.setPullRefreshEnable(true);
		list_black.setPullLoadEnable(false);

//		listAdapter = new E01_MyBuyListAdapter(this);
//		list_black.setAdapter(listAdapter);


	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (buyModel.data.myBuy.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (commonAdapter == null) {
				commonAdapter = new CommonAdapter<BUYLIST>(this, mDatas, R.layout.e01_my_sell_item) {
					@Override
					public void convert(final ViewHolder holder, final BUYLIST buy, final int position) {
						//((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
						holder.setText(R.id.saletype, buy.buy_type);
						holder.setText(R.id.title, buy.title);
						if (buy.img_url.contains(","))
							imageLoader.displayImage(buy.img_url.substring(0,buy.img_url.indexOf(",")),(ImageView)holder.getView(R.id.img), FarmingApp.options);
						else
							imageLoader.displayImage(buy.img_url,(ImageView)holder.getView(R.id.img), FarmingApp.options);
						holder.setText(R.id.price, buy.price);
						holder.setText(R.id.unit, buy.unit);
						holder.setText(R.id.distance, buy.distance);
						holder.setText(R.id.position, buy.provience+buy.city+buy.district);
						holder.setText(R.id.time, AppUtils.time(buy.publish_time));

						holder.setOnClickListener(R.id.phone, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								call(buy.link_phone);
							}
						});

						holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(E01_MyBuyActivity.this, C00_BuyDetailActivity.class);
								intent.putExtra("id", buy.id);
								intent.putExtra("type", "mine");
								startActivity(intent);
							}
						});

						holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
								((SwipeMenuLayout) holder.getConvertView()).quickClose();
								if (buy.deleted == 0) {
									buyModel.myBuyDelete(AppConst.info_from, buy.id, false);
									pos = position;
								}else {
									errorMsg("信息已被删除");
								}
//								mDatas.remove(position);
//								notifyDataSetChanged();
							}
						});
					}
				};

				list_black.setAdapter(commonAdapter);

			} else {
				commonAdapter.notifyDataSetChanged();
			}
			if (0 == buyModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
//			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
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
		//updateData();
		super.onResume();
		page = 1;
		buyModel.getMyBuyList(AppConst.info_from,page,this_app, SESSION.getInstance().sid,true);
	}

	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.MYBUY)) {
			list_black.setRefreshTime();
			mDatas.clear();
			mDatas.addAll(buyModel.data.myBuy);
			updateData();
		}else if (url.contains(ApiInterface.MYDELETE)){
			if (buyModel.lastStatus.error_code==200){
				errorMsg("删除成功");
//				mDatas.remove(pos);
				mDatas.get(pos).deleted = 1;
				commonAdapter.notifyDataSetChanged();
			}else
				errorMsg(buyModel.lastStatus.error_desc);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		buyModel.removeResponseListener(this);
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
		page = page +1;
		buyModel.getMyBuyListmore(AppConst.info_from,page,this_app, SESSION.getInstance().sid,true);
	}
}