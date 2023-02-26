package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueVideoDetailActivity;
import com.dmy.farming.activity.C03_DiagmpsticLibDetailActivity;
import com.dmy.farming.api.data.DIAGNOSTIC;
import com.dmy.farming.api.data.VIDEOLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class C01_DiagnosticLibAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<DIAGNOSTIC> dataList;

	public C01_DiagnosticLibAdapter(Context c, ArrayList<DIAGNOSTIC> dataList) {
		mContext = c;
		this.dataList = dataList;
	}

	public int getCount() {
		return dataList.size();
	}

	public DIAGNOSTIC getItem(int position) {
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
		public ImageView img;
		public TextView text_title,text_browse_num;
	}

    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_diagnosticlib_item, null, false);
			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.text_title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.text_browse_num = (TextView) convertView.findViewById(R.id.browse_num);

//			ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final DIAGNOSTIC cat = getItem(position);
		if (cat != null)
		{
			viewHolder.text_title.setText(cat.name);
			viewHolder.text_browse_num.setText(cat.page_view);
			if (TextUtils.isEmpty(cat.img_url))
				viewHolder.img.setVisibility(View.GONE);
			else {
				viewHolder.img.setVisibility(View.VISIBLE);
				if (cat.img_url.contains(","))
					imageLoader.displayImage(cat.img_url.substring(0,cat.img_url.indexOf(",")),viewHolder.img, FarmingApp.options);
				else
					imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options);
			}
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, C03_DiagmpsticLibDetailActivity.class);
                    intent.putExtra("id",cat.id);
                    intent.putExtra("title",cat.name);
					intent.putExtra("cycle",cat.cycle);
					mContext.startActivity(intent);

				}

			});

		}
		return convertView;
	}
}
