package com.dmy.farming.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.C02_AgrotechniqueVideoDetailActivity;
import com.dmy.farming.api.data.VIDEOLIST;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class C01_AgrotechniqueVideoAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<VIDEOLIST> dataList;

	public C01_AgrotechniqueVideoAdapter(Context c, ArrayList<VIDEOLIST> dataList) {
		mContext = c;
		this.dataList = dataList;
	}

	public int getCount() {
		return dataList.size();
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
//		public VideoView video;
		public ImageView img_route,video;
		public TextView num,title;
		public View layout_frame;
	}

    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.b01_knowledge_item_video, null);
		//	viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
			viewHolder.video =  (ImageView)convertView.findViewById(R.id.video);
			viewHolder.num = (TextView) convertView.findViewById(R.id.num);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final  VIDEOLIST cat = getItem(position);
		if (cat != null)
		{
			Uri uri = Uri.parse(cat.video_url);
//			viewHolder.video.setVideoURI(uri);

			viewHolder.num.setText(cat.viewNum);
			viewHolder.title.setText(cat.title);

			/*MediaMetadataRetriever media = new MediaMetadataRetriever();
			media.setDataSource(cat.video_url, new HashMap<String, String>());
			//获得第一帧图片
			Bitmap bitmap = media.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
			viewHolder.video.setImageBitmap(bitmap);*/

			imageLoader.displayImage(cat.titlePicurl,viewHolder.video,FarmingApp.options);

			/*viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, C02_AgrotechniqueVideoDetailActivity.class);

					mContext.startActivity(intent);

				}

			});*/
			convertView.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, C02_AgrotechniqueVideoDetailActivity.class);
					intent.putExtra("url",cat.video_url);
					intent.putExtra("id",cat.video_id);
					intent.putExtra("cycle",cat.cycle);
					intent.putExtra("cyclename",cat.cycleName);
					mContext.startActivity(intent);

				}

			});
		}
		return convertView;
	}
}
