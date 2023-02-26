package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C03_DiagmpsticLibDetailActivity;
import com.dmy.farming.api.data.DIAGNOSTIC;
import com.dmy.farming.api.data.VIDEOLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class B01_KnowledgeDiagnosisAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<DIAGNOSTIC> dataList;
	String code_type;
	String cyclename;

	public B01_KnowledgeDiagnosisAdapter(Context c, ArrayList<DIAGNOSTIC> dataList,String code_type,String cyclename) {
		mContext = c;
		this.dataList = dataList;
		this.code_type =code_type;
		this.cyclename = cyclename;
	}

	public int getCount() {
		if (dataList.size() < 3)
			return dataList.size();
		else
			return 3 /*dataList.size()*/;
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
		public TextView title,num;
	}

    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();

			convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_knowledge_item_article, null, false);
			viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.num = (TextView) convertView.findViewById(R.id.num);

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
			viewHolder.title.setText(cat.name);
			viewHolder.num.setText(cat.page_view);
			imageLoader.displayImage(cat.img_url, viewHolder.img, FarmingApp.options);
			viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					DIAGNOSTIC data = getItem(position);
					if (data != null)
					{
						Intent intent = new Intent(mContext, C03_DiagmpsticLibDetailActivity.class);
						intent.putExtra("id",cat.id);
						intent.putExtra("title",cat.name);
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
