package com.iot.shome.mqtt;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Kareem Diab on 3/29/2016.
 */
public class ServiceHandler extends Service {

    private MqttService mqtt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mqtt = new MqttService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Intent mqttIntent = new Intent(ctx, org.eclipse.paho.android.service.MqttService.class);
        //ctx.startService(mqttIntent);

        if(isServiceRunning(org.eclipse.paho.android.service.MqttService.class)) {
            Log.d("ServiceHandler", "MqttService Already Running.");
            mqtt.connect(this);
        } else {
            mqtt.connect(this);
        }
        return START_STICKY;
    }

    public void selfStop () {
        this.selfStop();
    }


    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
