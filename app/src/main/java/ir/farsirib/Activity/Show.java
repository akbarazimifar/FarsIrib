package ir.farsirib.Activity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import ir.farsirib.R;

public class Show extends BaseActivity implements ObservableScrollViewCallbacks {


    private View mImageView;
    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private TextView textView2;
    private TextView body;
    public String booody;
    private  WebView webView;
    public  String data_link;
    public  String html;
    public String Link;
    public  Handler uiHandler = new Handler();
//***************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mImageView = findViewById(R.id.image);
        mToolbarView = findViewById(R.id.toolbar);
        textView2 = findViewById(R.id.title_toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
        Bundle bundle = getIntent().getExtras();
        mScrollView = findViewById(R.id.scroll);
        body = findViewById(R.id.body);


        //****************************************************************************************************************

        html=bundle.getString("link_page");

        webView = findViewById(R.id.webview22);
        webView.setWebViewClient(new MyWebViewClient());

        new BackgroundWorker().execute();
        /*TEEEEEEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT*/


        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView2.setText(bundle.getString("title"));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @RequiresApi(21)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(html.toString());
            return false;
        }
    }

    private class BackgroundWorker extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            getDarewod();
            return null;
        }

        public void getDarewod() {

            try {
                Document htmlDocument = Jsoup.connect(html).get();
                Element element = htmlDocument.select("html > body#news > div.home_logo > div.container > div.page > div.row > div.col-md-25.col-sm-36.col_padd_l.news_general_dl > div > div.body.body_media_content_show").first();

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        booody="<html><head><title></title></head><body>"+html+"</body></html>";
                        booody = booody.replaceAll("/files/fa/", "http://fars.iribnews.ir/files/fa/");
                        booody = booody.replaceAll("<video", "<video id=\"video\" autobuffer height=\"240\" width=\"360\"  controls=\"control\"");
                        booody = booody.replaceAll("type=\"video/mp4\"", " ");
                        booody = booody.replaceAll("data-engine=\"html5\"", " ");
                        booody = booody.replaceAll("ط¯ط§ظ†ظ„ظˆط¯", " ");
                        booody = booody.replaceAll("flowplayer.conf", " ");
                        booody = booody.replaceAll("flowplayer.conf", " ");
                        booody = booody.replaceAll("$('.player_1199498').flowplayer();", " ");

                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebViewClient(new WebViewClient());
                        webView.setWebChromeClient(new WebChromeClient());

                        webView.loadDataWithBaseURL(null, booody, "text/html", "UTF-8", null);

                        webView.getSettings().setUseWideViewPort(true);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setBuiltInZoomControls(true);
                        webView.setWebChromeClient(new WebChromeClient());


                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
