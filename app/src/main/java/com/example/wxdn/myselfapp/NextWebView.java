package com.example.wxdn.myselfapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

/*************************************************
 *@date： 2017/7/6
 *@author： zxj
 *@description： 处理轮转界面点击的界面
 *************************************************/
public class NextWebView extends AppCompatActivity implements View.OnClickListener {
    private Button btn_back;
    private RelativeLayout btrl;
    private WebView wView;
    private int timeKeeper=600000;//定时器的时间，以毫秒为单位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_nextwebview);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titleback);
        bindViews();
        setTime();//设置计时器
    }
    /*************************************************
     *@description： 绑定数据
    *************************************************/
    private void bindViews() {
        btn_back = (Button) findViewById(R.id.btn_back);
        wView = (WebView) findViewById(R.id.wView);
        btn_back.setOnClickListener(this);
        btrl=(RelativeLayout)findViewById(R.id.btrl);
        btrl.setVisibility(View.GONE);
        btn_back.setVisibility(View.GONE);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        wView.loadUrl(url);
        wView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        setWebViewSetting(wView);
    }
    /*************************************************
     *@description： 返回事件禁听
    *************************************************/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
    /*************************************************
     *@description： 设置WebView的WebSetting
     *************************************************/
    private void setWebViewSetting(WebView webView)
    {
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setUseWideViewPort(true);//设定支持viewport
//        webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setSupportZoom(true);//设定支持缩放
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设定支持viewport
        webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);/////设置支持javascript
        webSettings.setSupportZoom(true);//设定支持缩放

    }
    /*************************************************
     *@description： 定时操作方法
    *************************************************/
    private void setTime()
    {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                handler.postDelayed(this, timeKeeper);
                finish();
            }
        };
        handler.postDelayed(runnable, timeKeeper);// 打开定时器，执行操作
    }

}
