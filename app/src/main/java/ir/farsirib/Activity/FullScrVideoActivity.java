package ir.farsirib.Activity;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ir.farsirib.Fragment.FullScrVideoFragment;
import ir.farsirib.R;

public class FullScrVideoActivity extends AppCompatActivity {

    String title;
    String sum;
    String imgURL;
    String videoURL;
    String callerContext;
    private int barname_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_scr_video);

        Bundle bundle = getIntent().getExtras();
        title    =    bundle.getString("title");
        sum      =      bundle.getString("descr");
        imgURL   =   bundle.getString("img");
        videoURL = bundle.getString("video_url");
        callerContext = bundle.getString("caller_context");
        barname_id = bundle.getInt("barname_id");


        switch (callerContext)
        {
            case "DetailActivity":
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new FullScrVideoFragment(title,sum,imgURL,videoURL,callerContext,barname_id));
                transaction.commit();
                break;
            case "ItemTwoFragment":
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.container, new FullScrVideoFragment(videoURL,callerContext));
                transaction1.commit();
                break;
            case "ItemThreeFragment":
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.container, new FullScrVideoFragment(videoURL,callerContext));
                transaction2.commit();
                break;
            case "ProvincialTv":
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.container, new FullScrVideoFragment(videoURL,callerContext));
                transaction3.commit();
                break;
            case "NationalTv":
                FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                transaction4.replace(R.id.container, new FullScrVideoFragment(videoURL,callerContext));
                transaction4.commit();
                break;
            case "OverseasTv":
                FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
                transaction5.replace(R.id.container, new FullScrVideoFragment(videoURL,callerContext));
                transaction5.commit();
                break;
        }

    }
}
