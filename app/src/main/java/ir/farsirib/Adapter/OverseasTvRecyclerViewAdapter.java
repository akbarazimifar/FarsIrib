package ir.farsirib.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.List;

import ir.farsirib.Holder.OverseasTvRecyclerViewHolders;
import ir.farsirib.Model.LiveItemObject;
import ir.farsirib.R;

/**
 * Created by alireza on 2017/08/13.
 */

public class OverseasTvRecyclerViewAdapter extends RecyclerView.Adapter<OverseasTvRecyclerViewHolders> {

    private List<LiveItemObject> itemList;
    private Context context;
    String URL = "";

    public OverseasTvRecyclerViewAdapter(List<LiveItemObject> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public OverseasTvRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_card_view, null);
        OverseasTvRecyclerViewHolders rcv = new OverseasTvRecyclerViewHolders(layoutView,context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(OverseasTvRecyclerViewHolders holder, int position) {

        holder.livetName.setText(itemList.get(position).getName());
        URL = itemList.get(position).getPhoto();

        ImageLoaderConfig();

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .showImageOnFail(R.mipmap.disconect)
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().build();
        imageLoader.displayImage(URL, holder.livePhoto, options);
    }

    private void ImageLoaderConfig() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
