/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package ir.farsirib.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import ir.farsirib.Activity.FullScrVideoActivity;
import ir.farsirib.R;
import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarview.QueryPreferences;
import ir.farsirib.shenavarview.Video;
import ir.farsirib.utils.UICircularImage;

public class ItemThreeFragment extends Fragment {
    VideoView videoView;
    MediaController mediaController;
    Context context;
    UICircularImage fullScr_btn,multiWin_btn;

    public static ItemThreeFragment newInstance() {
        ItemThreeFragment fragment = new ItemThreeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_three, container, false);

        final String videoURL = "http://cdn1.live.irib.ir:1935/channel-live/smil:fars/playlist.m3u8";
        //final String videoURL = "http://s2.tv.asandl.com:1935/e-tv/fars-300k.stream/playlist.m3u8";
        videoView = rootView.findViewById(R.id.video_view1);

        mediaController = new MediaController(context);
        //Uri uri = Uri.parse("http://s10.telewebion.com:1935/devices/fars-300k.stream/playlist.m3u8?wmsAuthSign=aXNfZnJlZT0xJnNlcnZlcl90aW1lPTEwLzI2LzIwMTUgMTE6MjY6MzkgQU0maGFzaF92YWx1ZT1NamtPMjhqVjZjR1RmZE9rQ2NCSXdnPT0mdmFsaWRtaW51dGVzPTYwMDA=");
        Uri uri = Uri.parse(videoURL);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.start();



        fullScr_btn = rootView.findViewById(R.id.fullScr_btn);

        fullScr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   videoView.stopPlayback();

                Intent intent = new Intent(getContext(), FullScrVideoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", "");
                bundle.putString("img","" );
                bundle.putString("descr", "");
                bundle.putString("video_url",videoURL);
                bundle.putString("caller_context","ItemThreeFragment");

                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();
            }
        });

        multiWin_btn = rootView.findViewById(R.id.multiwin_btn);
        multiWin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && QueryPreferences.getPermissionStatus(context)!=null && QueryPreferences.getPermissionStatus(context).equals("OK")){
                    StandOutWindow.videoUrl = videoURL;
                    StandOutWindow.title = "سیمای فارس";
                    StandOutWindow.show(context, Video.class, StandOutWindow.DEFAULT_ID);
                }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    StandOutWindow.videoUrl = videoURL;
                    StandOutWindow.title = "سیمای فارس";
                    StandOutWindow.show(context, Video.class, StandOutWindow.DEFAULT_ID);
                }
            }
        });



        WebView webView = rootView.findViewById(R.id.webView1);

        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.clearCache(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webView.loadUrl("http://www.shahreraz.com/mob/kondaktor/index.php/kondaktor/fetch_data/1");
        webView.loadUrl("http://www.shahreraz.com/mob/FarsApp/index.php/kondaktor/fetch_data/1");

        return rootView;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {
        super.onResume();
        videoView.start();
    }


//    @Override
//    public void onPause() {
//        super.onPause();
//        videoView.stopPlayback();
//    }
}
