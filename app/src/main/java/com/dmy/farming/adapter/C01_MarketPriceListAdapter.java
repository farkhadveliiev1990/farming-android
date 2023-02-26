package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C01_MarketPrice1Activity;
import com.dmy.farming.activity.C01_RentDetailActivity;
import com.dmy.farming.api.data.chat.PRICELIST;
import com.dmy.farming.api.data.chat.RENTLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C01_MarketPriceListAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;

	ArrayList<PRICELIST> dataList;

	public Handler parentHandler;

	public C01_MarketPriceListAdapter(Context context, ArrayList<PRICELIST> dataList) {
		this.mContext = context;
//		this.friends = friends;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {
		return dataList.size();
	}

//	@Override
//	public CHATUSER getItem(int position) {
//		return friends.get(position);
//	}


	@Override
	public PRICELIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	private class ViewHolder {
		public View layout_frame;
		public ImageView img_route;
		public TextView zuowu,price,position;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_market_price_list_item, null);
			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			//viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
			viewHolder.zuowu = (TextView) convertView.findViewById(R.id.zuowu);
			viewHolder.position = (TextView) convertView.findViewById(R.id.position);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			//viewHolder.address_detail = (TextView) convertView.findViewById(R.id.address_detail);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PRICELIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.zuowu.setText(cat.crop_type);
			viewHolder.price.setText(cat.low+"-" +cat.upper);
			viewHolder.position.setText(cat.district);
		/*	viewHolder.address_detail.setText(cat.district);
			imageLoader.displayImage(cat.img_url, viewHolder.img_route, FarmingApp.options_long_with_text);*/
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					PRICELIST data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, C01_MarketPrice1Activity.class);
                        intent.putExtra("price_data",data);
						mContext.startActivity(intent);
					}
				}

			});
		}
		return convertView;
	}
}
