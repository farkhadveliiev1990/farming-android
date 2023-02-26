package com.dmy.farming.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.AppUtils;
import com.dmy.farming.R;
import com.dmy.farming.activity.B01_WarnDetailActivity;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.adapter.B00_WarningListAdapter;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.adapter.E01_NoticeListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.GLOBAL_DATA;
import com.dmy.farming.api.data.chat.WARNLIST;
import com.dmy.farming.api.model.NoticeModel;
import com.dmy.farming.api.model.RentResponse;
import com.dmy.farming.api.model.WarnModel;
import com.dmy.farming.api.warnRequest;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.umeng.socialize.UMShareAPI;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class E01_WarnFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;

	WarnModel warnModel;


	/**
	 * Created by wang on 2016/5/12.
	 * checkbox抽象模型，用来保存每一个checkbox的选中状态
	 */
	public static class CheckBoxBean {
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

	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/
		isCreate=true;

		warnModel = new WarnModel(getContext());
		warnModel.addResponseListener(this);
		wRequest = new warnRequest();

		initView(mainView);
		requestData(true);

		return mainView;
	}

	static XListView list_black;
	View null_pager;
	E01_NoticeListAdapter listAdapter;
	static B00_WarningListAdapter warningListAdapter;
	warnRequest wRequest;
	LinearLayout deleted;
	private static final String TAG = "批量删除";
	private CheckBox checkBox;
	private TextView layout_deleted,cancel;
	private List<String> list = new ArrayList<>();
	public static List<CheckBoxBean> boxList = new ArrayList<>();
	public LinearLayout checklayout;
	RelativeLayout checkBox_layout;
	static int flag = 0;

	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);

//		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		//list_black.addFooterView(footerView);
		layout_deleted = (TextView) mainView. findViewById(R.id.layout_deleted);
		layout_deleted.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteCheckedItem();
			}
		});
		//cancel = (TextView)mainView.findViewById(R.id.cancel);
		checklayout = (LinearLayout) mainView.findViewById(R.id.checklayout);
	//	checkBox_layout = (RelativeLayout)mainView.findViewById(R.id.checkBox_layout) ;

	/*	deleted = (LinearLayout)getActivity().findViewById(R.id.deleted);
		deleted.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int a = ((E01_NoticeActivity)getActivity()).cur_tab;
				if (((E01_NoticeActivity)getActivity()).cur_tab == 1) {
					if (warnlist.size() > 0) {
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
	/*	cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				checklayout.setVisibility(View.GONE);
		//		checkBox_layout.setVisibility(View.GONE);
				flag = 0;
				ad();
			}
		});*/

		checkBox = (CheckBox) mainView.findViewById(R.id.checkBox);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectAll(isChecked);
				warningListAdapter.notifyDataSetChanged();
			}
		});

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(true);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);
	}

