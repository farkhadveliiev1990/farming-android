package com.dmy.farming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.AppUtils;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.bee.activity.BaseActivity;

import java.util.ArrayList;

public class B00_ActivityAdapter extends BaseAdapter{

	Context mContext;
//	ArrayList<ACTIVITYLIST> dataList;

	public B00_ActivityAdapter(Context c/*, ArrayList<ACTIVITYLIST> dataList*/) {
		mContext = c;
//		this.dataList = dataList;
	}

	public int getCount() {
		return 0 /*dataList.size()*/;
	}

	public /*ACTIVITYLIST*/ String getItem(int position) {
//		if (position >= 0 && position < dataList.size())
//			return dataList.get(position);
//		else
			return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public View layout_frame;
		public ImageView img_activity;
		public TextView text_activity,text_group;
	}

    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

//			convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_activity_cell, null, false);
//			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
//			viewHolder.img_activity = (ImageView) convertView.findViewById(R.id.img_activity);
//			viewHolder.text_activity = (TextView) convertView.findViewById(R.id.text_activity);
//			viewHolder.text_group = (TextView) convertView.findViewById(R.id.text_group);

			ViewGroup.LayoutParams params1 = viewHolder.img_activity.getLayoutParams();
			params1.width = (AppUtils.getScWidth(mContext));
			params1.height = (int) (params1.width*1.0/720*330);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		ACTIVITYLIST cat = getItem(position);
//		if (cat != null)
//		{
//			viewHolder.text_activity.setText(cat.activity_name);
//			viewHolder.text_group.setText(cat.group_name);
//			imageLoader.displayImage(cat.activity_img, viewHolder.img_activity, FarmingApp.options_long_with_text);
//			viewHolder.layout_frame.setOnClickListener(new OnClickListener()
//			{
//				@Override
//				public void onClick(View v) {
//					ACTIVITYLIST data = getItem(position);
//					if (data != null)
//					{
//						((BaseActivity)mContext).clickActivity(Long.parseLong(data.activity_id));
//					}
//				}
//
//			});
//		}
		return convertView;
	}
}
