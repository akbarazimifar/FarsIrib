package ir.farsirib.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import ir.farsirib.Adapter.Adapter;
import ir.farsirib.R;
import ir.farsirib.utils.Rss;
import ir.farsirib.utils.Xml;

public class LastNewsFragment extends Fragment {

    public RecyclerView re;
    public Adapter adapterMain;
    public List<Rss> data;
    private ProgressBar prgbar;

    public LastNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GetRSSDataTask task = new GetRSSDataTask();
        task.execute();

        View view = inflater.inflate(R.layout.fragment_last_news, container, false);
        re = view.findViewById(R.id.main_re);
        prgbar= view.findViewById(R.id.prg_bar);
        re.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    public class GetRSSDataTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... urls) {

            try {
                Xml rssReader = new Xml();
                data = rssReader.parser();

            } catch (Exception e) {
                Log.e("RssReader", "no internet");
            }

            return null;
        }


        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            adapterMain = new Adapter(getActivity(), data);
            re.setAdapter(adapterMain);
            prgbar.setVisibility(View.GONE);
        }
    }

}
