package com.dmy.farming.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dmy.farming.R;
import com.dmy.farming.api.data.chat.PRICELIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class C01_MarketPrice1ListAdapter extends BaseAdapter {

	Context mContext;
	List<PRICELIST> dataList;

	int type; //2:官方, 3:网友

	public C01_MarketPrice1ListAdapter(Context context,int type, List<PRICELIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
		this.type = type;
	}

	@Override
	public int getCount() {		
		return dataList.size();
	}

	@Override
	public PRICELIST getItem(int position) {
		if (position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {		
		return position;
	}

    private class ViewHolder {
        View layout_frame, layout_kind;
        ImageView img_activity;
        TextView zuowu, price, position, time;
    }

	ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.c01_market_price1_list_item, null);

			holder.zuowu = (TextView) convertView.findViewById(R.id.zuowu);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.position = (TextView) convertView.findViewById(R.id.position);
			holder.time = (TextView) convertView.findViewById(R.id.time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		PRICELIST item = getItem(position);
		if (item != null) {
			//	imageLoader.displayImage(item.user_logo, holder.img_user, FarmingApp.options_head);
			holder.zuowu.setText(item.crop_type);
			//	String pricel = item.low+"-"+item.upper;
			if(type ==2 ){
				String pricel = item.low+"-"+item.upper;
				holder.price.setText(pricel);
			}else{
				holder.price.setText(item.upper);
			}
			holder.position.setText(item.district);
			holder.time.setText(item.publish_time);

		}

		return convertView;
	}

}
