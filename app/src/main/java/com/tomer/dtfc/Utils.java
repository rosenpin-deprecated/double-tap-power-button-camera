package com.tomer.dtfc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by tomer aka rosenpin on 12/10/15.
 */
public class Utils {
    public static void Toast(Context c, String t) {
        if (Preferences.debug)
            Toast.makeText(c, t, Toast.LENGTH_SHORT).show();
    }
}
