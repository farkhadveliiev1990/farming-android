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

import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.api.data.BUYLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E01_MyBuyListAdapter extends BaseAdapter {

	Context mContext;
//	List<CHATUSER> friends;


	public Handler parentHandler;

	public E01_MyBuyListAdapter(Context context) {
		this.mContext = context;
//		this.friends = friends;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	public BUYLIST getItem(int position) {
			return null;
	}

	@Override
	public int getCount() {		
		return 1;
	}

//	@Override
//	public CHATUSER getItem(int position) {
//		return friends.get(position);
//	}

	private class ViewHolder {
		public View layout_frame;
		public ImageView img_route;
		public TextView title,price,district,address_detail;

	}



	@Override
	public long getItemId(int position) {		
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_my_buy_item, null,false);

			View phone = convertView.findViewById(R.id.phone);
			phone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.phone:
							call("123456789");
							break;
					}
				}
			});
		}else{
			//viewHolder = (ViewHolder) convertView.getTag();
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
