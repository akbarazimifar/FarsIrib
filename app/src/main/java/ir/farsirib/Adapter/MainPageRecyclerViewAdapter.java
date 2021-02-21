package ir.farsirib.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.Model.program;
import ir.farsirib.R;

/**
 * Created by alireza on 20/02/2017.
 */
public class MainPageRecyclerViewAdapter extends RecyclerView.Adapter<MainPageRecyclerViewAdapter.MainPageRecyclerViewHolders> {


    public MainPageRecyclerViewAdapter(Context context , List<program> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    private List<program> itemList = Collections.emptyList();
    private Context context;
    String URL = "";

    public class MainPageRecyclerViewHolders extends RecyclerView.ViewHolder {

        public TextView programName;
        public ImageView programPhoto;

        public MainPageRecyclerViewHolders(View itemView) {
            super(itemView);

            programName = itemView.findViewById(R.id.txt_title);
            programPhoto = itemView.findViewById(R.id.imageView11);
        }
    }

    @Override
    public MainPageRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.demo_images, null);
        return new MainPageRecyclerViewHolders(layoutView);
    }

    @Override
    public void onBindViewHolder(final MainPageRecyclerViewHolders holder, final int position) {

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

        holder.programPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View viewa) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("Option_Id", position);
                intent.putExtra("Page_Title", holder.programName.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount(){return this.itemList.size();}
}
