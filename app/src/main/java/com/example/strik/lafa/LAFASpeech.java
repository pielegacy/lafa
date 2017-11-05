package com.example.strik.lafa;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

/**
 * Provides basic TTS Functionality for LAFA
 *
 * Based on
 * https://code.tutsplus.com/tutorials/use-text-to-speech-on-android-to-read-out-incoming-messages--cms-22524
 * @author Alex Billson
 */
public class LAFASpeech implements TextToSpeech.OnInitListener {

    private Boolean ready = false;
    private TextToSpeech tts;

    public LAFASpeech(Context context)
    {
        tts = new TextToSpeech(context, this);
    }
    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            tts.setLanguage(Locale.US);
            ready = true;
        }else{
            ready = false;
        }
    }

    /**
     * Convert text to speech.
     *
     * @param text the text to speak.
     */
    @SuppressWarnings("deprecation")
    public void speak(String text)
    {
        if(ready) {
            HashMap<String, String> hash = new HashMap<>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, hash);
        }
    }
}
