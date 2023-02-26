package com.dmy.farming.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.FarmingApp;
import com.dmy.farming.R;
import com.dmy.farming.activity.D01_QuestionDetailActivity;
import com.dmy.farming.activity.E01_MyQuestionActivity;
import com.dmy.farming.activity.E01_NoticeActivity;
import com.dmy.farming.activity.E01_NoticeDetailActivity;
import com.dmy.farming.adapter.DialogAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.QUESTION;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.data.chat.NOTICELIST;
import com.dmy.farming.api.model.MyQuestionModel;
import com.dmy.farming.api.model.NoticeModel;
import com.dmy.farming.view.SwipeMenuLayout;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.mcxtzhang.commonadapter.lvgv.CommonAdapter;
import com.mcxtzhang.commonadapter.lvgv.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
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


public class E01_MyQuestionFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	BaseActivity mActivity;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.e01_myquestion,null);
		mActivity = (BaseActivity) getActivity();

		questionModel = new MyQuestionModel(getActivity());
		questionModel.addResponseListener(this);

		initView(mainView);

		updateData();
		requestData(true);

		return mainView;
	}

	XListView list_black;
	View null_pager;
	int pos;
	private List<QUESTION> mDatas;
	CommonAdapter commonAdapter;
	MyQuestionModel questionModel;

	void initView(View mainView)
	{
		list_black = (XListView) mainView.findViewById(R.id.list_black);
		null_pager = mainView.findViewById(R.id.null_pager);

		list_black.setPullLoadEnable(false);
		list_black.setPullRefreshEnable(false);
		list_black.setXListViewListener(this, 1);
		list_black.setAdapter(null);

	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
        {

		}
	}

	private void updateData() {
		list_black.stopRefresh();
		list_black.stopLoadMore();
		int size = questionModel.data.questions.size();
		if (size > 0) {
			null_pager.setVisibility(View.GONE);

			mDatas = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				QUESTION question = new QUESTION();
				question.content = questionModel.data.questions.get(i).content;
				question.user_name = questionModel.data.questions.get(i).userName;
				question.head_url = questionModel.data.questions.get(i).headurl;
				question.create_time = questionModel.data.questions.get(i).createTime;
				question.comment_num = questionModel.data.questions.get(i).commentNum;
				question.problemType = questionModel.data.questions.get(i).problemType;
				question.page_view = Integer.parseInt(questionModel.data.questions.get(i).pageView);
				question.question_id = questionModel.data.questions.get(i).id;
				question.accept = Integer.parseInt(questionModel.data.questions.get(i).accept);
				question.provience = questionModel.data.questions.get(i).provience;
				question.city = questionModel.data.questions.get(i).city;
				question.district = questionModel.data.questions.get(i).district;
				question.img_url = questionModel.data.questions.get(i).imgUrl;
				mDatas.add(question);
			}
			final ImageLoader imageLoader = ImageLoader.getInstance();
			if (commonAdapter == null) {
				commonAdapter = new CommonAdapter<QUESTION>(getContext(), mDatas, R.layout.e01_my_question_item) {
					@Override
					public void convert(final ViewHolder holder, final QUESTION question, final int position) {
						//((SwipeMenuLayout)holder.getConvertView()).setIos(false);//这句话关掉IOS阻塞式交互效果
						if(question.content.length()>14){
							holder.setText(R.id.content, question.content.substring(0,14));
						}else{
							holder.setText(R.id.content, question.content);
						}
						holder.setText(R.id.time, question.create_time);
						holder.setText(R.id.position, question.city + question.district);
						String a[] = question.problemType.split(",");
						String tkeyword = "";
						for(int i = 0;i<a.length;i++){
							tkeyword =tkeyword+a[i]+"  ";
						}
						holder.setText(R.id.keyword,tkeyword);
						holder.setText(R.id.comment_num, question.comment_num);
						holder.setText(R.id.browse_num, String.valueOf(question.page_view));
						ImageView image = (ImageView) holder.getConvertView().findViewById(R.id.img_user);
						if (TextUtils.isEmpty(question.img_url))
							image.setVisibility(View.GONE);
						else {
							image.setVisibility(View.VISIBLE);
							if (question.img_url.contains(","))
								imageLoader.displayImage(question.img_url.substring(0,question.img_url.indexOf(",")),image, FarmingApp.options);
							else
								imageLoader.displayImage(question.img_url, image, FarmingApp.options);
						}
						//	holder.setText(R.id.img_user,question.img_url);
						if(question.accept==0){
							holder.setVisible(R.id.solve,false);
						}else{
							holder.setVisible(R.id.solve,true);
						}
						holder.setOnClickListener(R.id.myquestion, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getContext(),D01_QuestionDetailActivity.class);
								intent.putExtra("id",question.question_id);
								intent.putExtra("type","myquestion");
								intent.putExtra("answer", false);
								startActivity(intent);
							}
						});

						holder.setOnClickListener(R.id.btnDelete, new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								//在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
								((SwipeMenuLayout) holder.getConvertView()).quickClose();
								questionModel.myQuestionDelete(question.question_id,true);
								pos = position;

							}
						});
					}
				};
				list_black.setAdapter(commonAdapter);
			} else {
				commonAdapter.notifyDataSetChanged();
			}
			if (0 == questionModel.paginated.more) {
				list_black.setPullLoadEnable(false);
			} else {
				list_black.setPullLoadEnable(true);
			}
		} else {
			commonAdapter = null;
			list_black.setAdapter(null);
			null_pager.setVisibility(View.VISIBLE);
		}

	}

	String userphone = "";
	String page = "1";

	public void requestData(boolean bShow) {
		//request.city = AppUtils.getCurrCity(mActivity);
		userphone = SESSION.getInstance().sid;
		questionModel.getBuyList(AppConst.info_from,userphone,page,bShow);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.MYQUESTIONLIST)) {
			list_black.setRefreshTime();
			updateData();
		}else if (url.contains(ApiInterface.DELETEQUESTION)){
			if (questionModel.lastStatus.error_code==200){
				mActivity.errorMsg("删除成功");
				mDatas.remove(pos);
				commonAdapter.notifyDataSetChanged();
			}else
				mActivity.errorMsg(questionModel.lastStatus.error_desc);
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onRefresh(int id) {

	}

	@Override
	public void onLoadMore(int id) {
		questionModel.getBuyListMore(AppConst.info_from,userphone,page, true);
	}
}
