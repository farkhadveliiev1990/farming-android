package com.dmy.farming.api.model;

import android.content.Context;
import android.text.TextUtils;

import com.dmy.DataCleanManager;
import com.dmy.farming.AppConst;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.HELPCENTER;
import com.dmy.farming.api.helpcenterResponse;
import com.dmy.farming.api.serviceAgreementResponse;
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

public class ServiceAgreementModel extends BaseModel {

	public HELPCENTER data = new HELPCENTER();

	String fileName;
	public STATUS lastStatus = new STATUS();

	public ServiceAgreementModel(Context context) {
		super(context);
		fileName = "/serviceAgreement.dat";
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

	public void getServiceAgreement() {
		BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject jo, AjaxStatus status) {
				ServiceAgreementModel.this.callback(url, jo, status);
				try {
					if (jo != null)
					{
						serviceAgreementResponse response = new serviceAgreementResponse();
						response.fromJson(jo);
						lastStatus = response.status;
						if (response.status.error_code == 200) {
							data = response.data;
							fileSave();
						}
						ServiceAgreementModel.this.OnMessageResponse(url, jo, status);
					}
				} catch (JSONException e) {
				}
			}
		};


		Map<String, String> params = new HashMap<String, String>();
		try {
			JSONObject localItemObject = new JSONObject();
			localItemObject.put("info_from", AppConst.info_from);
			params.put("json", localItemObject.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		cb.url(ApiInterface.USER_SERVICEAGREEMENT).type(JSONObject.class).params(params);

		if (TextUtils.isEmpty(data.content)) {
			MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
			aq.progress(pd.mDialog).ajax(cb);
		} else {
			aq.ajax(cb);
		}
	}
}
