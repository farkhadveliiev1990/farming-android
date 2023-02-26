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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C00_BuyDetailActivity;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.api.data.BUYLIST;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class E01_MyCollectionFarmArticleListAdapter extends BaseAdapter implements View.OnClickListener {

	Context mContext;
    ArrayList<FARMARTICLE> datalist;
	public Handler parentHandler;

	public E01_MyCollectionFarmArticleListAdapter(Context context, ArrayList<FARMARTICLE> datalist) {
		this.mContext = context;
		this.datalist = datalist;
	}

	@Override
	public int getCount() {
		return datalist.size();
	}

	public FARMARTICLE getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_collection_farmarticle_list_item, null,false);

			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.keyword = (TextView) convertView.findViewById(R.id.keyword);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.num = (TextView) convertView.findViewById(R.id.num);
			viewHolder.layout_article_item = (LinearLayout)convertView.findViewById(R.id.layout_article_item);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final	FARMARTICLE cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			String keywordname = "";
			for(int i =0;i< cat.keyWord.size();i++){
				keywordname += cat.keyWord.get(i).wordName+"  ";
			}
			viewHolder.keyword.setText(keywordname);
			viewHolder.time.setText(cat.publishTime);
			viewHolder.num.setText(cat.pageView);
			viewHolder.layout_article_item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,C02_AgrotechniqueArticleDetailActivity.class);
					intent.putExtra("id", cat.id);
					intent.putExtra("cycle", cat.cycle);
					mContext.startActivity(intent);
				}
			});
		}

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
		public TextView title,keyword,time,num;
		public ImageView close;
		public LinearLayout layout,layout_article_item;

	}
}
