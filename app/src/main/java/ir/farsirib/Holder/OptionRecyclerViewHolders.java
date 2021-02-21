package ir.farsirib.Holder;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.farsirib.Activity.MainActivity;
import ir.farsirib.R;

public class OptionRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView optionName;
    public ImageView optionPhoto;
    Context myContext;

    public OptionRecyclerViewHolders(View itemView, Context context) {
        super(itemView);
        itemView.setOnClickListener(this);
        optionName = itemView.findViewById(R.id.option_name);
        optionPhoto = itemView.findViewById(R.id.option_photo);
        this.myContext = context;

    }

    @SuppressWarnings("WrongConstant")
    @Override
    public void onClick(View view) {


        int pos = getPosition();

        try {

                Intent intent = new Intent(myContext, MainActivity.class);
                intent.putExtra("Option_Id", pos);
                intent.putExtra("Page_Title", optionName.toString());
                view.getContext().startActivity(intent);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
  }

}