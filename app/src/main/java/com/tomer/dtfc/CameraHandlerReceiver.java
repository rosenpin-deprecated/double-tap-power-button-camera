package com.tomer.dtfc;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.tomer.dtfc.Utils.Toast;


/**
 * Created by tomer on 11/7/15.
 */
public class CameraHandlerReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;
    PowerManager pm;
    protected PowerManager.WakeLock mWakeLock;
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
            this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Turn Screen On");
        }
        countPowerOff++;
        Toast(context, "Button Pressed");
        Log.v("onReceive", "Power button is pressed." + countPowerOff);
        Log.d("Delay is ", String.valueOf(Preferences.cameraClickDelay));
        Log.d("Current clicks are ", String.valueOf(countPowerOff));
        Log.d("Clicks need to be ", String.valueOf(Preferences.clicks));
        if (countPowerOff == Preferences.clicks) {
            this.mWakeLock.release();
            this.mWakeLock.acquire();
            keyguardLock.reenableKeyguard();
            keyguardLock.disableKeyguard();
            System.out.println("OPENING CAMERA");
            Intent cameraIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

            context.startActivity(cameraIntent);
            countPowerOff = 0;
            handler.removeCallbacks(runnable);
        }
        handler.postDelayed(runnable, (long) (Preferences.cameraClickDelay*1.5));
    }

    private static List<CameraHandlerReceiver> instances = new ArrayList();

    public static CameraHandlerReceiver createMyObject(Context context) {
        CameraHandlerReceiver o = new CameraHandlerReceiver();
        Log.d("Created an object", String.valueOf(o.hashCode()));
        instances.add((o));
        Toast(context, "Started Service");
        return o;
    }

    public static List<CameraHandlerReceiver> getInstances() {
        return instances;
    }

    public CameraHandlerReceiver() {

    }
}
