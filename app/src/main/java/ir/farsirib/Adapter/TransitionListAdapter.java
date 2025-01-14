package ir.farsirib.Adapter;

import java.util.ArrayList;

import ir.farsirib.Model.ListItem;
import ir.farsirib.utils.UICircularImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ir.farsirib.R;

public class TransitionListAdapter extends BaseAdapter {

	   ViewHolder viewHolder;

        private ArrayList<ListItem> mItems = new ArrayList<ListItem>();
        private Context mContext;

        public TransitionListAdapter(Context context, ArrayList<ListItem> list) {
            mContext = context;
            mItems = list;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if(convertView==null){

                // inflate the layout
            	LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.fragment_list_item_transition, null);

                // well set up the ViewHolder
                viewHolder = new ViewHolder();
                viewHolder.title = v.findViewById(R.id.item_title);
                viewHolder.descr = v.findViewById(R.id.item_description);
                viewHolder.image = v.findViewById(R.id.item_image);

                // store the holder with the view.
                v.setTag(viewHolder);

            }else{
                // just use the viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final String item = mItems.get(position).getTitle();
            final String desc = mItems.get(position).getDesc();
            final int imageid = mItems.get(position).getImageId();

            viewHolder.image.setImageResource(imageid);
            viewHolder.title.setText(item);
            viewHolder.descr.setText(desc);
            TextView hiddenView = v.findViewById(R.id.hidden_view);
            hiddenView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Toast.makeText(mContext, item + " hidden view clicked",
                      //      Toast.LENGTH_SHORT).show();

                }
            });
            viewHolder.image.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                  //  Toast.makeText(mContext, item + " icon clicked",
                        //    Toast.LENGTH_SHORT).show();

                }
            });

            return v;
        }

        static class ViewHolder {
        	  TextView title;
        	  TextView descr;
        	  UICircularImage image;
        	  int position;
        }

    }