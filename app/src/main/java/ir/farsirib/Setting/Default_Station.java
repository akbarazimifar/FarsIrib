package ir.farsirib.Setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by farzad on 4/11/2015.
 */
public class Default_Station {
    Context m_context;

    public int getTop() {
        return this.sharedPreferences.getInt(key_top,0);
    }

    public int getLeft() {
        return this.sharedPreferences.getInt(key_left,0);
    }

    public int getHeight() {
        return this.sharedPreferences.getInt(key_height,0);
    }

    public int getWidth() {
        return this.sharedPreferences.getInt(key_width,0);
    }

    public String getTitle() {
        return this.sharedPreferences.getString(key_title,"");
    }

    public String getDescription() {
        return this.sharedPreferences.getString(key_description,"");
    }

    public int getImageId() {
        return this.sharedPreferences.getInt(key_imageId,0);
    }

    public int getBarnameId() {
        return this.sharedPreferences.getInt(key_barnameId,0);
    }

    public  static final String KEY_ID="id";
    public  static final String key_top ="";
    public  static final String key_left = "";
    public  static final String key_height = "";
    public  static final String key_width ="";
    public  static final String key_title = "title";
    public  static final String key_description = "description";
    public  static final String key_imageId = "";
    public  static final String key_barnameId = "";



    public static final String MYPrefrences="Prefrence_Station";

    public SharedPreferences sharedPreferences;

    SharedPreferences.Editor edit;

    public Default_Station(Context context)
    {

        m_context=context;
        sharedPreferences=m_context.getSharedPreferences(MYPrefrences,Context.MODE_PRIVATE);
        edit =this.sharedPreferences.edit();
    }

   public void setTop(int top)
   {
       //SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putInt(String.valueOf(key_top),top);
       edit.commit();
   }

   public void setLeft(int left)
   {
       SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putInt(String.valueOf(key_left),left);
       edit.commit();
   }

   public void setHeight(int height)
   {
      // SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putInt(String.valueOf(key_height),height);
       edit.commit();
   }

   public void setWidth(int width)
   {
       //SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putInt(String.valueOf(key_width),width);
       edit.commit();
   }

   public void setTitle(String title)
   {
       //SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putString(key_title,title);
       edit.commit();
   }

   public void setDescription(String description)
   {
       //SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putString(key_description,description);
       edit.commit();
   }

   public void setImageId(int imageId)
   {
     //  SharedPreferences.Editor edit =this.sharedPreferences.edit();

       edit.putInt(String.valueOf(key_imageId),imageId);
       edit.commit();
   }

    public void setBarnameId(int barnameId)
    {
       // SharedPreferences.Editor edit =this.sharedPreferences.edit();

        edit.putInt(String.valueOf(key_barnameId),barnameId);
        edit.commit();
    }


    public void saveValue(int top,int left,int height,int width,String title,String description,int imageId,int barnameId)
    {
        SharedPreferences.Editor edit =this.sharedPreferences.edit();

        edit.putInt(String.valueOf(key_top),top);
        edit.putInt(String.valueOf(key_left),left);
        edit.putInt(String.valueOf(key_height),height);
        edit.putInt(String.valueOf(key_width),width);
        edit.putString(key_title,title);
        edit.putString(key_description,description);
        edit.putInt(String.valueOf(key_barnameId),barnameId);
        edit.putInt(String.valueOf(key_imageId),imageId);

        edit.commit();

    }

    public String getId()
    {
        return this.sharedPreferences.getString(KEY_ID,"");
    }
}
