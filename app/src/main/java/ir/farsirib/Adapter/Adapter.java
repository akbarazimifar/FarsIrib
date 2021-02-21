package ir.farsirib.Adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import ir.farsirib.Activity.Show;
import ir.farsirib.R;
import ir.farsirib.utils.Rss;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater inflater;

    public Adapter(Context context, List<Rss> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    List<Rss> data = Collections.emptyList();
    Context context;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String img_defult="http://www.iribnews.ir/client/themes/fa/main/img/logo.png";
        Rss cur = data.get(position);
        holder.title.setText(cur.getTitle());
        Picasso.with(context).load(cur.getImg()).into(holder.img);
        holder.date.setText(cur.getDate());

     }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView date;
        ImageView img;
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.imageView);
            title= itemView.findViewById(R.id.titles);
            date= itemView.findViewById(R.id.date);
            content= itemView.findViewById(R.id.content2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context,Show.class);
            intent.putExtra("title",data.get(getPosition()).getTitle());
            intent.putExtra("body",data.get(getPosition()).getContent());
            intent.putExtra("link_page",data.get(getPosition()).getLink());
            context.startActivity(intent);
        }
    }
}
