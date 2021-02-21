package ir.farsirib.Fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ViewListener;

import java.util.List;

import ir.farsirib.Activity.PrShow;
import ir.farsirib.R;
import ir.farsirib.utils.PrXml;
import ir.farsirib.utils.Rss;


public class FarsIribPRFragment extends Fragment {

    CarouselView customCarouselView;
    TextView customCarouselLabel;
    public List<Rss> data;
    public int[] sampleImages;
    public String[] sampleTitles;
    public String[] sampleNetworkImageURLs;
    View rootView;

    public FarsIribPRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_fars_irib_pr, container, false);

        customCarouselView = rootView.findViewById(R.id.customCarouselView);
        customCarouselLabel = rootView.findViewById(R.id.customCarouselLabel);

        GetRSSDataTask3 task3 = new GetRSSDataTask3();
        task3.execute();

        return rootView;
    }

    public class GetRSSDataTask3 extends AsyncTask<String, Integer, Void> {
        @Override
        protected Void doInBackground(String... params) {

            PrXml rssReader = new PrXml();
            data = rssReader.parser();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Xml", data.toString());

            //   sampleImages = new int[]{R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
            sampleTitles = new String[]{data.get(0).getTitle(), data.get(1).getTitle(), data.get(2).getTitle(), data.get(3).getTitle(), data.get(4).getTitle()};
            sampleNetworkImageURLs = new String[]{
                    data.get(0).getImg(),
                    data.get(1).getImg(),
                    data.get(2).getImg(),
                    data.get(3).getImg(),
                    data.get(4).getImg()
            };

//            customCarouselView = (CarouselView) rootView.findViewById(R.id.customCarouselView);
//            customCarouselLabel = (TextView) rootView.findViewById(R.id.customCarouselLabel);

            customCarouselView.setViewListener(viewListener);

            customCarouselView.setImageClickListener(new ImageClickListener() {
                @Override
                public void onClick(int position) {

                    Intent intent = new Intent(getContext(), PrShow.class);
                    intent.putExtra("title", data.get(position).getTitle());
                    intent.putExtra("link", data.get(position).getLink());
                    startActivity(intent);
                }
            });

            customCarouselView.setPageCount(5);
            customCarouselView.setSlideInterval(4000);
        }


        // To set custom views
        ViewListener viewListener = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {

                View customView = getActivity().getLayoutInflater().inflate(R.layout.view_custom_pr, null);

                TextView labelTextView = customView.findViewById(R.id.labelTextView);
                ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);

                Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"IRANSansMobile.ttf");
                labelTextView.setTypeface(tf);
                Picasso.with(getContext()).load(sampleNetworkImageURLs[position]).into(fruitImageView);
                labelTextView.setText(sampleTitles[position]);

                return customView;
            }
        };

        View.OnClickListener pauseOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //customCarouselView.reSetSlideInterval(0);
            }
        };
    }
}
