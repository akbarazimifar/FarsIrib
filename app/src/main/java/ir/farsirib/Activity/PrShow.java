package ir.farsirib.Activity;

import android.os.AsyncTask;
import android.os.Handler;
import androidx.annotation.RequiresApi;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import ir.farsirib.R;

public class PrShow extends BaseActivity implements ObservableScrollViewCallbacks {

    private View mToolbarView;
    private ObservableScrollView mScrollView;
    private TextView textView2;
    public String booody;
    private WebView webView;
    public  String html;
    public Handler uiHandler = new Handler();
    public  String link_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pr_show);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));

        String Title_value="";

        Bundle title = getIntent().getExtras();
        Bundle link = getIntent().getExtras();
        if (title != null) {
            Title_value = title.getString("title");
        }
        if (link != null) {
            link_value = title.getString("link");
        }
        mScrollView = findViewById(R.id.scroll);
        textView2= findViewById(R.id.title_toolbar);
        textView2.setText(Title_value);

        //****************************************************************************************************************

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webview22);
        webView.setWebViewClient(new MyWebViewClient());
        Log.e("Xml", "22222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

        new BackgroundWorker().execute();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
/*        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);*/
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
                Document htmlDocument = Jsoup.connect(link_value).get();
                Elements element = htmlDocument.select("#content > div.container.main-content.irib-2-inner-tpl > div#main-content > div._row > div#column-1.portlet-column.col-xs-12.col-md-9.right-border-visible > div#layout-column_column-1.portlet-dropzone.row.portlet-column-content.portlet-column-content-only > div#p_p_id_101_INSTANCE_ugRs8EYqvk8c_.portlet-boundary.portlet-boundary_101_.portlet-static.portlet-static-end.portlet-borderless.portlet-asset-publisher > div.portlet-body > div.portlet-borderless-container > div.portlet-body > div > section.portlet.asset-full-content-style-1 > div.portlet-content.container > div.asset-full-content.default-asset-publisher.show-asset-title > div.asset-content");

                // replace body with selected element
                htmlDocument.body().empty().append(element.toString());
                final String html = htmlDocument.toString();

                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        booody="<html><head><title></title></head><body>"+html+"</body></html>";
                        booody = booody.replaceAll("/documents/", "http://fars.irib.ir/documents/");
                        booody = booody.replaceAll("<video", "<video id=\"video\" autobuffer height=\"240\" width=\"360\"  controls=\"control\"");
                        booody = booody.replaceAll("type=\"video/mp4\"", " ");
                        booody = booody.replaceAll("data-engine=\"html5\"", " ");
                        booody = booody.split("مطالب مرتبط ")[0];
                        booody=booody+"</body></html>";
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
