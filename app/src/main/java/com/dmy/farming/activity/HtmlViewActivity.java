package com.dmy.farming.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.bee.activity.BaseActivity;
import com.dmy.farming.R;

public class HtmlViewActivity extends BaseActivity {

    TextView text_title;
    WebView webView;

    ImageView web_back;
    ImageView goForward;
    ImageView reload;
    ProgressBar web_progress;

    String link = "";
    String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.html_view);

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        title = intent.getStringExtra("title");


        View img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_title = (TextView) findViewById(R.id.text_title);

        web_progress = (ProgressBar) findViewById(R.id.web_progress);
        webView = (WebView) findViewById(R.id.pay_web);

        webView.loadUrl(link);
        //webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel://")) {
                    phoneNumber = url.replace("//", "");
                    m_handler.sendEmptyMessage(1);
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
        });

        webView.setWebChromeClient(new MyWebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        web_back = (ImageView) findViewById(R.id.web_back);
        web_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {

                }
            }
        });

        goForward = (ImageView) findViewById(R.id.goForward);
        goForward.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.goForward();

            }
        });

        reload = (ImageView) findViewById(R.id.reload);
        reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                webView.reload();
            }
        });
    }

    String phoneNumber = "";
    Handler m_handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse(phoneNumber);
                    intent.setData(data);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
            web_progress.setVisibility(View.VISIBLE);
            web_progress.setProgress(newProgress);
            if(newProgress >= 100) {
                web_progress.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String str) {
            super.onReceivedTitle(view, str);
            if(null != str && !view.getUrl().contains(str)){
                text_title.setText(str);
            }

        }
    }
}
