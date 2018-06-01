package ir.farsirib.shenavarview;

import android.content.Context;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String IS_PERMISSION_GRANTED = "isPermissionGranted";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";

    public static String getPermissionStatus(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(IS_PERMISSION_GRANTED, null);
    }
    public static void setPermissionStatus(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(IS_PERMISSION_GRANTED, query)
                .apply();
    }
}