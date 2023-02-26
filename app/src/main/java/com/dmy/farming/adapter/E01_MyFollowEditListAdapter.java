package com.dmy.farming.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.api.data.DICTIONARY;

import java.util.ArrayList;

public class E01_MyFollowEditListAdapter extends BaseAdapter{

	Context mContext;
//	List<CHATUSER> friends;
	public ArrayList<DICTIONARY> dataList;
	public String removelist = "";

	public Handler parentHandler;

	public E01_MyFollowEditListAdapter(Context context, ArrayList<DICTIONARY> dataList) {
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
		public TextView content,delete,code;
		public FrameLayout frameLayout;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	/*ImageLoader imageLoader = ImageLoader.getInstance();*/

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final	ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chose_crop_button2, null,false);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.frameLayout = (FrameLayout)convertView.findViewById(R.id.layout_crop) ;
			viewHolder.delete = (TextView) convertView.findViewById(R.id.delete);
			viewHolder.code = (TextView) convertView.findViewById(R.id.code);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		DICTIONARY cat = getItem(position);
		if (cat != null)
		{
			viewHolder.content.setText(cat.name);
			viewHolder.code.setText(cat.aboutCode);
			viewHolder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					viewHolder.frameLayout.removeView(v);
					DICTIONARY cat = getItem(position);
					dataList.remove(position);
					removelist += cat.aboutCode+"," ;
					notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}
}
