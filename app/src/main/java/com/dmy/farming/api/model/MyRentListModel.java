package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.a_MYRENT_LIST;
import com.dmy.farming.api.myRentResponse;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyRentListModel extends BaseModel {
	public a_MYRENT_LIST data = new a_MYRENT_LIST();
	public int type;

	int PAGE_COUNT = 10;
	String fileName;

	public MyRentListModel(Context context, int type) {
		super(context);

		this.type = type;
		fileName = "/myRent_" + type +".dat";
		readCache();
	}

	public void readCache() {
		String path = DataCleanManager.getCacheDir(mContext) + fileName;
		File f1 = new File(path);
		if (f1.exists()) {
			try {
				InputStream is = new FileInputStream(f1);
				InputStreamReader input = new InputStreamReader(is, "UTF-8");
				BufferedReader bf = new BufferedReader(input);

				parseCache(bf.readLine());

				bf.close();
				input.close();
				is.close();

			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void parseCache(String result) {
		try {
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				data.fromJson(jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 缓存数据
	private PrintStream ps = null;

	public void fileSave() {
		data.lastUpdateTime = System.currentTimeMillis();

		String path = DataCleanManager.getCacheDir(mContext);
		File filePath = new File(path);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}

		File file = new File(filePath + fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			ps = new PrintStream(fos);
			ps.print(data.toJson().toString());
			ps.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	///////////////////////////
	public void getMyRentList(String info_from,String crop_code,int page,int this_app,String publish_user) {

		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				MyRentListModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						myRentResponse response = new myRentResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
							data.paginated = response.paginated;
							data.data.clear();
							if (response.data.size() > 0)
							{
								data.data.addAll(response.data);
							}
							fileSave();
							MyRentListModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		String url = "/" + info_from+"/"+ crop_code+ "/" + page + "/" + this_app + "/" + publish_user;

		cb.url(ApiInterface.MYRENT + url).type(JSONObject.class);
		aq.ajax(cb);
	}

	public void getMyRentListMore(String info_from,int page,int this_app,String publish_user) {

		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				MyRentListModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						myRentResponse response = new myRentResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
							data.paginated = response.paginated;
							if (response.data.size() > 0) {
								data.data.addAll(response.data);
							}
							fileSave();
							MyRentListModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		String url = "/" + info_from + "/" + page + "/" + this_app + "/" + publish_user;

		cb.url(ApiInterface.MYRENT + url).type(JSONObject.class);
		aq.ajax(cb);
	}
}
