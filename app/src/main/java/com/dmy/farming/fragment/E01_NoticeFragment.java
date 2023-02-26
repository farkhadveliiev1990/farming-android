package com.dmy.farming.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.activity.E01_NoticeDetailActivity;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.adapter.E01_NoticeListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.chat.NOTICELIST;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.api.model.NoticeModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.umeng.socialize.UMShareAPI;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class E01_NoticeFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;

	NoticeModel noticeModel;

	@Override
	public void onClick(View v) {

	}
	/**
	 * Created by wang on 2016/5/12.
	 * checkbox抽象模型，用来保存每一个checkbox的选中状态
	 */
	public class CheckBoxBean {
		private boolean isChecked;

		public CheckBoxBean() {
		}

		public CheckBoxBean(boolean isChecked) {
			this.isChecked = isChecked;
		}

		public boolean isChecked() {
			return isChecked;
		}

		public void setChecked(boolean checked) {
			isChecked = checked;
		}
	}

	/**
	 * 是否创建
	 */
	protected boolean isCreate = false;


	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.b00_warning,null);
		mActivity = (BaseActivity) getActivity();


		noticeModel = new NoticeModel(getContext());
		noticeModel.addResponseListener(this);
	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/

		initView(mainView);
		requestData(true);
		isCreate=true;
		return mainView;
	}

	XListView list_black;
	View null_pager;
	LinearLayout deleted;
	E01_NoticeListAdapter listAdapter;
	private static final String TAG = "批量删除";
	private CheckBox checkBox;
	private TextView layout_deleted,cancel;
	private List<String> list = new ArrayList<>();
	private List<CheckBoxBean> boxList = new ArrayList<>();
	LinearLayout checklayout;
	RelativeLayout checkBox_layout;
	int flag = 0;

	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);
		layout_deleted = (TextView) mainView. findViewById(R.id.layout_deleted);
		layout_deleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteCheckedItem();
			}
		});
		checklayout = (LinearLayout) mainView.findViewById(R.id.checklayout);
		/*deleted = (LinearLayout)getActivity().findViewById(R.id.deleted);
		deleted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
              int a = ((E01_NoticeActivity)getActivity()).cur_tab;
				if (((E01_NoticeActivity)getActivity()).cur_tab == 0) {
					if (noticelists.size() > 0) {
						if (flag == 0) {
							checklayout.setVisibility(View.VISIBLE);
							//	checkBox_layout.setVisibility(View.VISIBLE);
							flag = 1;
							ad();
						} else {
							checklayout.setVisibility(View.GONE);
							//	checkBox_layout.setVisibility(View.VISIBLE);
							flag = 0;
							ad();
						}
					}
				}
			}
		});*/
		checkBox = (CheckBox) mainView.findViewById(R.id.checkBox);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectAll(isChecked);
				listAdapter.notifyDataSetChanged();
			}
		});


		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

	public  void  ad(){
		initBoxBeanList();
		listAdapter = new E01_NoticeListAdapter(getContext(),noticelists,boxList,flag);
		list_black.setAdapter(listAdapter);
	}

	public  void initBoxBeanList() {
		if (noticelists.size() > 0) {
			boxList.clear();
			for (int i = 0; i < noticelists.size(); i++) {
				boxList.add(new CheckBoxBean(false));
			}
			if (flag == 0) {
				checklayout.setVisibility(View.VISIBLE);
				//	checkBox_layout.setVisibility(View.VISIBLE);
				flag = 1;
			} else {
				checklayout.setVisibility(View.GONE);
				//	checkBox_layout.setVisibility(View.VISIBLE);
				flag = 0;
			}
		}
		listAdapter = new E01_NoticeListAdapter(getContext(),noticelists,boxList,flag);
		list_black.setAdapter(listAdapter);
	}

	/**
	 * 删除选中项
	 */

	private void deleteCheckedItem() {
		int count = noticelists.size() - 1 ;
		String id = "";
		String status = "";
		//从大都小遍历，否则，从小到大遍历的话会出现错位
		for (int i = count ; i >= 0 ; i--) {
			if (boxList.get(i).isChecked()){
				id+=noticelists.get(i).id+",";
				status+= noticelists.get(i).readNum+",";
			}
		}

		if(status.contains("0")){
			showDialog1("您有未读消息确定删除吗？",id);
		}else{
			deleteNotice(id);
			checklayout.setVisibility(View.GONE);
			checkBox.setChecked(false);
		}
		deleteNotice(id);
		//如果是全部删除,全选框设为未选中
		if (noticelists.size()==0){
			checklayout.setVisibility(View.GONE);
			checkBox.setChecked(false);
		}
		//initBoxBeanList();
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 单项中选中状态改变时，由adpater回调
	 * @param position
	 * @param isChecked
	 */
	public void checkedStateChange(int position, boolean isChecked) {
		boxList.get(position).setChecked(isChecked);
		listAdapter.notifyDataSetChanged();
	}

	/**
	 * 是否全选
	 * @param isChecked
	 * true:全选
	 * false:全不选
	 */
	private void selectAll(boolean isChecked) {
		for (int i = 0; i < boxList.size(); i++) {
			boxList.get(i).setChecked(isChecked);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isCreate) {
			//相当于Fragment的onResume
			requestData(true);
		} else {
			//相当于Fragment的onPause
		}
	}

	AlertDialog dialog;
	private void showDialog1(String title, final String noiceid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),AlertDialog.THEME_HOLO_LIGHT);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout,null);
		builder.setView(view);
		if (TextUtils.isEmpty(title)){
			view.findViewById(R.id.dialog_message).setVisibility(View.GONE);
		}else {
			view.findViewById(R.id.dialog_message).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.dialog_message)).setText(title);
		}
		view.findViewById(R.id.positive).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = 0;
				deleteNotice(noiceid);
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.negative).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
    	/*ListView listview = (ListView) view.findViewById(R.id.listview);
		DialogAdapter adapter = new DialogAdapter(getContext(),datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				deleteNotice(noiceid);
				dialog.dismiss();
			}
		});
*/
		dialog = builder.create();

		Window dialogWindow = dialog.getWindow();

		dialog.show();

		Display d = ((E01_NoticeActivity)getContext()).getWindowManager().getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.gravity = Gravity.CENTER;
		p.width = (int)(d.getWidth()*0.8);

		dialogWindow.setAttributes(p);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	final static int REQUEST_MONEY = 1;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(mActivity).onActivityResult( requestCode, resultCode, data);

		if (data != null)
		{
		}

			if (requestCode == REQUEST_MONEY)
				//gotoQianBao();
				requestData(true);
			//	noticeModel.getNoticnum();

	}

	ArrayList<NOTICELIST>  noticelists;
	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (noticeModel.data.routes.size() > 0)
		{
			noticelists = noticeModel.data.routes;
			null_pager.setVisibility(View.GONE);
			if (listAdapter == null) {
				listAdapter = new E01_NoticeListAdapter(getContext(), noticeModel.data.routes,boxList,flag);
				list_black.setAdapter(listAdapter);


			} else {
				listAdapter.notifyDataSetChanged();
			}
			if (0 == noticeModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			listAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}


	public class E01_NoticeListAdapter extends BaseAdapter {

		Context mContext;
		ArrayList<NOTICELIST> dataList;
		private List<CheckBoxBean> boxBeanList;
		private int isflag;

		public E01_NoticeListAdapter(Context context, ArrayList<NOTICELIST> dataList,List<CheckBoxBean> boxBeanList,int isflag) {
			this.mContext = context;
			this.dataList = dataList;
			this.isflag = isflag;
			this.boxBeanList = boxBeanList;
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
			CheckBox checkBox;

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
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_item);
				viewHolder.layout = convertView.findViewById(R.id.layout);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final NOTICELIST cat = getItem(position);
			if (cat != null)
			{
				if(isflag==1){
					viewHolder.checkBox.setVisibility(View.VISIBLE);
					//设置监听
					viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							checkedStateChange(position, isChecked);
						}
					});

					//根据集合中的 抽象模型的属性的设置checkbox的选中状态
					viewHolder.checkBox.setChecked(boxBeanList.get(position).isChecked());
				}else{
					viewHolder.checkBox.setVisibility(View.GONE);
				}
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
						startActivityForResult(intent, REQUEST_MONEY);
						//mContext.startActivity(intent);

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
					deleteNotice(noiceid);
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


	}


	int page = 1;
	String type = "0";


	public void requestData(boolean bShow)
	{
		//request.city = AppUtils.getCurrCity(mActivity);
		page = 1;
		noticeModel.getNoticeList(AppConst.info_from,type,page,bShow);
	}

	public void deleteNotice(String id)
	{
		noticeModel.deleteNotice(id);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if(url.contains(ApiInterface.NOTICE)) {
			list_black.setRefreshTime();
			updateData();
		}else if (url.contains(ApiInterface.deleteNoice)){
			if (noticeModel.lastStatus.error_code == 200){
				mActivity.errorMsg("删除成功");
				listAdapter = null;
				checklayout.setVisibility(View.GONE);
				flag = 0;
				requestData(false);
			}else {
				mActivity.errorMsg(noticeModel.lastStatus.error_desc);
			}

		}

	}

	@Override
	public void onDestroyView() {
		listAdapter = null;
		super.onDestroyView();
	}

	@Override
	public void onRefresh(int id) {
		requestData(false);
	}

	@Override
	public void onLoadMore(int id) {
		page = page+1;
		noticeModel.getNoticeListMore(AppConst.info_from,type,page,true);
	//	warnModel.getNoticeListMore(info_from,type,page,true);
	}
}
