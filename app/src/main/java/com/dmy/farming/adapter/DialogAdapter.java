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
import com.dmy.farming.activity.C01_SellDetailActivity;
import com.dmy.farming.api.data.chat.SALELIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class DialogAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<String> dataList;

	public Handler parentHandler;

	public DialogAdapter(Context context, ArrayList<String> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	private class ViewHolder {
		public TextView title;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_item, null);

			viewHolder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(dataList.get(position));

		return convertView;
	}


}
