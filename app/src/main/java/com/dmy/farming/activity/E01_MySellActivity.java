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
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.SALELIST;
import com.dmy.farming.api.model.SaleModel;
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

public class E01_MySellActivity extends BaseActivity implements OnClickListener, BusinessResponse,XListView.IXListViewListener {

	SaleModel saleModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_sell);

		initView();

		saleModel = new SaleModel(this);
		saleModel.addResponseListener(this);

	}

	XListView list_black;
	View null_pager;
	CommonAdapter<SALELIST> commonAdapter;
	List<SALELIST> mDatas = new ArrayList<>();
	ImageLoader imageLoader = ImageLoader.getInstance();
	int pos;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		null_pager = findViewById(R.id.null_pager);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setXListViewListener(this, 1);
		list_black.setPullRefreshEnable(true);
		list_black.setPullLoadEnable(false);



	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (saleModel.data.mySale.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (commonAdapter == null) {
				commonAdapter = new CommonAdapter<SALELIST>(this, mDatas, R.layout.e01_my_sell_item) {
					@Override
					public void convert(final ViewHolder holder, final SALELIST sale, final int position) {
						//((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
						holder.setText(R.id.saletype, sale.sale_type);
						holder.setText(R.id.title, sale.title);
						if (sale.imgUrl.contains(","))
							imageLoader.displayImage(sale.imgUrl.substring(0,sale.imgUrl.indexOf(",")),(ImageView)holder.getView(R.id.img), FarmingApp.options);
						else
							imageLoader.displayImage(sale.imgUrl,(ImageView)holder.getView(R.id.img), FarmingApp.options);
						holder.setText(R.id.price, sale.price);
						holder.setText(R.id.unit, sale.unit);
						holder.setText(R.id.distance, sale.distance);
						holder.setText(R.id.position, sale.provience+sale.city+sale.district);
						holder.setText(R.id.time, AppUtils.time(sale.publishTime));

						holder.setOnClickListener(R.id.phone, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								call(sale.linkPhone);
							}
						});

						holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(E01_MySellActivity.this, C01_SellDetailActivity.class);
								intent.putExtra("id", sale.id);
								intent.putExtra("type", "mine");
								startActivity(intent);
							}
						});

						holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
								((SwipeMenuLayout) holder.getConvertView()).quickClose();
								if (sale.deleted == 0) {
									saleModel.mySaleDelete(AppConst.info_from,sale.id,"1",false);
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
			if (0 == saleModel.paginated.more) {
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
int page = 1;
	@Override
	protected void onResume() {
		super.onResume();
		page = 1;
		saleModel.getMySaleList("1",1,"1", SESSION.getInstance().sid,true);
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
		if(url.contains(ApiInterface.MYSALE)) {
			list_black.setRefreshTime();
			mDatas.clear();
			mDatas.addAll(saleModel.data.mySale);
			updateData();
		}else if (url.contains(ApiInterface.MYDELETE)){
			if (saleModel.lastStatus.error_code==200){
				errorMsg("删除成功");
//				mDatas.remove(pos);
				mDatas.get(pos).deleted = 1;
				commonAdapter.notifyDataSetChanged();
			}else
				errorMsg(saleModel.lastStatus.error_desc);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		saleModel.removeResponseListener(this);
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
		saleModel.getMySaleListmore("1",1,"1", SESSION.getInstance().sid,true);
	}
}