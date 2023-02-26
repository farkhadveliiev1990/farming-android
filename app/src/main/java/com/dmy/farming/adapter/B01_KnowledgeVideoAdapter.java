package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.dmy.farming.activity.C02_AgrotechniqueVideoDetailActivity;
import com.dmy.farming.api.data.VIDEOLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class B01_KnowledgeVideoAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<VIDEOLIST> dataList;
	String code_type;
	String cyclename;

	public B01_KnowledgeVideoAdapter(Context c, ArrayList<VIDEOLIST> dataList,String code_type,String cyclename) {
		mContext = c;
		this.dataList = dataList;
		this.code_type =code_type;
		this.cyclename =cyclename;
	}

	public int getCount() {
		if (dataList.size() < 4)
			return dataList.size();
		else
			return 4 /*dataList.size()*/;
	}

	public VIDEOLIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		public View layout_frame;
		public ImageView video;
		public TextView title,num;
	}

    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_knowledge_item_video, null, false);
			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.video = (ImageView) convertView.findViewById(R.id.video);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.num = (TextView) convertView.findViewById(R.id.num);

//			ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		VIDEOLIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			viewHolder.num.setText(cat.page_view);
			imageLoader.displayImage(cat.video_url, viewHolder.video, FarmingApp.options);
			viewHolder.layout_frame.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					VIDEOLIST data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, C02_AgrotechniqueVideoDetailActivity.class);
						intent.putExtra("id", data.video_id);
						intent.putExtra("url",data.video_url);
						intent.putExtra("cycle",code_type);
						intent.putExtra("cyclename",cyclename);
						mContext.startActivity(intent);
					}
				}

			});
		}
		return convertView;
	}
}
