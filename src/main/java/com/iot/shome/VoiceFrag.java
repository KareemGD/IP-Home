package com.iot.shome;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.iot.shome.tts.TTS;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Kareem Diab on 11/17/2015.
 */
public class VoiceFrag extends Fragment {

    private final String IP_ADDRESS = "192.168.43.163";
    private TextView txtSpeechInput;
    private TTS tts;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_voice, container, false);

        txtSpeechInput = (TextView) rootView.findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);

        // hide the action bar
        //getActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        //TEXT TO SPEECH
        tts = new TTS(getContext());

        return rootView;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    if (result.get(0).contains("turn") && result.get(0).contains("on") && result.get(0).contains("light")) {
                        //TTS
                        tts.Say("Okay, turning on the light", true);


                        //WEB REQUEST
                    new WebRequest().execute("http://" + IP_ADDRESS + "/shome/voice_commands/light.php?light=ON");

                    } else if (result.get(0).contains("turn") && result.get(0).contains("off") && result.get(0).contains("light")) {
                        //TTS
                        tts.Say("Okay, turning off the light", true);



                        //WEB REQUEST
                        new WebRequest().execute("http://"+IP_ADDRESS+"/shome/voice_commands/light.php?light=OFF");
                    } else {
                        tts.Say("Sorry, this command isn't available", true);

                    }
                }
                break;
            }

        }
    }



}
