package ir.farsirib.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import ir.farsirib.Activity.DetailActivity;
import ir.farsirib.Model.barname;
import ir.farsirib.R;

/**
 * Created by alireza on 20/02/2017.
 */
public class SpecialProgramRecyclerViewAdapter extends RecyclerView.Adapter<SpecialProgramRecyclerViewAdapter.SpecialProgramRecyclerViewHolders> {


    public SpecialProgramRecyclerViewAdapter(Context context , List<barname> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    private List<barname> itemList = Collections.emptyList();
    private Context context;
    String URL = "";

    public class SpecialProgramRecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView programName;
        public ImageView programPhoto;

        public SpecialProgramRecyclerViewHolders(View itemView) {
            super(itemView);

            programName = itemView.findViewById(R.id.option_name);
            programPhoto = itemView.findViewById(R.id.option_photo);
        }
    }

    @Override
    public SpecialProgramRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_card_view_list, null);
        return new SpecialProgramRecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final SpecialProgramRecyclerViewHolders holder, final int position) {

        holder.programName.setText(itemList.get(position).getTitle());

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(itemList.get(position).getImage_url(), opts);
        opts.inJustDecodeBounds = false;
        Picasso.with(context)
                .load(itemList.get(position).getImage_url())
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200,200)
                .centerCrop()
                .into((holder.programPhoto));

        holder.programPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View viewa) {

                Intent intent = new Intent(context, DetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", itemList.get(position).getTitle());
                bundle.putString("img",itemList.get(position).getImage_url() );
                bundle.putString("descr", itemList.get(position).getDescription());
                bundle.putString("video_url",itemList.get(position).getVideo_url());
                bundle.putInt("barname_id",itemList.get(position).getCategory_id());

                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });

        holder.programName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View viewa) {

                Intent intent = new Intent(context, DetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("title", itemList.get(position).getTitle());
                bundle.putString("img",itemList.get(position).getImage_url() );
                bundle.putString("descr", itemList.get(position).getDescription());
                bundle.putString("video_url",itemList.get(position).getVideo_url());
                bundle.putInt("barname_id",itemList.get(position).getCategory_id());

                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount(){return this.itemList.size();}
}
