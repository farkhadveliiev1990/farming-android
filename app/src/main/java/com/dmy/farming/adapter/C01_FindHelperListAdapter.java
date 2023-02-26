package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_FindHelperActivity;
import com.dmy.farming.activity.C01_FindHelperDetailActivity;
import com.dmy.farming.activity.C01_RentDetailActivity;
import com.dmy.farming.api.data.FINDHELPLIST;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class C01_FindHelperListAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;
	ArrayList<FINDHELPLIST> dataList;

	public Handler parentHandler;

	public C01_FindHelperListAdapter(Context context, ArrayList<FINDHELPLIST> dataList) {
		this.mContext = context;
//		this.friends = friends;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	public FINDHELPLIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	private class ViewHolder {
		public View layout_frame,phone;
		public ImageView img_route;
		public TextView title,price,distance,address_detail,time,farm,text_unit;


	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		String pattern ="yyyy-MM-dd HH:mm:ss";
		String display = "";
		String image[];
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_sell_item, null,false);

			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			viewHolder.address_detail = (TextView) convertView.findViewById(R.id.address_detail);
			viewHolder.farm = (TextView) convertView.findViewById(R.id.farm);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.phone = convertView.findViewById(R.id.phone);
			viewHolder.text_unit = (TextView) convertView.findViewById(R.id.text_unit);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
	final 	FINDHELPLIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			viewHolder.price.setText(cat.price);
			viewHolder.text_unit.setText(cat.unit);
			viewHolder.distance.setText(cat.distance);
			viewHolder.address_detail.setText(cat.addressDetails);
			viewHolder.farm.setText(cat.helpType);
			image = cat.imgUrl.split(",");
			imageLoader.displayImage(image[0], viewHolder.img_route, FarmingApp.options_long_with_text);
			viewHolder.phone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.phone:
							call(cat.link_phone);
							break;
					}
				}
			});
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					FINDHELPLIST data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, C01_FindHelperDetailActivity.class);

						intent.putExtra("id", data.id);
						intent.putExtra("type", "findhelp");
						/*intent.putExtra("price", data.price);
						intent.putExtra("time", data.publish_time);
						intent.putExtra("address_detail",data.address_details);
						intent.putExtra("content",data.content);*/
						mContext.startActivity(intent);
					}
				}

			});
			String time = cat.publishTime;
			int tMin = 60 * 1000;
			int tHour = 60 * tMin;
			int tDay = 24 * tHour;

			if (time != null) {
				try {
					Date tDate = new SimpleDateFormat(pattern).parse(time);
					Date today = new Date();
					SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
					SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
					Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
					Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
					Date beforeYes = new Date(yesterday.getTime() - tDay);
					if (tDate != null) {
						SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日 HH:mm");
						long dTime = today.getTime() - tDate.getTime();
						if (tDate.before(thisYear)) {
							display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
						} else {
							if (dTime < tMin) {
								display = "刚刚";
							} else if (dTime < tHour) {
								display = (int) Math.ceil(dTime / tMin) + "分钟前";
							} else if (dTime < tDay && tDate.after(yesterday)) {
								display = (int) Math.ceil(dTime / tHour) + "小时前";
							} else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
								display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
							} else {
								long days = dTime / (1000 * 60 * 60 * 24);
								//display = halfDf.format(tDate);
								if(days<7){
									display = (int) Math.ceil(days) +"天前";
								} else if(days>=7 && days <28){
									display = (int) Math.ceil(days/7)+"周前";
								}else if(days>=28){
									display = (int) Math.ceil(days/30)+"个月前";
								}else{
									display = halfDf.format(tDate);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			viewHolder.time.setText(display);
		}
		return convertView;
	}

	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
}
