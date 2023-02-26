package com.dmy.farming.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmy.farming.R;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.activity.E01_NoticeDetailActivity;
import com.dmy.farming.activity.PublishRentActivity;
import com.dmy.farming.api.data.chat.NOTICELIST;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.fragment.E01_NoticeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class E01_NoticeListAdapter extends BaseAdapter{

	Context mContext;
	ArrayList<NOTICELIST> dataList;

	public E01_NoticeListAdapter(Context context, ArrayList<NOTICELIST> dataList) {
		this.mContext = context;
		this.dataList = dataList;
	}

//	public void bindData(List<CHATUSER> friends) {
//		this.friends = friends;
//	}

	public NOTICELIST getItem(int position) {
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
		public TextView title,content,detail,time,readstatus;
		public View  layout;

	}



	@Override
	public long getItemId(int position) {
		return position;
	}

	/*ImageLoader imageLoader = ImageLoader.getInstance();*/

	boolean isChinese = true;

	String display = null;
	String pattern ="yyyy-MM-dd HH:mm:ss";
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.e01_notice_item, null,false);

			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.readstatus = (TextView) convertView.findViewById(R.id.readstatus);
			viewHolder.layout = convertView.findViewById(R.id.layout);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final NOTICELIST cat = getItem(position);
		if (cat != null)
		{
			viewHolder.title.setText(cat.title);
			viewHolder.content.setText((Html.fromHtml(cat.content)));
			if(cat.readNum.equals("0")){
				viewHolder.readstatus.setText("[未读]");
				viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.text_red));
			}else{
				viewHolder.readstatus.setText("[已读]");
				viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.green));
			}

			String time = cat.createTime;

			/*long curtime=System.currentTimeMillis();
			long time = Long.parseLong(cat.createtime);
			final Calendar mCalendar=Calendar.getInstance();
			mCalendar.setTimeInMillis(time);
			int mHour=mCalendar.get(Calendar.HOUR);
			int mMinuts=mCalendar.get(Calendar.MINUTE);
			//format = "HH:mm";
			if(curtime-time) {
				if (mHour > 6 && mHour <= 11) {
					format = "上午" + mHour + ":" + mMinuts;
				}
				if (mHour > 11 && mHour <= 17) {
					format = "下午" + mHour + ":" + mMinuts;
				}
				if (mHour >= 0 && mHour <= 6) {
					format = "凌晨" + mHour + ":" + mMinuts;
				}
				if (mHour > 17) {
					format = "晚上" + mHour + ":" + mMinuts;
				}
			}*/
			int tMin = 60 * 1000;
			int tHour = 60 * tMin;
			int tDay = 24 * tHour;
			if (time != null) {
				try {
					Date tDate = new SimpleDateFormat(pattern).parse(time);
					Date today = new Date();
					SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
					SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
					Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
					Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
					Date beforeYes = new Date(yesterday.getTime() - tDay);
					if (tDate != null) {
						SimpleDateFormat halfDf = new SimpleDateFormat("yyyy年MM月dd日");
						long dTime = today.getTime() - tDate.getTime();
						if (tDate.before(thisYear)) {
							display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
						} else {
							 if (tDate.after(beforeYes) && tDate.before(yesterday)) {
								display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
							}else if(dTime < tDay && tDate.after(yesterday)){
								 if (tDate.getHours() > 6 && tDate.getHours() <= 11) {
									 display = "上午" + (tDate.getHours()-12) + ":" + (tDate.getMinutes()<10?"0"+tDate.getMinutes():tDate.getMinutes());
								 }
								 if (tDate.getHours() > 11 && tDate.getHours() <= 17) {
									 display = "下午" + (tDate.getHours()-12) + ":" + (tDate.getMinutes()<10?"0"+tDate.getMinutes():tDate.getMinutes());
								 }
								 if (tDate.getHours() >= 0 && tDate.getHours() <= 6) {
									 display = "凌晨" + (tDate.getHours()-12) + ":" + (tDate.getMinutes()<10?"0"+tDate.getMinutes():tDate.getMinutes());
								 }
								 if (tDate.getHours() > 17) {
									 display = "晚上" + (tDate.getHours()-12)+ ":" + (tDate.getMinutes()<10?"0"+tDate.getMinutes():tDate.getMinutes());
								 }
							 } else {
								display = halfDf.format(tDate);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			viewHolder.time.setText(display);

			viewHolder.layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent intent = new Intent(mContext, E01_NoticeDetailActivity.class);
					intent.putExtra("id", cat.id);
					mContext.startActivity(intent);

				}
			});

			viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					ArrayList<String> datalist = new ArrayList<String>();
					datalist.add("删除");
					showDialog("",datalist,cat.id);
					return true;
				}
			});
		}
		return convertView;
	}

	AlertDialog dialog;
	private void showDialog(String title, final ArrayList<String> datalist, final String noiceid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext,AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog,null);
		builder.setView(view);
		if (TextUtils.isEmpty(title)){
			view.findViewById(R.id.text_title).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.text_title).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.text_title)).setText(title);
		}
		ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(mContext,datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				deleteNotice(noiceid);
				dialog.dismiss();
			}
		});

		dialog = builder.create();

		Window dialogWindow = dialog.getWindow();

		dialog.show();

		Display d = ((E01_NoticeActivity)mContext).getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.gravity = Gravity.CENTER;
		p.width = (int)(d.getWidth()*0.8);

		dialogWindow.setAttributes(p);
	}

	public static String formatDisplayTime(String time, String pattern) {
		String display = "";
		int tMin = 60 * 1000;
		int tHour = 60 * tMin;
		int tDay = 24 * tHour;

		if (time != null) {
			try {
				Date tDate = new SimpleDateFormat(pattern).parse(time);
				Date today = new Date();
				SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
				SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
				Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
				Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
				Date beforeYes = new Date(yesterday.getTime() - tDay);
				if (tDate != null) {
					SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
					long dTime = today.getTime() - tDate.getTime();
					if (tDate.before(thisYear)) {
						display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
					} else {

						if (dTime < tMin) {
							display = "刚刚";
						} else if (dTime < tHour) {
							display = (int) Math.ceil(dTime / tMin) + "分钟前";
						} else if (dTime < tDay && tDate.after(yesterday)) {
							display = (int) Math.ceil(dTime / tHour) + "小时前";
						} else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
							display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
						} else {
							display = halfDf.format(tDate);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return display;
	}


}
