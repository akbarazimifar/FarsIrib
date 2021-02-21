package ir.farsirib.Holder;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import ir.farsirib.R;
import ir.farsirib.utils.UICircularImage;

/**
 * Created by alireza on 20/02/2017.
 */
//public class ProgramRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//    public TextView programName;
//    public UICircularImage programPhoto;
//    Context myContext;
//
//    public ProgramRecyclerViewHolders(View itemView, Context context) {
//        super(itemView);
//        myContext = context;
//        programName = (TextView)itemView.findViewById(R.id.txt_title);
//        programPhoto = (UICircularImage)itemView.findViewById(R.id.ivItemGridImage);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        Toast.makeText(myContext, "Clicked Share", Toast.LENGTH_SHORT).show();
//
//    }
//}

public class ProgramRecyclerViewHolders extends RecyclerView.ViewHolder {

    public TextView programName;
    public UICircularImage programPhoto;

    public ProgramRecyclerViewHolders(View itemView) {
        super(itemView);

        programName = itemView.findViewById(R.id.txt_title);
        programPhoto = itemView.findViewById(R.id.imageView11);
    }


}