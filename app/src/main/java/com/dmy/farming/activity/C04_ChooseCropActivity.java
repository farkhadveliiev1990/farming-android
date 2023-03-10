package com.dmy.farming.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dmy.farming.AppConst;
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
import java.util.HashMap;
import java.util.Map;

public class C04_ChooseCropActivity extends BaseActivity implements View.OnClickListener,BusinessResponse {
	ImageView img_back;
	GridView gridView;
	ListView list_crop;
	DictionaryModel dictionaryModel;
	CropTypeAdapter cropTypeAdapter;
	public ArrayList<DICTIONARY> crop_type = new ArrayList<DICTIONARY>();
	public ArrayList<DICTIONARY> crop_sub_type = new ArrayList<DICTIONARY>();
	int currentItem = 0;
	int currentSubItem = -1;
	Map<String,String> crop = new HashMap<>();
	Map<String,String> cropCode = new HashMap<>();
	String code;
	String textcode = "";
	String textname = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c04_choose_crop);

		list_crop = (ListView) findViewById(R.id.listview_crop);

		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setOnClickListener(this);
		gridView= (GridView)findViewById(R.id.GridView1);

		findViewById(R.id.text_sure).setOnClickListener(this);

		dictionaryModel = new DictionaryModel(this);
		dictionaryModel.addResponseListener(this);

		dictionaryModel.cropType(true);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.img_back:
				finish();
				break;
			case R.id.text_sure:
				if (!TextUtils.isEmpty(textcode)) {
					Intent intent = new Intent();
					intent.putExtra("crop", textname);
					intent.putExtra("crop_code",textcode);
					setResult(Activity.RESULT_OK, intent);
					finish();
				} else
                    errorMsg("???????????????");
				break;

		}
	}

	@Override
	protected void onResume() {
		//updateData();
		//requestData(true);
		super.onResume();

	}

	CropSubAdapter cropSubAdapter;

	private void updateData() {
			if (cropSubAdapter == null) {
				cropSubAdapter = new CropSubAdapter(this, crop_sub_type);
				gridView.setAdapter(cropSubAdapter);
				gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						currentSubItem = position; //????????????
						cropTypeAdapter.notifyDataSetChanged(); //??????adapter??????
					}
				});
			} else {
				cropSubAdapter.notifyDataSetChanged();
			}
	}

	public void requestData(String code_type,boolean bShow) {
		dictionaryModel.cropsubType(AppConst.info_from,code_type,bShow);
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		super.OnMessageResponse(url, jo, status);
			if (url.contains(ApiInterface.CROPTYPE)){
				if (dictionaryModel.data.crop_type.size() > 0)
				{
					crop_type = dictionaryModel.data.crop_type;
					updateCrop();
				}
			}else if (url.contains(ApiInterface.CROPSUB)){
				if (dictionaryModel.data.crop_sub_type.size() > 0)
				{
					crop_sub_type = dictionaryModel.data.crop_sub_type;
				}
				updateData();

			}

	}

	private void updateCrop() {
		if (dictionaryModel.data.crop_type.size() >= 0) {
			if (cropTypeAdapter == null) {
				cropTypeAdapter = new CropTypeAdapter(this, crop_type);
				list_crop.setAdapter(cropTypeAdapter);
				list_crop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						currentItem = position; //????????????
						cropSubAdapter = null;
						cropTypeAdapter.notifyDataSetChanged(); //??????adapter??????
						crop.put("crop","");
						cropCode.put("crop_code","");
					}
				});

			} else {
				cropTypeAdapter.notifyDataSetChanged();
			}
		}
		else {
			cropTypeAdapter = null;
			//gridView.setAdapter(null);
		}
	}


	class CropTypeAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;


		public CropTypeAdapter(Context context,ArrayList<DICTIONARY> datas) {
			mContext = context;
			dataList = datas;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
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


		String id;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.crop_button, null);
//				viewHolder.button = (Button) convertView.findViewById(R.id.btn);
				viewHolder.text = (TextView) convertView.findViewById(R.id.btn);

				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.text.setText(dataList.get(position).name);

			if (position == currentItem) {
				viewHolder.text.setTextColor(getResources().getColor(R.color.green));
				code = dataList.get(position).code;
				requestData(code, true);
			}else {
				viewHolder.text.setTextColor(getResources().getColor(R.color.text_grey));
			//	viewHolder.text.setEnabled(false);
			}

			return convertView;
		}


	}

	class ViewHolder {
		Button button;
		TextView content,id,text;

	}

	class ViewHolder1 {
		Button button;
		TextView content,id,text;

	}

	class CropSubAdapter extends BaseAdapter{

		ArrayList<DICTIONARY> dataList;
		Context mContext;

		public CropSubAdapter(Context context,ArrayList<DICTIONARY> datas) {
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

			final ViewHolder1 viewHolder1;
			if (convertView == null) {
				viewHolder1 = new ViewHolder1();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.choose_crop_button, null);
				viewHolder1.content = (TextView) convertView.findViewById(R.id.content);

				convertView.setTag(viewHolder1);
			}else {
				viewHolder1 = (ViewHolder1) convertView.getTag();
			}

			final DICTIONARY cat = getItem(position);
			if (cat != null)
			{
				viewHolder1.content.setText(cat.name);
				if (position == currentSubItem) {
					viewHolder1.content.setBackgroundResource(R.drawable.green);
					viewHolder1.content.setTextColor(getResources().getColor(R.color.white));
				}else {
					viewHolder1.content.setBackgroundResource(R.drawable.gray);
					viewHolder1.content.setTextColor(getResources().getColor(R.color.text_grey));
				}
				viewHolder1.content.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						viewHolder1.content.setBackgroundResource(R.drawable.green);
//						viewHolder1.content.setTextColor(getResources().getColor(R.color.white));

						currentSubItem = position; //????????????
						cropSubAdapter.notifyDataSetChanged(); //??????adapter??????
						textcode = cat.code;
						textname = cat.name;

						crop.put("crop",cat.name);
						cropCode.put("crop_code",cat.code);
					}
				});

			}

			return convertView;
		}



	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dictionaryModel.removeResponseListener(this);
	}

}


