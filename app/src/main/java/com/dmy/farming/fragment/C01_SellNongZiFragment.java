package com.dmy.farming.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.dmy.farming.R;
import com.dmy.farming.adapter.C00_MySellListAdapter;
import com.external.activeandroid.util.Log;
import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;

import org.bee.activity.BaseActivity;
import org.bee.fragment.BaseFragment;
import org.bee.model.BusinessResponse;
import org.json.JSONException;
import org.json.JSONObject;


public class C01_SellNongZiFragment extends BaseFragment implements OnClickListener, BusinessResponse, XListView.IXListViewListener {
	final static String TAG = "route fragment";
	BaseActivity mActivity;

/*	RouteListModel dataModel;
	int BUFFER_TIME = 30 * 60 * 1000;

	routeListRequest request;*/

	private static final String LOG_TAG = "WebViewDemo";
	private WebView mWebView;
	private Handler mHandler = new Handler();


	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.c01_sell_nongzi_item, null);
		mActivity = (BaseActivity) getActivity();

	/*	request = new routeListRequest();
		dataModel = new RouteListModel(mActivity, tag_id);
		dataModel.addResponseListener(this);*/

	//	initView(mainView);


		//Toast.makeText(mActivity.this,"HELLOW WORLD", Toast.LENGTH_LONG);

		 mWebView = (WebView) mainView.findViewById(R.id.webView);


		 WebSettings webSettings = mWebView.getSettings();
		 webSettings.setSavePassword(false);
		 webSettings.setSaveFormData(false);
		 // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
		 webSettings.setJavaScriptEnabled(true);
		 webSettings.setSupportZoom(false);


		 mWebView.setWebChromeClient(new MyWebChromeClient());

		 // 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
		 // 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
		mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "asdasd");
		mWebView.addJavascriptInterface(new myHellowWorld(),"my");

		mWebView.loadUrl("file:///android_asset/sell.html");
		return mainView;
	}

	XListView list_news;
	View null_pager;
	View footerView,mFooter;
	C00_MySellListAdapter listAdapter;

	void initView(View mainView)
	{
		null_pager = mainView.findViewById(R.id.null_pager);
		list_news = (XListView) mainView.findViewById(R.id.list_news);

		footerView = LayoutInflater.from(mActivity).inflate(R.layout.c01_sell_item, null);
		//mFooter = footerView.findViewById(R.id.mFooter);
		list_news.addFooterView(footerView);

		list_news.setPullLoadEnable(false);
		list_news.setPullRefreshEnable(true);
		list_news.setXListViewListener(this, 1);
		list_news.setAdapter(null);
	}

	class myHellowWorld{
          myHellowWorld(){

			        }
          public void show(){
			            mHandler.post(new Runnable(){

		              @Override
              public void run() {
					               //  Toast.makeText(MyActivity.this,"HELLOW WORLD", Toast.LENGTH_LONG).show();
					             }
          });
		  }
      }

	// 这是他定义由 addJavascriptInterface 提供的一个Object
      final class DemoJavaScriptInterface {
         DemoJavaScriptInterface() {
			       }

		         /**
  72          * This is not called on the UI thread. Post a runnable to invoke
  73          * 这不是呼吁界面线程。发表一个运行调用
  74          * loadUrl on the UI thread.
  75          * loadUrl在UI线程。
  76          */
		         public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
			mHandler.post(new Runnable() {
                public void run() {
				                     // 此处调用 HTML 中的javaScript 函数
					                    mWebView.loadUrl("javascript:wave()");
					                 }
             });
			         }
      }


	final class MyWebChromeClient extends WebChromeClient {
         @Override
          public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			             Log.d(LOG_TAG, message);
			            result.confirm();
			             return true;
			        }
     }



	@Override
	public void onClick(View v) 
	{
		switch(v.getId())
        {
		}
	}

	public void requestData(boolean bShow)
	{
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	private void updateData() {

	}


	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {
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

	}
}
