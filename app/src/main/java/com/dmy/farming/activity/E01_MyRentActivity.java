package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.adapter.C01_RentListAdapter;
import com.dmy.farming.adapter.E01_MyRentDetail1ListAdapter;
import com.dmy.farming.adapter.E01_MyRentDetailListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.dmy.farming.api.model.DictionaryModel;
import com.dmy.farming.api.model.MyRentListModel;
import com.dmy.farming.api.model.RentModel;
import com.dmy.farming.view.SwipeMenuLayout;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class E01_MyRentActivity extends BaseActivity implements OnClickListener, BusinessResponse, XListView.IXListViewListener{

//	MyRentListModel listModel_0,listModel_1;

	RentModel rentModel;
	DictionaryModel dictionaryModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_rent);

	//	listModel_0 = new MyRentListModel(this, 0);
		//listModel_1 = new MyRentListModel(this, 1);
	//	listModel_0.addResponseListener(this);
	//	listModel_1.addResponseListener(this);

		rentModel = new RentModel(this);
		rentModel.addResponseListener(this);
		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);


		initView();

	}

	XListView list_black;
	View null_pager,line1,line2;
	TextView text_tab_0,text_tab_1,text_tab_2,text_tab_3,text;
	ImageView img_line_0,img_line_1,img_line_2,imageView;
	C01_RentListAdapter listAdapter;
	String crop_code = "CHUZU_CODE";
	int page = 1,this_app = 1;
	int pos;
	List<RENTLIST> mDatas = new ArrayList<>();
	CommonAdapter<RENTLIST> commonAdapter;
	ImageLoader imageLoader = ImageLoader.getInstance();
	private LinearLayout mGallery,mGallery1;
	private String[] mImgIds;
	private String[] diccode;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		text_tab_0 = (TextView)findViewById(R.id.text_tab_0);
		text_tab_1 = (TextView)findViewById(R.id.text_tab_1);
		text_tab_2 = (TextView)findViewById(R.id.text_tab_2);
		text_tab_0.setOnClickListener(this);
		text_tab_1.setOnClickListener(this);
		text_tab_2.setOnClickListener(this);

		img_line_0 = (ImageView)findViewById(R.id.img_line_0);
		img_line_1 =  (ImageView)findViewById(R.id.img_line_1);
		img_line_2 =  (ImageView)findViewById(R.id.img_line_2);

		clickTab(0);

		null_pager = findViewById(R.id.null_pager);
		mGallery = (LinearLayout)findViewById(R.id.id_gallery);

		list_black = (XListView) findViewById(R.id.list_black);
		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this,0);


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

	private void initView1()// 填充数据
	{
		if(mImgIds != null || mImgIds.length != 0) {
			for ( int i = 0; i < mImgIds.length; i++) {
				/*final  View item =  LayoutInflater.from(this).inflate(R.layout.question_tab_item,null);
				final TextView text = (TextView)item.findViewById(R.id.text_tab);
				imageView = (ImageView) item.findViewById(R.id.img_line);
				imageView.setVisibility(View.GONE);
				text.setText(mImgIds[i]);
				text.setTag(i);
				text.setId(i);
				if (i == 0){
					imageView.setVisibility(View.VISIBLE);
					text.setTextColor(getResources().getColor(R.color.green));
				}else {
					imageView.setVisibility(View.GONE);
					text.setTextColor(getResources().getColor(R.color.black));
				}
				mGallery.addView(item);*/

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
				textView.setTag(i);

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
			//TextView childAt = (TextView) mGallery.getChildAt(position);
			TextView child = (TextView) mGallery.getChildAt(i);
			if (position == i) {
				child.setTextColor(getResources().getColor(R.color.green));
			} else {
				child.setTextColor(getResources().getColor(R.color.text_blackgrey));
				child.setBackgroundResource(0);
			}

		}

	}


	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (rentModel.data.data.size() > 0)
		{
			null_pager.setVisibility(View.GONE);
			if (commonAdapter == null) {
				commonAdapter = new CommonAdapter<RENTLIST>(this, mDatas, R.layout.e01_my_sell_item) {
					@Override
					public void convert(final ViewHolder holder, final RENTLIST rent, final int position) {
						//((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
						holder.setText(R.id.saletype, rent.rent_type);
						holder.setText(R.id.title, rent.title);
						if (rent.imgUrl.contains(","))
							imageLoader.displayImage(rent.imgUrl.substring(0,rent.imgUrl.indexOf(",")),(ImageView)holder.getView(R.id.img), FarmingApp.options);
						else
							imageLoader.displayImage(rent.imgUrl,(ImageView)holder.getView(R.id.img), FarmingApp.options);
						holder.setText(R.id.price, rent.price);
						holder.setText(R.id.unit, rent.unit);
						holder.setText(R.id.distance, rent.distance);
						holder.setText(R.id.position, rent.addressDetails);
						holder.setText(R.id.time, AppUtils.time(rent.publishTime));

						holder.setOnClickListener(R.id.phone, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								call(rent.link_phone);
							}
						});

						holder.setOnClickListener(R.id.layout, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext, C01_RentDetailActivity.class);
								intent.putExtra("id", rent.id);
								intent.putExtra("type", "mine");
								startActivity(intent);
							}
						});

						holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
								((SwipeMenuLayout) holder.getConvertView()).quickClose();
								if (rent.deleted == 0) {
									rentModel.myRentDelete(AppConst.info_from,rent.id,false);
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
			if (0 == rentModel.paginated.more) {
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
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_tab_0:
				type_code = "0";
				clickTab(0);
				commonAdapter = null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.GONE);
				break;
			case R.id.text_tab_1:
				clickTab(1);
				commonAdapter = null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
			case R.id.text_tab_2:
				clickTab(2);
				commonAdapter = null;
				mGallery.removeAllViews();
				mGallery.setVisibility(View.VISIBLE);
				break;
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
			dictionaryModel.getPublishTypeList(AppConst.info_from,"ZHAOZU_CODE");
//			requestData(true);
		}else if (cur_tab == 2){
			dictionaryModel.getPublishTypeList(AppConst.info_from,"CHUZU_CODE");
//			requestData(true);
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

	String  type_code="0";
	public void requestData(boolean bShow)
	{
		/*request.info_from = AppConst.info_from;
		request.city = AppUtils.getCurrCity(this);
		request.provience = AppUtils.getCurrProvince(this);
		request.district = AppUtils.getCurrDistrict(this);
		request.lon = AppUtils.getCurrLon(this);
		request.lat = AppUtils.getCurrLat(this);
		request.page = 1;
		request.this_app = "1";
		request.keyword = keyword;
		request.type_code = type_code;*/
		//rentModel.getrentList(request,bShow);
		rentModel.getMyRentList(AppConst.info_from,type_code,page,this_app, SESSION.getInstance().sid,true);
	}


	@Override
	protected void onResume() {
		super.onResume();
		rentModel.getMyRentList(AppConst.info_from,type_code,page,this_app, SESSION.getInstance().sid,true);
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
		//requestData();
		page = 1;
		rentModel.getMyRentList(AppConst.info_from,type_code,page,this_app, SESSION.getInstance().sid,true);
	}

	@Override
	public void onLoadMore(int id) {
		page = page + 1;
		rentModel.getMyRentListmore(AppConst.info_from,type_code,page,this_app, SESSION.getInstance().sid,true);
	}

	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) {
		if (url.contains(ApiInterface.MYRENT)) {
			list_black.setRefreshTime();
			mDatas.clear();
			mDatas.addAll(rentModel.data.data);
			updateData();
		}
		else if (url.contains(ApiInterface.MYDELETE)){
			if (rentModel.lastStatus.error_code==200){
				errorMsg("删除成功");
//				mDatas.remove(pos);
				mDatas.get(pos).deleted = 1;
				commonAdapter.notifyDataSetChanged();
			}else
				errorMsg(rentModel.lastStatus.error_desc);
		}
		if (url.contains(ApiInterface.GONGQIULABEL)) {
			updatecroyData();
		}
	}

}