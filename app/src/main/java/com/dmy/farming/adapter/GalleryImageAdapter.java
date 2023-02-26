package com.dmy.farming.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dmy.farming.R;

import java.io.InputStream;


public class GalleryImageAdapter extends PagerAdapter {

	private Context context;
	public Handler parentHandler;

	LayoutInflater inflater;

	public GalleryImageAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public static int SCREEN_CNT = 3;

	private Bitmap[] bmp = new Bitmap[SCREEN_CNT];

	@Override
	public int getCount() {

		return SCREEN_CNT;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		(container).removeView((View) object);
		
		if (bmp[position] != null && !bmp[position].isRecycled())
		{
			//Log.e("delete", position+"");
			bmp[position].recycle();   //回收图片所占的内存
	        System.gc();  //提醒系统及时回收
	        bmp[position] = null;
		}
	}


	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */
	public Bitmap readBitMap(Context context, int resId, int position){ 
		//Log.e("load", position+"");
		if (bmp[position] == null || bmp[position].isRecycled())
		{
			//Log.e("init", position+"");
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;   
			//opt.inJustDecodeBounds = false;
			//opt.inSampleSize = 0;   //width，hight设为原来的十分一			
			//opt.inSampleSize = 10;
			
			opt.inPurgeable = true;  
			opt.inInputShareable = true;  
			
			
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			//获取资源图片			
			opt.inScaled = true;

			//getBitmapDensity()用于设置图片将要被显示的密度。
			//opt.inTargetDensity = displayMetrics.densityDpi;
			opt.inDensity = DisplayMetrics.DENSITY_DEFAULT;
			
			InputStream is = context.getResources().openRawResource(resId);  
			try {				
				bmp[position] = BitmapFactory.decodeStream(is,null,opt);
			}
			catch (Exception e){  // kcm 2015-05-19
				//e.printStackTrace();
				bmp[position] = null;
			}			
		}
		return bmp[position];
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		final ViewHolder holder;

		holder = new ViewHolder();
		View imageLayout = inflater.inflate(R.layout.gallery_image_item, null);

		holder.image = (LinearLayout) imageLayout.findViewById(R.id.gallery_image_item_view);

		if(position == SCREEN_CNT) {
			holder.image.setEnabled(true);
		} else {
			holder.image.setEnabled(false);
		}
		Bitmap bm = null;
		if(position == 0) {
			holder.image.removeAllViews();
			View view0 = inflater.inflate(R.layout.lead_a, null);
			ImageView imv = (ImageView) view0.findViewById(R.id.imgView);


			bm = readBitMap(context, R.drawable.vectoring_img_1, position);  // kcm 2015-05-19
			if (bm != null)
				imv.setImageBitmap(bm);
			holder.image.addView(view0);

		} else if (position == 1) {
			holder.image.removeAllViews();
			View view1 = inflater.inflate(R.layout.lead_a, null);
			ImageView imv = (ImageView) view1.findViewById(R.id.imgView);
			//imv.setImageBitmap(readBitMap(context, R.drawable.tuitional_img_2, position));
			bm = readBitMap(context, R.drawable.vectoring_img_2, position);
			if (bm != null)
				imv.setImageBitmap(bm);
			holder.image.addView(view1);
		}
//		else if (position == 2) {
//			holder.image.removeAllViews();
//			View view2 = inflater.inflate(R.layout.lead_a, null);
//			ImageView imv = (ImageView) view2.findViewById(R.id.imgView);
//			//imv.setImageBitmap(readBitMap(context, R.drawable.tuitional_img_3, position));
//			bm = readBitMap(context, R.drawable.vectoring_img_3, position);
//			if (bm != null)
//				imv.setImageBitmap(bm);
//			holder.image.addView(view2);
//		}
		else if (position == 2) {
			holder.image.removeAllViews();
			View view3 = inflater.inflate(R.layout.lead_e, null);

			ImageView imv = (ImageView) view3.findViewById(R.id.imgView);
			bm = readBitMap(context, R.drawable.vectoring_img_3, position);
			if (bm != null)
				imv.setImageBitmap(bm);

			View button_start = view3.findViewById(R.id.button_start);
			button_start.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if (parentHandler != null) {
						Message msg = new Message();
						msg.what = 1;
						parentHandler.sendMessage(msg);
					}
				}
			});

			holder.image.addView(view3);
		}/* else if (position == 4) {
			holder.image.removeAllViews();
			View view4 = inflater.inflate(R.layout.lead_e, null);
			holder.image.addView(view4);
		} */

		view.addView(imageLayout, 0);
		return imageLayout;
	}

	class ViewHolder {
		private LinearLayout image;
	}

}
