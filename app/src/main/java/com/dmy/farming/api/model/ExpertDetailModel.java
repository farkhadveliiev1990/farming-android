package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.CollectResponse;
import com.dmy.farming.api.data.EXPERTINFO;
import com.dmy.farming.api.data.SESSION;
import com.dmy.farming.api.expertDetailResponse;
import com.dmy.farming.protocol.STATUS;
import com.external.androidquery.callback.AjaxStatus;

import org.bee.model.BaseModel;
import org.bee.model.BeeCallback;
import org.bee.view.MyProgressDialog;
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

public class ExpertDetailModel extends BaseModel {

	public EXPERTINFO data = new EXPERTINFO();

	public STATUS lastStatus;

	public String expert_id;

	String fileName;

	public ExpertDetailModel(Context context, String expert_id) {
		super(context);

		this.expert_id = expert_id;
		fileName = "/expertD_" + expert_id +".dat";
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
	public void getExpertDetail(String id) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ExpertDetailModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						expertDetailResponse response = new expertDetailResponse();
						response.fromJson(jo);
						if (response.status.error_code == 200) {
							data.fromJson(response.data.toJson());
							fileSave();
							ExpertDetailModel.this.OnMessageResponse(url, jo, status);
						}
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject object = new JSONObject();
			object.put("userid", SESSION.getInstance().uid);
			object.put("id",id);

			params.put("json", object.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.EXPERTDETAIL).type(JSONObject.class).params(params);
		MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
		aq.progress(pd.mDialog).ajax(cb);
	}

	public void collection(String id) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ExpertDetailModel.this.callback(url, jo, status);
				try {
					if (jo != null)
					{
						CollectResponse response = new CollectResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.error_code == 200) {
							fileSave();
						}
						ExpertDetailModel.this.OnMessageResponse(url, jo, status);
					}
				} catch (JSONException e) {
				}
			}
		};

		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject itemObject = new JSONObject();
			itemObject.put("info_from", AppConst.info_from);
			itemObject.put("collection_type", "0");
			itemObject.put("userid", SESSION.getInstance().uid);
			itemObject.put("collection_id", id);

			params.put("json", itemObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.COLLECTION).type(JSONObject.class).params(params);
		aq.ajax(cb);
	}

	public void cancelcollection(String id) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ExpertDetailModel.this.callback(url, jo, status);
				try {
					if (jo != null)
					{
						CollectResponse response = new CollectResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.error_code == 200) {
							fileSave();
						}
						ExpertDetailModel.this.OnMessageResponse(url, jo, status);
					}
				} catch (JSONException e) {
				}
			}
		};

		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject itemObject = new JSONObject();
			itemObject.put("info_from", AppConst.info_from);
			itemObject.put("collection_type", "0");
			itemObject.put("userid", SESSION.getInstance().uid);
			itemObject.put("collection_id", id);

			params.put("json", itemObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.CANCELCOLLECTION).type(JSONObject.class).params(params);
		aq.ajax(cb);
	}

	/*public void enterActivity(final enterActivityRequest request) {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ActivityDetailModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						enterActivityResponse response = new enterActivityResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.succeed == 1) {
							order = response.data;
						}
						ActivityDetailModel.this.OnMessageResponse(url, jo, status);
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		request.session = SESSION.getInstance();
		request.activity_id = activity_id;

		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("json", request.toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.setParams(ApiInterface.ACTIVITY_ENTER, params).type(JSONObject.class);
		aq.ajax(cb);
	}*/

	/*public void enterPay(String order_sn) {
		enterPayRequest request = new enterPayRequest();
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ActivityDetailModel.this.callback(url, jo, status);
				try {
					if (jo != null) {
						enterActivityResponse response = new enterActivityResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.succeed == 1) {
							order = response.data;
						}
						ActivityDetailModel.this.OnMessageResponse(url, jo, status);
					}

				} catch (JSONException e) {
					// TODO: handle exception
				}
			}
		};

		request.session = SESSION.getInstance();
		request.order_sn = order_sn;

		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("json", request.toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.setParams(ApiInterface.ACTIVITY_ENTER_PAY, params).type(JSONObject.class);
		aq.ajax(cb);
	}*/
}
