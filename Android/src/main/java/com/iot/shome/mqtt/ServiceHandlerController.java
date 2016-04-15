package com.iot.shome.mqtt;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import org.eclipse.paho.android.service.*;

/**
 * Created by Kareem Diab on 3/29/2016.
 */
public class ServiceHandlerController {
    static Context context;
    public static void setContext (Context ctx) {
        context = ctx;
    }


    public static void startSvcHandler () {
        Intent svcHandler = new Intent(context, ServiceHandler.class);
        context.startService(svcHandler);
    }

    public static void stopSvcHandler () {
        Intent svcHandler = new Intent(context.getApplicationContext(), ServiceHandler.class);
        Intent MqttService = new Intent(context.getApplicationContext(), org.eclipse.paho.android.service.MqttService.class);

        context.stopService(svcHandler);
        context.stopService(MqttService);
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
