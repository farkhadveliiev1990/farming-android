package com.dmy.farming.api.model;

import android.content.Context;

import com.dmy.DataCleanManager;
import com.dmy.farming.R;
import com.dmy.farming.api.ApiInterface;
import com.dmy.farming.api.data.a_DIAGNOSTIC;
import com.dmy.farming.api.data.chat.a_FARMARTICLE;
import com.dmy.farming.api.diagnosticRequest;
import com.dmy.farming.api.diagnosticResponse;
import com.dmy.farming.api.farmarticleResponse;
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

public class DiagnosticModel extends BaseModel {

	public a_DIAGNOSTIC data = new a_DIAGNOSTIC();
	public String type;

	String fileName;
	public STATUS lastStatus = new STATUS();

	public DiagnosticModel(Context context) {
		super(context);
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
	public void getDiagnosticList(diagnosticRequest request, boolean bshow) {
			BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

				@Override
				public void callback(String url, JSONObject jo, AjaxStatus status) {
					DiagnosticModel.this.callback(url, jo, status);
					try {
						if (jo != null) {
							diagnosticResponse response = new diagnosticResponse();
							response.fromJson(jo);
							if (response.status.error_code == 200) {

								data.diagnostics.clear();

								if (response.data != null)
								{
									data.diagnostics = response.data;
								}
								fileSave();
								DiagnosticModel.this.OnMessageResponse(url, jo, status);
							}
						}
					} catch (JSONException e) {
						// TODO: handle exception
					}
				}
			};

		Map<String, String> params = new HashMap<String, String>();
		try {
			params.put("json", request.toJson().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		cb.url(ApiInterface.DIAGNOSTICLIST).type(JSONObject.class).params(params);
		MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
		aq.progress(pd.mDialog).ajax(cb);

		}

	}
