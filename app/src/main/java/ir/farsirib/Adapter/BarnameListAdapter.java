package ir.farsirib.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import java.util.List;

import ir.farsirib.Activity.DetailActivity;
import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Model.barname;
import ir.farsirib.R;


/**
 * Created by 910170 on 04/01/2016.
 */
public class BarnameListAdapter extends ArrayAdapter<barname> {


    private static String title="";
    private static String description="";
    Context mContext;
    String URL = "";

        public BarnameListAdapter(Context context, int resource, List<barname> items) {
            super(context, resource,items);

            this.mContext=context;
        }

        public static  View getView(final barname item, final Context context)
        {
            // 1. Create inflater
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.fragment_list_item, null, false);


            // 3. Get the two text view from the rowView
            TextView txt_id= rowView.findViewById(R.id.section_id_txt);
            TextView txt_title= rowView.findViewById(R.id.item_title);
            ImageView img_section= rowView.findViewById(R.id.item_image);
            TextView txt_img_url= rowView.findViewById(R.id.image_url_txt);
            TextView txt_video_url= rowView.findViewById(R.id.video_url_txt);
            TextView txt_desc= rowView.findViewById(R.id.item_description);

            // 4. Set the text for textView
            txt_id.setText(item.getCategory_id()+"");
            txt_title.setText(item.getTitle());
            txt_img_url.setText(item.getImage_url());
            txt_video_url.setText(item.getVideo_url());
            txt_desc.setText(item.getDescription());

            String URL = item.getImage_url();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(URL, opts);
            opts.inJustDecodeBounds = false;

            Picasso.with(context)
                    .load(item.getImage_url())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .resize(360,192)
                    .centerCrop()
                    .into((img_section));

            img_section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("title", item.getTitle());
                    bundle.putString("img",item.getImage_url() );
                    bundle.putString("descr", item.getDescription());
                    bundle.putString("video_url",item.getVideo_url());
                    bundle.putInt("barname_id",item.getCategory_id());

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            // 5. retrn rowView
            return rowView;
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Viewholder viewholder=null;

            barname rowItem = getItem(position);

            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if(convertView==null)
            {
                convertView=inflater.inflate(R.layout.fragment_list_item,null);
                viewholder=new Viewholder();
                viewholder.txt_id= convertView.findViewById(R.id.section_id_txt);
                viewholder.txt_title= convertView.findViewById(R.id.item_title);
                viewholder.img_section= convertView.findViewById(R.id.item_image);
                viewholder.txt_img_url= convertView.findViewById(R.id.image_url_txt);
                viewholder.txt_video_url= convertView.findViewById(R.id.video_url_txt);
                viewholder.txt_desc= convertView.findViewById(R.id.item_description);
                viewholder.relative= convertView.findViewById(R.id.relativelayout);
                convertView.setTag(viewholder);

            }
            else
            {
                viewholder=(Viewholder)convertView.getTag();
            }

            viewholder.txt_id.setText(rowItem.getCategory_id()+"");
            viewholder.txt_title.setText(rowItem.getTitle());
            viewholder.txt_img_url.setText(rowItem.getImage_url());
            viewholder.txt_video_url.setText(rowItem.getVideo_url());
            viewholder.txt_desc.setText(rowItem.getDescription());


            URL = rowItem.getImage_url();
                    //rowItem.getImage_url();
           // URL = "http://farsirib.ir/images/product/thumb/1087.jpg";

//            ImageLoaderConfig();
//
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                    .showImageOnFail(R.mipmap.ic_launcher)
//                    .showStubImage(R.mipmap.ic_launcher)
//                    .showImageForEmptyUri(R.mipmap.ic_launcher).cacheInMemory()
//                    .cacheOnDisc().build();
//
//        //download and display image from url
 //           imageLoader.displayImage(URL , viewholder.img_section, options);


        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(URL, opts);
        opts.inJustDecodeBounds = false;

        Picasso.with(mContext)
                .load(rowItem.getImage_url())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .resize(360,192)
                .centerCrop()
                .into((viewholder.img_section));




//            int type=rowItem.getType();
//            int rowId = rowItem.getId();
//
//            if (rowId % 2 == 0)
//            {
//                viewholder.relative.setBackgroundColor(Color.parseColor("#FFB74D"));//#C5CAE9
//            }
//            else
//            {
//                viewholder.relative.setBackgroundColor(Color.parseColor("#FFCC80"));//#9FA8DA
//            }


            return convertView;
        }

    private void ImageLoaderConfig() {
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }


        private class Viewholder{

            TextView txt_title,txt_video_url,txt_desc,txt_id,txt_img_url;
            RelativeLayout relative;
            ImageView img_section;

        }
}
