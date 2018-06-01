package ir.farsirib.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ir.farsirib.Holder.OptionRecyclerViewHolders;
import ir.farsirib.Model.OptionItemObject;
import ir.farsirib.R;

public class OptionRecyclerViewAdapter extends RecyclerView.Adapter<OptionRecyclerViewHolders> {

    private List<OptionItemObject> itemList;
    private Context context;

    public OptionRecyclerViewAdapter(Context context, List<OptionItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public OptionRecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_card_view_list, null);
        OptionRecyclerViewHolders rcv = new OptionRecyclerViewHolders(layoutView,context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(OptionRecyclerViewHolders holder, int position) {
        holder.optionName.setText(itemList.get(position).getName());

        holder.optionPhoto.setImageResource(itemList.get(position).getPhoto());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
