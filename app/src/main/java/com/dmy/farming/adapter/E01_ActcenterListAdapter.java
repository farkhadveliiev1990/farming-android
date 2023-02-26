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
import com.dmy.farming.activity.HtmlViewActivity;
import com.dmy.farming.api.data.ACTIVITYCENTER;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class E01_ActcenterListAdapter extends BaseAdapter {

	Context mContext;
	List<ACTIVITYCENTER> datas;


	public E01_ActcenterListAdapter(Context context,List<ACTIVITYCENTER> datas) {
		this.mContext = context;
		this.datas = datas;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	@Override
	public int getCount() {		
		return datas.size();
	}

	@Override
	public ACTIVITYCENTER getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_actcenter_list_item, null);

			holder.layout =  convertView.findViewById(R.id.layout);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ACTIVITYCENTER item = getItem(position);
		imageLoader.displayImage(item.image, holder.img, FarmingApp.options_long_with_text);
		holder.title.setText(item.title);
		holder.time.setText("有效期 : " + item.start_time.replace("-",".") + " - " + item.end_time.replace("-","."));

		holder.layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ACTIVITYCENTER data = getItem(position);
				if (data != null){
					Intent intent = new Intent(mContext, HtmlViewActivity.class);
					intent.putExtra("link", data.targetUrl);
					mContext.startActivity(intent);
				}
			}
		});

		return convertView;
	}
	
	class ViewHolder {
		ImageView img;
		TextView title,time;
		View layout;
	}
}
