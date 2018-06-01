package ir.farsirib.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import ir.farsirib.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RadioFreqFragment extends Fragment {


    CarouselView customCarouselView;
    ImageView fruitImageView;

    Button pauseButton;

    String[] sampleNetworkImageURLs = {
            "http://mob.shahreraz.com/Farsirib/img/frequency/radio/11.jpg",
            "http://mob.shahreraz.com/Farsirib/img/frequency/radio/12.jpg",
            "http://mob.shahreraz.com/Farsirib/img/frequency/radio/13.jpg",
            "http://mob.shahreraz.com/Farsirib/img/frequency/radio/14.jpg",
    };


    public RadioFreqFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_radio_freq, container, false);

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

            Picasso.with(getContext())
                    .load(sampleNetworkImageURLs[position])
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .fit().centerCrop()
                    .into(fruitImageView);

            return customView;
        }
    };

}
