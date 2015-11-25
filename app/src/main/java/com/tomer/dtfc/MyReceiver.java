package com.tomer.dtfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tomer on 11/7/15.
 */
public class MyReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            countPowerOff = 0;
            System.out.println("RESETTING");
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"STARTED SERVICE",Toast.LENGTH_LONG).show();
        Log.v("onReceive", "Power button is pressed."+countPowerOff);
        countPowerOff++;
        handler.postDelayed(runnable, context.getResources().getInteger(R.integer.timeout));
        if (countPowerOff == 2) {
            System.out.println("OPENING CAMERA");
            context.startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA));
            countPowerOff = 0;
            handler.removeCallbacks(runnable);
        }
    }

}
