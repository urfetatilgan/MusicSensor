package com.example.musicsensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.TextView;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private AudioManager audioManager;
    private Context con;
    @Override
    public void onReceive(Context context, Intent intent) {
        con = context.getApplicationContext();
        audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
        if ("atilgan.urfet.SENSOR_ACTION".equals(intent.getAction())) {
            String receivedText = intent.getStringExtra("status");
            //Toast.makeText(context, receivedText, Toast.LENGTH_SHORT).show();
            if(receivedText.equals("sesKis")){
                audioManager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_PLAY_SOUND);
            //    Toast.makeText(context, "Masada", Toast.LENGTH_LONG).show();

            }else{
                audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE,AudioManager.FLAG_PLAY_SOUND);
            //    Toast.makeText(context, "Masada değil", Toast.LENGTH_LONG).show();
            }
        }
    }
}