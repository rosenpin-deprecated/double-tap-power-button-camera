package com.tomer.dtfc;


import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    CameraHandlerReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        Preferences.initialize(getApplicationContext());
        if (Preferences.enabled)
            startBrodcastReciever();
        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(bar, 0);
        bar.setTitle(R.string.app_name);
        bar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }

    public void startBrodcastReciever() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new CameraHandlerReceiver();
        registerReceiver(mReceiver, filter);
        Toast.makeText(getApplicationContext(), "Started, please try to double tap your power button", Toast.LENGTH_LONG).show();

    }

    public void killBrodcastReciever() {
        ComponentName receiver = new ComponentName(getApplicationContext(), CameraHandlerReceiver.class);

        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Preference changed", key);
        Preferences.initialize(getApplicationContext());
        killBrodcastReciever();
        if (sharedPreferences.getBoolean("enabled", false))
            startBrodcastReciever();
    }
}
