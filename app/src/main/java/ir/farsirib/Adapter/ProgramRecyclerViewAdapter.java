package ir.farsirib.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ir.farsirib.Activity.TransitionDetailActivity;
import ir.farsirib.Holder.ProgramRecyclerViewHolders;
import ir.farsirib.Model.ListItem;
import ir.farsirib.Model.program;
import ir.farsirib.R;
import ir.farsirib.Setting.Default_Station;
import ir.farsirib.utils.UICircularImage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by alireza on 20/02/2017.
 */
public class ProgramRecyclerViewAdapter extends RecyclerView.Adapter<ProgramRecyclerViewAdapter.ProgramRecyclerViewHolders> {

    public static final String MYPrefrences="Prefrence_Station";
    public SharedPreferences sharedPreferences;
    public  static final String KEY_ID="id";
    public  static final String key_top ="";
    public  static final String key_left = "";
    public  static final String key_height = "";
    public  static final String key_width ="";
    public  static final String key_title = "title";
    public  static final String key_description = "description";
    public  static final String key_imageId = "";
    public  static final String key_barnameId = "";

    //Vars
    private String PACKAGE = "IDENTIFY";
    private int barname_id = 0;
    Default_Station default_station;
    private String title="";
    private String description="";

    public ProgramRecyclerViewAdapter(Context context , List<program> itemList) {
        this.itemList = itemList;
        this.context = context;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //sharedPreferences =context.getSharedPreferences(MYPrefrences,context.MODE_PRIVATE);
    }

    private List<program> itemList = Collections.emptyList();
    private Context context;
    String URL = "";

    public class ProgramRecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView programName;
        //public UICircularImage programPhoto;
        public ImageView programPhoto;

        public ProgramRecyclerViewHolders(View itemView) {
            super(itemView);

            programName = itemView.findViewById(R.id.txt_title);
            //programPhoto = (UICircularImage)itemView.findViewById(R.id.ivItemGridImage);
            programPhoto = itemView.findViewById(R.id.imageView11);
        }


    }

    @Override
    public ProgramRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_images, null);
        return new ProgramRecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final ProgramRecyclerViewHolders holder, final int position) {

        holder.programName.setText(itemList.get(position).getName());

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(itemList.get(position).getPhoto(), opts);
        opts.inJustDecodeBounds = false;
        Picasso.with(context)
                .load(itemList.get(position).getPhoto())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200,200)
                .centerCrop()
                .into((holder.programPhoto));

//        holder.programPhoto.setImageResource(itemList.get(position).getImageId());

        holder.programPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View viewa) {
//                Toast.makeText(viewa.getContext(), "man injaaaaam, "+position , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context , TransitionDetailActivity.class);

                switch (position)
                {
                    case 0:
                        barname_id = 3 ; //kashaneh
                        break;
                    case 1:
                        barname_id = 4 ;//khosha
                        break;
                    case 2:
                        barname_id = 72 ;//gompegola
                        break;
                    case 3:
                        barname_id = 11 ;//shabeparsi
                        break;
                    case 4:
                        barname_id = 15 ;//sobheDekgosha
                        break;
                    case 5:
                        barname_id = 10 ;//goftegoo
                        break;
                    case 6:
                        barname_id = 12 ;//shahreraz
                        break;
                    case 7:
                        barname_id = 13 ;//moshavereShoma
                        break;
                    case 8:
                        barname_id = 30 ;//hamvelayati
                        break;
                    case 9:
                        barname_id = 67 ;//shamdooni
                        break;
                }


                Bundle bundle = new Bundle();
                bundle.putString("title", itemList.get(position).getName());
                bundle.putInt("img",itemList.get(position).getImageId());
                bundle.putString("descr", itemList.get(position).getDesc());
                bundle.putInt("barname_id", barname_id);

                int[] screen_location = new int[2];
                //View view = viewa.findViewById(R.id.ivItemGridImage);
                View view = viewa.findViewById(R.id.imageView11);
                view.getLocationOnScreen(screen_location);

                bundle.putInt(PACKAGE + ".left", screen_location[0]);
                bundle.putInt(PACKAGE + ".top", screen_location[1]);
                bundle.putInt(PACKAGE + ".width", view.getWidth());
                bundle.putInt(PACKAGE + ".height", view.getHeight());
                bundle.putString("myPrefrences",MYPrefrences);

                intent.putExtras(bundle);

 //               default_station = new Default_Station(context);
//                default_station.saveValue(screen_location[1]
//                        ,screen_location[0]
//                        ,view.getHeight()
//                        ,view.getWidth()
//                        ,itemList.get(position).getName()
//                        ,itemList.get(position).getDesc()
//                        ,itemList.get(position).getImageId()
//                        ,barname_id);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(key_top,screen_location[1]);
                editor.putInt(key_left,screen_location[0]);
                editor.putInt(key_height,view.getHeight());
                editor.putInt(key_width,view.getWidth());
                editor.putString(key_title,itemList.get(position).getName());
                editor.putString(key_description,itemList.get(position).getDesc());
                editor.putInt(key_imageId,itemList.get(position).getImageId());
                editor.putInt(key_barnameId,barname_id);
                editor.commit();


//                default_station.setTop(screen_location[1]);
//                default_station.setLeft(screen_location[0]);
//                default_station.setHeight(view.getHeight());
//                default_station.setWidth(view.getWidth());
//
//                default_station.setTitle(itemList.get(position).getName());
//                default_station.setDescription(itemList.get(position).getDesc());
//                default_station.setBarnameId(barname_id);
//                default_station.setImageId(itemList.get(position).getImageId());

                context.startActivity(intent);
               // context.overridePendingTransition(0, 0);
              //  ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount(){return this.itemList.size();}
}
