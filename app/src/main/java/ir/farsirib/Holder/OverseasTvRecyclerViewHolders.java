package ir.farsirib.Holder;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import ir.farsirib.Activity.FullScrVideoActivity;
import ir.farsirib.Model.OverseasTv;
import ir.farsirib.R;
import ir.farsirib.shenavarlib.StandOutWindow;
import ir.farsirib.shenavarview.QueryPreferences;
import ir.farsirib.shenavarview.Video;

/**
 * Created by alireza on 2017/08/13.
 */

public class OverseasTvRecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView livetName;
    public ImageView livePhoto;
    Context myContext;
    OverseasTv overseasTv;
    String txt_name,txt_address;

    public OverseasTvRecyclerViewHolders(View itemView , Context context) {
        super(itemView);

        itemView.setOnClickListener(this);
        livetName = itemView.findViewById(R.id.live_name);
        livePhoto = itemView.findViewById(R.id.live_photo);
        myContext = context;
        overseasTv = new OverseasTv();

    }

    @Override
    public void onClick(View v) {
        int pos = getPosition();

        try {
            ConnectivityManager connManager = (ConnectivityManager) myContext.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeConnection = connManager.getActiveNetworkInfo();
            boolean isOnline = false;
            if ( (activeConnection != null) && activeConnection.isConnected() )
                isOnline = true;

            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            boolean wifi = false;
            if ( mWifi.isConnected() )
                wifi = true;

            overseasTv = OverseasTv.getItem (OverseasTv.OVERSEAS_TVS[pos].getId());

            txt_name = overseasTv.get(OverseasTv.Field.NAME);
            txt_address = overseasTv.get(OverseasTv.Field.ADDRESS);


            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(myContext, livePhoto);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getTitle().toString()) {
                        case "پخش زنده تمام صفحه":
                            Intent intent = new Intent(myContext, FullScrVideoActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("title", txt_name);
                            bundle.putString("img", "");
                            bundle.putString("descr", "");
                            bundle.putString("video_url", txt_address);
                            bundle.putString("caller_context", "NationalTv");

                            intent.putExtras(bundle);
                            myContext.startActivity(intent);
                            break;
                        case "پخش زنده شناور":
//                                                             if(QueryPreferences.getPermissionStatus(myContext)==null){
//                                                                QueryPreferences.setPermissionStatus(myContext, "notGranted");
//                                                                checkPermission();
//                                                            }else{
//                                                                if(QueryPreferences.getPermissionStatus(context).equals("notGranted")){
//                                                                    checkPermission();
//                                                                }
//                                                            }


                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && QueryPreferences.getPermissionStatus(myContext)!=null && QueryPreferences.getPermissionStatus(myContext).equals("OK")){
                                StandOutWindow.videoUrl = txt_address;
                                StandOutWindow.title = txt_name;
                                StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                            }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                                StandOutWindow.videoUrl = txt_address;
                                StandOutWindow.title = txt_name;
                                StandOutWindow.show(myContext, Video.class, StandOutWindow.DEFAULT_ID);
                            }


                            break;
                    }

                    //  Toast.makeText(myContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            popup.show();//showing popup menu

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
