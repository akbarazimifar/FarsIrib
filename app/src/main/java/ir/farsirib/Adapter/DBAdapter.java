package ir.farsirib.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ir.farsirib.Database.DatabaseAssets;
import ir.farsirib.Model.barname;

/**
 * Created by farzad on 4/8/2015.
 */
public class DBAdapter extends DatabaseAssets {

    barname my_barname;
//    String DB_PATH = null;

    public DBAdapter(Context c)
    {
        super(c);
        DB_PATH = "/data/data/" + c.getPackageName() + "/" + "databases/";
        my_barname = new barname();
    }

    public boolean isDataExists(String id,String title)
    {
        String selectquery="SELECT * FROM "+TABLE_BARNAME +" WHERE category_id = ? AND title = ?";
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectquery, new String[]{id,title});
        int count = cursor.getCount();

        cursor.close();

        return count > 0;


    }

    public boolean isBarnameEmpty()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectquery="SELECT * FROM "+TABLE_BARNAME+"";
        Cursor cursor = db.rawQuery(selectquery, null);

        int cnt = cursor.getCount();
        cursor.close();

        return cnt == 0;
    }

    public ArrayList<barname> getBarname(String ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<barname> lst=new ArrayList<barname>();
        String selectquery="SELECT * FROM "+TABLE_BARNAME+" WHERE "+KEY_CATEGORY_ID+"="+ID+"";

        Cursor cursor2=db.rawQuery(selectquery, null);

        int cnt = cursor2.getCount();
        Log.d("tedad", "getHealthCenters: count is:"+cnt);

        for(;cursor2.moveToNext();)
        {

            //read all data
            int category_id=cursor2.getInt(1);
            String image_url=cursor2.getString(2);
            String title=cursor2.getString(3);
            String video_url = cursor2.getString(4);
            String description=cursor2.getString(5);

            my_barname = new barname(category_id , title , image_url, video_url, description);

            lst.add(my_barname);
        }
        cursor2.close();
        return  lst;
    }

    public ArrayList<barname> getBarname()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<barname> lst=new ArrayList<barname>();
        String selectquery="SELECT * FROM "+TABLE_BARNAME+"";

        Cursor cursor2=db.rawQuery(selectquery, null);

        int cnt = cursor2.getCount();
        Log.d("tedad", "getHealthCenters: count is:"+cnt);

        for(;cursor2.moveToNext();)
        {

            //read all data
            int category_id=cursor2.getInt(1);
            String image_url=cursor2.getString(2);
            String title=cursor2.getString(3);
            String video_url = cursor2.getString(4);
            String description=cursor2.getString(5);

            my_barname = new barname(category_id , title , image_url, video_url, description);

            lst.add(my_barname);
        }
        cursor2.close();
        return  lst;
    }

    public void insertBarname(barname my_barname)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_CATEGORY_ID ,my_barname.getCategory_id());
        values.put(KEY_IMAGE_URL, my_barname.getImage_url());
        values.put(KEY_TITLE, my_barname.getTitle());
        values.put(KEY_VIDEO_URL, my_barname.getVideo_url());
        values.put(KEY_DESCRIPTION, my_barname.getDescription());

        db.insert(TABLE_BARNAME, null, values);
    }

    public void update()
    {
        SQLiteDatabase database=this.getReadableDatabase();
        database = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);

        database.execSQL("DELETE FROM " + TABLE_BARNAME);

//        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BARNAME);
//
//        database.execSQL("CREATE  TABLE  IF NOT EXISTS barname (" +
//                "category_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , " +
//                "image_url TEXT , " +
//                "title TEXT , " +
//                "video_url TEXT , " +
//                "description TEXT " +
//                ")");
    }
}
