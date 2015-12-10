package com.tomer.dtfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by tomer aka rosenpin on 12/7/15.
 */
public class Preferences {
    public static int cameraClickDelay = R.integer.defaultTimeout;
    public static boolean enabled = true;
    public static boolean debug = false;

    public static void initialize(Context context) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        enabled = preferences.getBoolean("enabled", true);
        cameraClickDelay = Integer.parseInt(preferences.getString("click_delay", String.valueOf(cameraClickDelay)));
        debug = preferences.getBoolean("debug", false);
    }

}
