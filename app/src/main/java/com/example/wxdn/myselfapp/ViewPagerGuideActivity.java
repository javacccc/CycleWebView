package com.example.wxdn.myselfapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.example.wxdn.myselfapp.ScollHandler.MSG_UPDATE_IMAGE;

/*************************************************
 *@date：2017/7/4
 *@author：  zxj
 *@description： 轮转界面的实现与处理
*************************************************/
public class ViewPagerGuideActivity extends Activity {
	public ViewPager viewPager;
	/**装分页显示的view的数组*/
	public ArrayList<View> pageViews;
	private ImageView imageView;
	/**将小圆点的图片用数组表示*/
	private ImageView[] imageViews;
	//包裹滑动图片的LinearLayout
	private ViewGroup viewPics;
	//包裹小圆点的LinearLayout
	private ViewGroup viewPoints;
	public ScollHandler handler = new ScollHandler(new WeakReference<ViewPagerGuideActivity>(this));
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置无标题栏
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();//将要分页显示的View装入数组中
		addWebView(pageViews, "http://start.firefoxchina.cn/");
		addWebView(pageViews, "http://start.firefoxchina.cn/");
        imageViews = new ImageView[pageViews.size()];//创建imageviews数组，大小是要显示的图片的数量
        viewPics = (ViewGroup) inflater.inflate(R.layout.view_pics, null);//从指定的XML文件加载视图
        //实例化小圆点的linearLayout和viewpager
		//控件绑定
		viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
        //添加小圆点的图片
        for(int i=0;i<pageViews.size();i++){
        	imageView = new ImageView(ViewPagerGuideActivity.this);
        	//设置小圆点imageview的参数
        	imageView.setLayoutParams(new LayoutParams(20,20));//创建一个宽高均为20 的布局
        	imageView.setPadding(20, 0, 20, 0);
        	//将小圆点layout添加到数组中
        	imageViews[i] = imageView;
        	//默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
        	if(i==0){
        		imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
        	}else{
        		imageViews[i].setBackgroundResource(R.drawable.page_indicator);
        	}
        	viewPoints.addView(imageViews[i]);//将imageviews添加到小圆点视图组
        }
        //显示滑动图片的视图
        setContentView(viewPics);
        //设置viewpager的适配器和监听事件
        viewPager.setAdapter(new GuidePageAdapter());
        viewPager.setOnPageChangeListener(new GuidePageChangeListener());
		viewPager.setCurrentItem(0);//默认在第一个页面中间
		//开始轮播效果
		handler.sendEmptyMessageDelayed(ScollHandler.MSG_BREAK_SILENT, ScollHandler.MSG_DELAY);
	}
	@Override
	public void onResume()
	{
		super.onResume();
		handler.sendEmptyMessageDelayed(ScollHandler.MSG_BREAK_SILENT, ScollHandler.MSG_DELAY);
	}
	/*************************************************
     *@description： 适配器，页面位置的更新
    *************************************************/
    public class GuidePageAdapter extends PagerAdapter{
    	//销毁position位置的界面
		@Override
		public void destroyItem(View v, int position, Object arg2) {
			((ViewPager)v).removeView(pageViews.get(position));
		}
		@Override
		public void finishUpdate(View arg0) {
		}
		//获取当前窗体界面数
		@Override
		public int getCount() {
			return pageViews.size();
		}
		//初始化position位置的界面
		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(pageViews.get(position));
            return pageViews.get(position);
		}
		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View v, Object arg1) {
			return v == arg1;
		}
		@Override
		public void startUpdate(View arg0) {
			
		}
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			
		}
		@Override
		public Parcelable saveState() {
			return null;
		}
    }
    /*************************************************
     *@description： 修改当前下面点的切换，和监听相应的操作
    *************************************************/
    public class GuidePageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
				case ViewPager.SCROLL_STATE_DRAGGING:
					handler.sendEmptyMessage(ScollHandler.MSG_KEEP_SILENT);
					break;
				case ViewPager.SCROLL_STATE_IDLE:
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, ScollHandler.MSG_DELAY);
					break;
				default:
					break;
			}
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int position) {
			handler.sendMessage(Message.obtain(handler, ScollHandler.MSG_PAGE_CHANGED, position, 0));
			for(int i=0;i<imageViews.length;i++){
				imageViews[position].setBackgroundResource(R.drawable.page_indicator_focused);//不是当前选中的page，其小圆点设置为未选中的状态
				if(position !=i){
					imageViews[i].setBackgroundResource(R.drawable.page_indicator);
				}
			}
		}
    }
    /*************************************************
     *@description：触摸事件的监听器
	 * 触摸时停止，触摸时滑动
    *************************************************/
	View.OnTouchListener onTouchListener=new View.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					handler.sendEmptyMessage(ScollHandler.MSG_KEEP_SILENT);
					break;
				case MotionEvent.ACTION_UP:
					handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, ScollHandler.MSG_DELAY);
					break;
				default:
					break;
			}
			return false;
		}
	};
    /*************************************************
     *@description： 添加相应的WebView，以及相应的适配问题
    *************************************************/
	private void addWebView(List<View> viewList, String url)
	{
		WebView webView=new WebView(this);
		webView.loadUrl(url);
		webView.setOnTouchListener(onTouchListener);//监听触摸事件
		setWebViewSetting(webView);
		/*************************************************
		 *@description： 设置使用WebView来直接打开新的链接
		*************************************************/
		webView.setWebViewClient(new WebViewClient() {
			//在webview里打开新链接
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent intent=new Intent(ViewPagerGuideActivity.this,NextWebView.class);
				intent.putExtra("url",url);
				startActivity(intent);
				handler.sendEmptyMessage(ScollHandler.MSG_KEEP_SILENT);
				return true;
			}
		});
		viewList.add(webView);
	}
	/*************************************************
	 *@description： 设置WebView的WebSetting
	*************************************************/
	private void setWebViewSetting(WebView webView)
	{
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);//设定支持viewport
		webSettings.setLoadWithOverviewMode(true);   //自适应屏幕
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true);/////设置支持javascript
		webSettings.setSupportZoom(true);//设定支持缩放
	}
}