package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.DICTIONARY;
import com.dmy.farming.api.model.DictionaryModel;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.activity.BaseActivity;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChooseCropTypeActivity extends BaseActivity implements View.OnClickListener,BusinessResponse {

	ImageView img_back;
	GridView gridView;
	DictionaryModel dictionaryModel;
	public ArrayList<DICTIONARY> crop_type = new ArrayList<DICTIONARY>();
	TextView text_finish;
    String crop = "";
    String crop_code = "";
	int currentItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_crop_type);

		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);

		gridView = (GridView)findViewById(R.id.gridView);

		text_finish = (TextView)findViewById(R.id.text_finish);
		text_finish.setOnClickListener(this);

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		dictionaryModel.cropType(false);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_finish:
				if (currentItem >= 0) {
					crop = crop_type.get(currentItem).name;
					crop_code = crop_type.get(currentItem).code;
				}
				Intent intent = new Intent();
				intent.putExtra("crop",crop);
				intent.putExtra("crop_code",crop_code);
				setResult(Activity.RESULT_OK,intent);
				finish();
				break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	CropAdapter cropAdapter;

	private void updateData() {
		if (dictionaryModel.data.crop_type.size() > 0) {
			if (cropAdapter == null) {
				cropAdapter = new CropAdapter(this, crop_type);
				gridView.setAdapter(cropAdapter);

			} else {
				cropAdapter.notifyDataSetChanged();
			}
		}else {
			cropAdapter = null;
			gridView.setAdapter(null);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
		if (url.contains(ApiInterface.CROPTYPE)){
			if (dictionaryModel.data.crop_type.size() > 0)
			{
				crop_type = dictionaryModel.data.crop_type;
				updateData();
			}

		}

	}


	class ViewHolder {
		Button button;
		TextView content,id,text;

	}

	class CropAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;
		boolean select = false;

		public CropAdapter(Context context,ArrayList<DICTIONARY> datas) {
			mContext = context;
			dataList = datas;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public DICTIONARY getItem(int position) {
			if (position >= 0 && position < dataList.size())
				return dataList.get(position);
			else
				return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_question_button, null);
				viewHolder.button = (Button) convertView.findViewById(R.id.btn);

				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			DICTIONARY cat = getItem(position);
			if (cat != null)
			{
				viewHolder.button.setText(cat.name);

			}
			if (position == currentItem){
				viewHolder.button.setTextColor(getResources().getColor(R.color.green));
				viewHolder.button.setSelected(true);
			}else {
				viewHolder.button.setTextColor(getResources().getColor(R.color.black));
				viewHolder.button.setSelected(false);
			}

			viewHolder.button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					currentItem = position; //重新赋值
					cropAdapter.notifyDataSetChanged(); //通知adapter更新
				}
			});

			return convertView;
		}



	}




}


