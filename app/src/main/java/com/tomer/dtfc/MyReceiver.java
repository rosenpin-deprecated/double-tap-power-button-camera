package com.tomer.dtfc;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by tomer on 11/7/15.
 */
public class MyReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;
    PowerManager pm;
    KeyguardManager keyguardManager;
    KeyguardManager.KeyguardLock keyguardLock;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            countPowerOff = 0;
            System.out.println("RESETTING");
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        if (pm == null) {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Camera Open");
            wl.acquire();
            keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            keyguardLock = keyguardManager.newKeyguardLock("TAG");
        }
        Toast.makeText(context, "STARTED SERVICE", Toast.LENGTH_LONG).show();
        Log.v("onReceive", "Power button is pressed." + countPowerOff);
        countPowerOff++;
        handler.postDelayed(runnable, context.getResources().getInteger(R.integer.timeout));
        if (countPowerOff == 2) {
            keyguardLock.reenableKeyguard();
            keyguardLock.disableKeyguard();

            System.out.println("OPENING CAMERA");
            Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

            context.startActivity(cameraIntent);
            countPowerOff = 0;
            handler.removeCallbacks(runnable);
        }
    }

}
