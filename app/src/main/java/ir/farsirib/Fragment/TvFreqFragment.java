package ir.farsirib.Fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.synnapps.carouselview.ViewListener;

import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFreqFragment extends Fragment {

    CarouselView customCarouselView;
    ImageView fruitImageView;

    Button pauseButton;

    String[] sampleTitles = {"مهارلو", "میانرود", "سعدی", "گلستان", "زیباشهر" ,"دروازه قرآن", "صدرا", "کوشک هزار", "", "" };
    String[] sampleNetworkImageURLs = {
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/01.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/02.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/03.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/04.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/05.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/06.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/07.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/08.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/09.jpg",
            "http://www.shahreraz.com/Farsirib/img/frequency/tv/10.jpg",
    };


    public TvFreqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tv_freq, container, false);

        fruitImageView = rootView.findViewById(R.id.fruitImageView);
        customCarouselView = rootView.findViewById(R.id.customCarouselView);
        customCarouselView.setPageCount(sampleNetworkImageURLs.length);
        customCarouselView.setSlideInterval(4000);

        customCarouselView.setViewListener(viewListener);



        return rootView;
    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int position) {

            View customView = getActivity().getLayoutInflater().inflate(R.layout.view_custom_carousel, null);

            ImageView fruitImageView = customView.findViewById(R.id.fruitImageView);

            //fruitImageView.setImageResource(sampleImages[position]);
            Picasso.with(getContext())
                    .load(sampleNetworkImageURLs[position])
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .into(fruitImageView);

            //labelTextView.setText(sampleTitles[position]);

            return customView;
        }
    };

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {


            //fruitImageView.setImageResource(sampleNetworkImageURLs[position]);
//            labelTextView.setText(sampleTitles[position]);




            Picasso.with(getContext())
                    .load(sampleNetworkImageURLs[position])
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .into(imageView);




            //imageView.setImageResource(sampleImages[position]);
        }
    };


}
