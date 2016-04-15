package com.iot.shome.tts;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Kareem Diab on 11/28/2015.
 */
public class TTS {
    private Context context;
    private TextToSpeech tts;

    public TTS(Context context){
        this.context = context;

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });
    }
    public void Say(String toSpeak, boolean toast) {
        if(toast)
            Toast.makeText(context, toSpeak, Toast.LENGTH_SHORT).show();
        if (Build.VERSION.RELEASE.startsWith("5")) {
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
}
