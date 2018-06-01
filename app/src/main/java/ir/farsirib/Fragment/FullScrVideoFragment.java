package ir.farsirib.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import ir.farsirib.Activity.DetailActivity;
import ir.farsirib.Activity.FullScrVideoActivity;
import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Activity.OptionActivity;
import ir.farsirib.Activity.VideoPlayerActivity;
import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class FullScrVideoFragment extends Fragment {

    LinearLayout topview;
    TextView titleBar;
    VideoView videoView;
    MediaController mediaController;

    String title;
    String sum;
    String imgURL;
    String videoURL;
    String callerContext;
    int barname_id;

    @SuppressLint("ValidFragment")
    public FullScrVideoFragment(String title,String sum,String imgURL,String videoUrl,String callerContext,int barname_id) {

        this.title = title;
        this.sum = sum;
        this.imgURL = imgURL;
        this.videoURL = videoUrl;
        this.callerContext = callerContext;
        this.barname_id = barname_id;
    }

    public FullScrVideoFragment(String videoURL,String callerContext) {

        this.videoURL = videoURL;
        this.callerContext = callerContext;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_full_scr_video, container, false);

        topview = rootView.findViewById(R.id.layout_top2);
        titleBar = rootView.findViewById(R.id.titleBar);

        //Setting the titlebar background blank (initial state)
        topview.getBackground().setAlpha(0);
        titleBar.setVisibility(View.INVISIBLE);


        videoView = rootView.findViewById(R.id.fullScr_video_view);
        mediaController = new MediaController(getContext());
        Uri uri = Uri.parse(videoURL);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.start();


        Button btnback = rootView.findViewById(R.id.title_bar_left_menu);
        btnback.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                switch (callerContext)
                {
                    case "DetailActivity":

                        Intent intent = new Intent(getContext(), DetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("title", title);
                        bundle.putString("img",imgURL );
                        bundle.putString("descr", sum);
                        bundle.putString("video_url",videoURL);
                        bundle.putInt("barname_id",barname_id);

                        intent.putExtras(bundle);
                        getActivity().finish();
                        startActivity(intent);
                        break;
                    case "ItemTwoFragment":
                        Intent intent1 = new Intent(getContext(), VideoPlayerActivity.class);
                        intent1.putExtra("Option_Id", VideoPlayerActivity.RADIO_NAMA_ID);
                        intent1.putExtra("Page_Title", "رادیو نما فارس");
                        startActivity(intent1);
                        break;
                    case "ItemThreeFragment":
                        Intent intent2 = new Intent(getContext(), VideoPlayerActivity.class);
                        intent2.putExtra("Option_Id", VideoPlayerActivity.TV_ID);
                        intent2.putExtra("Page_Title", "سیمای فارس");
                        startActivity(intent2);
                        break;
                    case "ProvincialTv":
                        Intent intent3 = new Intent(getContext(), VideoPlayerActivity.class);
                        intent3.putExtra("Option_Id", VideoPlayerActivity.PROVICIALTV_ID);
                        intent3.putExtra("Page_Title", "سیمای فارس");
                        startActivity(intent3);
                        break;
                }
            }
        });
        return rootView;
    }
}
