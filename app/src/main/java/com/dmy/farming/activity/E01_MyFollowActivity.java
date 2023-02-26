package com.dmy.farming.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.adapter.E01_MyFollowListAdapter;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class E01_MyFollowActivity extends BaseActivity implements OnClickListener, BusinessResponse {

	MainActivity mActivity;
	DictionaryModel followModel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e01_my_follow);

		followModel = new DictionaryModel(this);
		followModel.addResponseListener(this);
		initView();

	}

	XListView list_black;
	View null_pager;
	TextView add,edit;
	GridView gridView;
	E01_MyFollowListAdapter myFollowListAdapter;
	ArrayList<DICTIONARY> ad;

	void initView()
	{
		View img_back =  findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		//list_black = (XListView) findViewById(R.id.list_black);
		//list_black.setXListViewListener(this, 1);

		gridView= (GridView)findViewById(R.id.GridView1);

		add = (TextView) findViewById(R.id.add);
		add.setOnClickListener(this);
		edit = (TextView)findViewById(R.id.edit);
		edit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch(v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.add:
				if(checkLogined()) {
					intent = new Intent(this, ChooseCropActivity.class);
					ArrayList<String> attentname = new ArrayList<>();
					ArrayList<String> attentcode = new ArrayList<>();
					for (DICTIONARY dictionary : ad) {
						attentname.add(dictionary.name);
						attentcode.add(dictionary.aboutCode);
					}
					intent.putStringArrayListExtra("attentname", attentname);
					intent.putStringArrayListExtra("attentcode", attentcode);
					startActivity(intent);
				}
				break;
			case R.id.edit:
				if(checkLogined()) {
					intent = new Intent(this, E01_MyFollowEditActivity.class);
					startActivity(intent);
				}
				break;

		}
	}

	@Override
	protected void onResume() {
		updateData();
		requestData(true);
		super.onResume();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      	}

	private void updateData() {
		if (followModel.data.follow_type.size() > 0) {
			if (myFollowListAdapter == null) {
				myFollowListAdapter = new E01_MyFollowListAdapter(this, followModel.data.follow_type);
				gridView.setAdapter(myFollowListAdapter);
			} else {
				myFollowListAdapter.notifyDataSetChanged();
			}
		} else {
			myFollowListAdapter = null;
			//gridView.setAdapter(null);
		}
	}

	public void requestData(boolean bShow) {
		//request.city = AppUtils.getCurrCity(mActivity);
		followModel.followType(AppConst.info_from,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
		if (url.contains(ApiInterface.FOLLOWTYPE)) {
			ad =  followModel.data.follow_type;
			updateData();
		}
	}




	final static int REQUEST_MONEY = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (requestCode == REQUEST_MONEY) {

			}
		}
	}


}