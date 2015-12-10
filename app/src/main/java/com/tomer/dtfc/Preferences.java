package com.tomer.dtfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by tomer aka rosenpin on 12/7/15.
 */
public class Preferences {
    public static int cameraClickDelay = R.integer.defaultTimeout;
    public static boolean enabled = true;
    public static boolean debug = false;
    public static int clicks = 2;

    public static void initialize(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        enabled = preferences.getBoolean("enabled", true);
        cameraClickDelay = Integer.parseInt(preferences.getString("click_delay", "2000"));
        debug = preferences.getBoolean("debug", false);
        clicks = preferences.getBoolean("triple", false) ? 3 : 2;
        Log.d("Clicks is ", String.valueOf(clicks));
    }

}
