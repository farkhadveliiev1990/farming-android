package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.E01_NoticeDetailActivity;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.chat.WARNLIST;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class E01_MyFollowListAdapter extends BaseAdapter{

	Context mContext;
//	List<CHATUSER> friends;
	ArrayList<DICTIONARY> dataList;

	public Handler parentHandler;

	public E01_MyFollowListAdapter(Context context, ArrayList<DICTIONARY> dataList) {
		this.mContext = context;
//		this.friends = friends;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	public DICTIONARY getItem(int position) {
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
		public TextView content;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	/*ImageLoader imageLoader = ImageLoader.getInstance();*/

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chose_crop_button1, null,false);

			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DICTIONARY cat = getItem(position);
		if (cat != null)
		{
			viewHolder.content.setText(cat.name);
		}
		return convertView;
	}
}
