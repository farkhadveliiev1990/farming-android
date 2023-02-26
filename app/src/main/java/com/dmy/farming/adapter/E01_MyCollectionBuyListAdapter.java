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
import com.dmy.farming.activity.C00_BuyDetailActivity;
import com.dmy.farming.activity.C01_SellDetailActivity;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.chat.SALELIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class E01_MyCollectionBuyListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
    ArrayList<BUYLIST> datalist;
	public Handler parentHandler;

	public E01_MyCollectionBuyListAdapter(Context context, ArrayList<BUYLIST> datalist) {
		this.mContext = context;
		this.datalist = datalist;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {
		return datalist.size();
	}

//	@Override
//	public CHATUSER getItem(int position) {
//		return friends.get(position);
//	}


	public BUYLIST getItem(int position) {
		if (position >= 0 && position < datalist.size())
			return datalist.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	String pattern ="yyyy-MM-dd HH:mm:ss";
	String display = "";
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

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
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.farm = (TextView) convertView.findViewById(R.id.farm);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

			viewHolder. phone = convertView.findViewById(R.id.phone);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
	final 	BUYLIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			viewHolder.price.setText(cat.price);
			viewHolder.distance.setText(cat.distance);
			viewHolder.address_detail.setText(cat.address_details);
			viewHolder.farm.setText(cat.buy_type);
			viewHolder.farm.setBackgroundColor(mContext.getResources().getColor(R.color.text_blue));
			imageLoader.displayImage(cat.img_url, viewHolder.img_route, FarmingApp.options_long_with_text);
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					BUYLIST data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, C00_BuyDetailActivity.class);
						intent.putExtra("id", data.id);
						intent.putExtra("type", "buy");
						mContext.startActivity(intent);
					}
				}

			});
			viewHolder.phone .setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.phone:
							call(cat.link_phone);
							break;
					}
				}
			});

			String time = cat.publish_time;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
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


	private class ViewHolder {
		public View layout_frame,phone;
		public ImageView img_route;
		public TextView title,price,distance,address_detail,time,farm;

	}
}
