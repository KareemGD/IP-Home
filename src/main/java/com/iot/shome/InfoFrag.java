package com.iot.shome;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.iot.shome.mqtt.ServiceHandlerController;
import com.iot.shome.tts.TTS;
import com.iot.shome.mqtt.MqttService;


/**
 * Created by Kareem Diab on 11/17/2015.
 */
public class InfoFrag extends Fragment {

    private TTS tts;
    private View rootView;
    public MqttService mqtt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ServiceHandlerController.setContext(getActivity().getApplicationContext());
        tts = new TTS(getContext());
        mqtt = new MqttService();
        rootView = inflater.inflate(R.layout.fragment_info, container, false);

        final CheckBox push_notify = ((CheckBox) rootView.findViewById(R.id.push_notify));

        push_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(((CheckBox) v).isChecked()) {
                    ServiceHandlerController.startSvcHandler();

                } else {
                    ServiceHandlerController.stopSvcHandler();

                }
            }
        });


        Button b = (Button) rootView.findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                tts.Say("Hello, Swipe right for voice commands section... or... swipe left for manual control", false);
            }
        });


        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();

        boolean checked;
        if(isServiceRunning(org.eclipse.paho.android.service.MqttService.class)){
            checked = true;
        } else {
            checked = false;
        }

       ((CheckBox) rootView.findViewById(R.id.push_notify)).setChecked(checked);
    }



    @Override
    public void onStart() {
        super.onStart();
        boolean checked;
        if(isServiceRunning(org.eclipse.paho.android.service.MqttService.class)) {
            checked = true;
            //mqtt.connect();
        } else
            checked = false;

        ((CheckBox) rootView.findViewById(R.id.push_notify)).setChecked(checked);

    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
