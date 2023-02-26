package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_FindHelperListAdapter;
import com.dmy.farming.adapter.E01_FindHelpDetail1ListAdapter;
import com.dmy.farming.adapter.E01_FindHelpDetail2ListAdapter;
import com.dmy.farming.adapter.E01_MyRentDetail1ListAdapter;
import com.dmy.farming.adapter.E01_MyRentDetailListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.FINDHELPLIST;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.FindHelpModel;
import com.dmy.farming.api.model.RentModel;
import com.dmy.farming.api.publishRentRequest;
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

public class E01_FindHelpActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener  {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_find_help);

		findhelpModel = new FindHelpModel(this);
		findhelpModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		request = new publishRentRequest();
		initView();

	}

	XListView list_black;
	View null_pager,img_line_0,img_line_1,img_line_2,expert1,expert2;
	TextView text_tab_0,text_tab_1, text_tab_2;
	FindHelpModel findhelpModel;
	C01_FindHelperListAdapter listAdapter;
	CommonAdapter<FINDHELPLIST> commonAdapter;
	int pos;
	List<FINDHELPLIST> mDatas = new ArrayList<>();
	ImageLoader imageLoader = ImageLoader.getInstance();
	publishRentRequest request;
	private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] diccode;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);
		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView)findViewById(R.id.text_tab_1);
		text_tab_2 = (TextView)findViewById(R.id.text_tab_2);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		text_tab_2.setOnClickListener(this);

		img_line_0 = findViewById(R.id.img_line_0);
		img_line_1 = findViewById(R.id.img_line_1);
		img_line_2 = findViewById(R.id.img_line_2);

		list_black = (XListView)findViewById(R.id.list_black);

		mGallery = (LinearLayout)findViewById(R.id.id_gallery);
		null_pager = findViewById(R.id.null_pager);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
		clickTab(0);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				cur_tab = -1;
				type_code = "0";
				clickTab(0);
				commonAdapter= null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.GONE);
				break;
			case R.id.text_tab_1:
				cur_tab = -1;
				clickTab(1);
				commonAdapter= null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
			case R.id.text_tab_2:
				cur_tab = -1;
				clickTab(2);
				commonAdapter= null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
		}
	}

	String type_code = "0";
	String provience = "1";
	String city = "1";
	String district = "1";
	float lon = 1;
	float lat = 1;
	int page = 1;
	String this_app = "1";
	DictionaryModel dictionaryModel;

	@Override
	protected void onResume() {
		requestData(true);
		super.onResume();
	}

	public void requestData(boolean bShow)
	{
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(this);
		request.provience = AppUtils.getCurrProvince(this);
		request.district = AppUtils.getCurrDistrict(this);
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = 1;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		findhelpModel.getmyhelpList(AppConst.info_from,type_code, AppUtils.getCurrProvince(this), AppUtils.getCurrDistrict(this),  AppUtils.getCurrDistrict(this), lon, lat, page, this_app,bShow);
	}
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.FINDMYHELP)) {
			list_black.setRefreshTime();
			mDatas.clear();
			mDatas.addAll(findhelpModel.data.routes);
			updateData();
		}
		else if (url.contains(ApiInterface.MYDELETE)){
			if (findhelpModel.lastStatus.error_code==200){
				errorMsg("删除成功");
//				mDatas.remove(pos);
				mDatas.get(pos).deleted = 1;
				commonAdapter.notifyDataSetChanged();
			}else
				errorMsg(findhelpModel.lastStatus.error_desc);
		}else if (url.contains(ApiInterface.GONGQIULABEL)) {
			updatecroyData();
		}
	}

	private void initView1()// 填充数据
	{
		int left, top, right, bottom;
		left = top = right = bottom = 20;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(left, top, right, bottom);
		if(mImgIds != null) {
			for ( int i = 0; i < mImgIds.length; i++) {
				final TextView textView = new TextView(this);
				textView.setText(mImgIds[i]);
				textView.setId(i);
				textView.setTextSize(16);
				textView.setGravity(Gravity.CENTER);
				textView.setPadding(5,10,5,5);
				//  textView.setLayoutParams(params);
				if(i==0){
					textView.setBackground(getResources().getDrawable(R.drawable.watermellon));
					textView.setTextColor(getResources().getColor(R.color.green));
					type_code = diccode[i];
					requestData(true);
				}
				mGallery.addView(textView);
				textView.setOnClickListener(new View.OnClickListener(){
					@Override
					public void onClick(View v) {

						final ViewGroup.LayoutParams lp =  v.getLayoutParams();
						lp.width = lp.WRAP_CONTENT;
						lp.height=lp.WRAP_CONTENT;
						int left1, top1, right1, bottom1;
						left1 = top1 = right1 = bottom1 = 20;
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
						params.setMargins(left1, top1, right1, bottom1);
						v.setLayoutParams(lp);
						v.setBackground(getResources().getDrawable(R.drawable.watermellon));
						textView.setTextColor(getResources().getColor(R.color.green));

						int id = v.getId();
						selectTab(id);
						type_code = diccode[id];
						requestData(true);

					}
				});

			}


		}
	}


	private void selectTab(int position) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mGallery.getChildCount(); i++) {
			TextView child = (TextView) mGallery.getChildAt(i);
			if (position == i) {
				child.setTextColor(getResources().getColor(R.color.green));
			} else {
				child.setTextColor(getResources().getColor(R.color.text_blackgrey));
				child.setBackgroundResource(0);
			}

		}

	}

	int cur_tab = -1;
	void clickTab(int tab_index) {
		if (tab_index != cur_tab)
		{
			cur_tab = tab_index;
			updateBottomLine();
			//tabSelected(cur_tab);
		}
	}

	void updateBottomLine() {
		img_line_0.setVisibility(View.INVISIBLE);
		img_line_1.setVisibility(View.INVISIBLE);
		img_line_2.setVisibility(View.INVISIBLE);

		if (cur_tab == 0) {
			img_line_0.setVisibility(View.VISIBLE);
			text_tab_0.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_0.setBackgroundColor(getResources().getColor(R.color.green));

		}
		else if (cur_tab == 1) {
			img_line_1.setVisibility(View.VISIBLE);
			text_tab_1.setTextColor(getResources().getColor(R.color.green));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_1.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else if (cur_tab == 2) {
			img_line_2.setVisibility(View.VISIBLE);
			text_tab_2.setTextColor(getResources().getColor(R.color.green));
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
			img_line_2.setBackgroundColor(getResources().getColor(R.color.green));
		}
		else{
			text_tab_1.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_2.setTextColor(getResources().getColor(R.color.text_blackgrey));
			text_tab_0.setTextColor(getResources().getColor(R.color.text_blackgrey));
		}

		if (cur_tab == 0){
			requestData(true);
		}else if (cur_tab == 1){
			dictionaryModel.getPublishTypeList(AppConst.info_from,"WOSHIBANGSHOU_CODE");
			requestData(true);
		}else if (cur_tab == 2){
			dictionaryModel.getPublishTypeList(AppConst.info_from,"ZHAOBANGSHOU_CODE");
			requestData(true);
		}
	}


	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (findhelpModel.data.routes.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (commonAdapter == null) {
				commonAdapter = new CommonAdapter<FINDHELPLIST>(this, mDatas, R.layout.e01_my_sell_item) {
					@Override
					public void convert(final ViewHolder holder, final FINDHELPLIST helper, final int position) {
						//((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
						holder.setText(R.id.saletype, helper.helpType);
						holder.setText(R.id.title, helper.title);
						if (helper.imgUrl.contains(","))
							imageLoader.displayImage(helper.imgUrl.substring(0,helper.imgUrl.indexOf(",")),(ImageView)holder.getView(R.id.img), FarmingApp.options);
						else
							imageLoader.displayImage(helper.imgUrl,(ImageView)holder.getView(R.id.img), FarmingApp.options);
						holder.setText(R.id.price, helper.price);
						holder.setText(R.id.unit, helper.unit);
						holder.setText(R.id.distance, helper.distance);
						holder.setText(R.id.position, helper.addressDetails);
						holder.setText(R.id.time, AppUtils.time(helper.publishTime));

						holder.setOnClickListener(R.id.phone, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								call(helper.link_phone);
							}
						});

						holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, C01_FindHelperDetailActivity.class);
								intent.putExtra("id", helper.id);
								intent.putExtra("type", "mine");
								startActivity(intent);
							}
						});

						holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
								((SwipeMenuLayout) holder.getConvertView()).quickClose();
								if (helper.deleted == 0) {
									findhelpModel.myFindHelpDelete(AppConst.info_from,helper.id,false);
									pos = position;
								}else {
									errorMsg("信息已被删除");
								}
							}
						});
					}
				};

				list_black.setAdapter(commonAdapter);

			} else {
				commonAdapter.notifyDataSetChanged();
			}
			if (0 == findhelpModel.paginated.more) {
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

	private ArrayList<DICTIONARY> ad;
	private void updatecroyData() {
		if (dictionaryModel.data.gongqiu_label.size() > 0) {
			null_pager.setVisibility(View.GONE);
			ad =  dictionaryModel.data.gongqiu_label;
			diccode = new String[dictionaryModel.data.gongqiu_label.size()];
			if(dictionaryModel.data.gongqiu_label.size()>6){
				mImgIds = new String[6];
				for(int i = 0 ;i<6;i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}else{
				mImgIds = new String[dictionaryModel.data.gongqiu_label.size()];
				for(int i = 0 ;i<dictionaryModel.data.gongqiu_label.size();i++){
					mImgIds[i] = ad.get(i).name;
					diccode[i] = ad.get(i).code;
				}
			}
			initView1();
		} else {
			null_pager.setVisibility(View.VISIBLE);
		}
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

	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onRefresh(int id) {
		requestData(false);
	}

	@Override
	public void onLoadMore(int id) {
		page = page+1;
		request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(this);
		request.provience = AppUtils.getCurrProvince(this);
		request.district = AppUtils.getCurrDistrict(this);
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = page;
		request.this_app = "1";
		request.keyword = "";
		request.type_code = type_code;
		findhelpModel.getrentListMore(request,true);
	}



}