/*	public void  ad(){
		if (warnlist.size() > 0) {
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
		initBoxBeanList();
		warningListAdapter = new B00_WarningListAdapter(getContext(),warnlist,boxList,flag);
		list_black.setAdapter(warningListAdapter);
	}*/

	public  void  initBoxBeanList() {
		if (warnlist.size() > 0) {
			boxList.clear();
			for (int i = 0; i < warnlist.size(); i++) {
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
		warningListAdapter = new B00_WarningListAdapter(getContext(),warnlist,boxList,flag);
		list_black.setAdapter(warningListAdapter);
	}

	/**
	 * 删除选中项
	 */

	private void deleteCheckedItem() {
		int count = warnlist.size() - 1 ;
		String id = "";
		String status = "";
		//从大都小遍历，否则，从小到大遍历的话会出现错位
		for (int i = count ; i >= 0 ; i--) {
			if (boxList.get(i).isChecked()){
				id+=warnlist.get(i).id+",";
				status+= warnlist.get(i).readStatus+",";
			}
		}
		if(status.contains("0")){
			showDialog1("您有未读消息确定删除吗？",id);
		}else{
			checklayout.setVisibility(View.GONE);
			checkBox.setChecked(false);
			deleteNotice(id);
		}

		//如果是全部删除,全选框设为未选中
		if (warnlist.size()==0){
			checklayout.setVisibility(View.GONE);
			checkBox.setChecked(false);
		}

		warningListAdapter.notifyDataSetChanged();
	}

	/**
	 * 单项中选中状态改变时，由adpater回调
	 * @param position
	 * @param isChecked
	 */
	public void checkedStateChange(int position, boolean isChecked) {
		boxList.get(position).setChecked(isChecked);
		warningListAdapter.notifyDataSetChanged();
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
	public void onClick(View v) 
	{
		switch(v.getId())
        {

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
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser && isCreate) {
			//相当于Fragment的onResume
			requestData(true);
		} else {
			//相当于Fragment的onPause
		}
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

 public	static ArrayList<WARNLIST>  warnlist;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();

		if (warnModel.data.routes.size() > 0) {
			null_pager.setVisibility(View.GONE);
			warnlist = warnModel.data.routes;
			if (warningListAdapter == null) {
				warningListAdapter = new B00_WarningListAdapter(getContext(), warnModel.data.routes,boxList,flag);
				list_black.setAdapter(warningListAdapter);
			} else {
				warningListAdapter.notifyDataSetChanged();
			}
			if (0 == warnModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			warningListAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	public  class B00_WarningListAdapter extends BaseAdapter implements View.OnClickListener {

		Context mContext;
		ArrayList<WARNLIST> dataList;
		private List<CheckBoxBean> boxBeanList;
		private int isflag;


		public  B00_WarningListAdapter(Context context, ArrayList<WARNLIST> dataList,List<CheckBoxBean> boxBeanList,int isflag ) {
			this.mContext = context;
			this.dataList = dataList;
			this.isflag = isflag;
			this.boxBeanList = boxBeanList;
		}

		public WARNLIST getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public void onClick(View v) {

		}

		private class ViewHolder {
			public View layout_frame;
			public TextView title,levels,content,time,detail,readstatus;
			public ImageView type;
			CheckBox checkBox;

		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.b00_warning_item, null,false);

				viewHolder.layout_frame = convertView.findViewById(R.id.layout_frame);
				//viewHolder.img_route = (ImageView) convertView.findViewById(R.id.img_route);
				viewHolder.title = (TextView) convertView.findViewById(R.id.title);
				viewHolder.type = (ImageView) convertView.findViewById(R.id.type);
				viewHolder.content = (TextView) convertView.findViewById(R.id.content);
				viewHolder.time = (TextView) convertView.findViewById(R.id.time);
				viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
				viewHolder.readstatus = (TextView) convertView.findViewById(R.id.readstatus);
				viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox_item);
				//viewHolder.address_detail = (TextView) convertView.findViewById(R.id.address_detail);

//			android.view.ViewGroup.LayoutParams params1 = viewHolder.img_route.getLayoutParams();
//			params1.width = (AppUtils.getScWidth(mContext));
//			params1.height = (int) (params1.width*1.0/720*330);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final WARNLIST cat = getItem(position);
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
				switch (cat.warning_type){
					case "虫害":
						viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_pest));
						break;
					case "病害":
						viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_disease));
						break;
					case "植保":
						viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_zhibao));
						break;
					case "气象":
						viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_weather));
						break;
				/*case "CHONGHAI_CODE":
					viewHolder.type.setBackground(convertView.getResources().getDrawable(R.drawable.warning_disease));
					break;*/

				}

				if(cat.content.length()>20){
					viewHolder.content.setText((Html.fromHtml(cat.content)));
				}else {
					viewHolder.content.setText(Html.fromHtml(cat.content));
				}
				if(cat.readStatus.equals("0")){
					viewHolder.readstatus.setText("[未读]");
					viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.text_red));
				}else{
					viewHolder.readstatus.setText("[已读]");
					viewHolder.readstatus.setTextColor(mContext.getResources().getColor(R.color.green));
				}
				viewHolder.time.setText(AppUtils.time(cat.publishTime));
				//	viewHolder.levels.setText(cat.levels);
				//imageLoader.displayImage(cat.img_url, viewHolder.img_route, FarmingApp.options_long_with_text);
				viewHolder.layout_frame.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, B01_WarnDetailActivity.class);
						intent.putExtra("id", cat.id);
						startActivityForResult(intent, REQUEST_MONEY);

					}

				});

				viewHolder.layout_frame.setOnLongClickListener(new View.OnLongClickListener() {
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



	public  void deleteNotice(String id)
	{
		warnModel.deleteWarn(id);
	}


	int page = 1;
	String type = "0";

	public void requestData(boolean bShow)
	{
		//request.city = AppUtils.getCurrCity(mActivity);
		wRequest.city = GLOBAL_DATA.getInstance(mActivity).currCity;
		wRequest.district = AppUtils.getCurrDistrict(mActivity);
		wRequest.province = AppUtils.getCurrProvince(mActivity);
		wRequest.info_from = AppConst.info_from;
		wRequest.page = 1;
		warnModel.getWarnMessageList(wRequest,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.WARN)) {
			list_black.setRefreshTime();
			updateData();
		}else if (url.contains(ApiInterface.deleteWaring)){
			if (warnModel.lastStatus.error_code == 200){
				mActivity.errorMsg("删除成功");
				listAdapter = null;
				checklayout.setVisibility(View.GONE);
				flag = 0;
				requestData(false);
			}else {
				mActivity.errorMsg(warnModel.lastStatus.error_desc);
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
		/*wRequest.city = AppUtils.getCurrCity(mActivity);
		wRequest.district = AppUtils.getCurrDistrict(mActivity);
		wRequest.province = AppUtils.getCurrProvince(mActivity);*/
		wRequest.info_from = AppConst.info_from;
		page = page+1;
		wRequest.page = page;
		warnModel.getWarnListMore(wRequest, true);
	}
}
