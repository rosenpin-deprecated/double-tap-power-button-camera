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
import static com.tomer.dtfc.Utils.Toast;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    CameraHandlerReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        Preferences.initialize(getApplicationContext());
        killBrodcastReciever();
        if (Preferences.enabled)
            startBrodcastReciever();
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, root, false);
        root.addView(bar, 0);
        bar.setTitle(R.string.app_name);
        bar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }

    public void startBrodcastReciever() {
        killBrodcastReciever();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver =  CameraHandlerReceiver.createMyObject(getApplicationContext());
        try {
            registerReceiver(mReceiver, filter);
        }catch (Exception e){
            System.out.println("Error, retrying..");
            startBrodcastReciever();
        }
        Toast(getApplicationContext(), "Started, please try to double tap your power button");
    }

    public void killBrodcastReciever() {
        try {
            ComponentName receiver = new ComponentName(getApplicationContext(), CameraHandlerReceiver.class);

            PackageManager pm = getApplicationContext().getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
            unregisterReceiver(mReceiver);
            for(int i = 0;i<CameraHandlerReceiver.getInstances().size();i++){
                Log.d("Number of instances ", String.valueOf(i));
                unregisterReceiver(CameraHandlerReceiver.getInstances().get(i));
            }
        } catch (Exception e) {
            Log.d("Service is already dead", e.getMessage());
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Preference changed", key);
        Preferences.initialize(getApplicationContext());
        if (Preferences.enabled) {
            killBrodcastReciever();
            startBrodcastReciever();
        } else
            killBrodcastReciever();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"You closed the app, it won't work now",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(),"You closed the app, it won't work now",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(getApplicationContext(),"You closed the app, it won't work now",Toast.LENGTH_LONG).show();
    }
}
