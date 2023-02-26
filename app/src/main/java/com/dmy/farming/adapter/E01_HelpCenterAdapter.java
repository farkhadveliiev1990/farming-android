package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueArticleDetailActivity;
import com.dmy.farming.activity.E01_HelpCenterDetailActivity;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.api.data.chat.FARMARTICLE;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class E01_HelpCenterAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<HELPCENTER> dataList;

	public E01_HelpCenterAdapter(Context c, ArrayList<HELPCENTER> dataList) {
		this.mContext = c;
		this.dataList = dataList;
	}

	public int getCount() {
		return dataList.size();
	}

	public HELPCENTER getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public TextView title,content;
		public View layout_0;
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public View getView(final int position, View convertView, ViewGroup parent) {
		final	ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_helpcenter_item, null);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.layout_0 =  convertView.findViewById(R.id.layout_0);

			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
	final 	HELPCENTER cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			viewHolder.content.setText(cat.content);
			viewHolder.layout_0.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,E01_HelpCenterDetailActivity.class);
					intent.putExtra("content",viewHolder.content.getText());
					mContext.startActivity(intent);
				}
			});
		}
		return convertView;
	}

}
