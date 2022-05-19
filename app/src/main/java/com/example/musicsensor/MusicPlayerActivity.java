package com.example.musicsensor;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {
    TextView titleTextView,currentTimeTextView,totalTimeTextView;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,prevBtn,musicIcon;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        titleTextView= findViewById(R.id.song_title);
        currentTimeTextView= findViewById(R.id.currentTime);
        totalTimeTextView= findViewById(R.id.totalTime);
        seekBar=findViewById(R.id.seek_bar);
        pausePlay=findViewById(R.id.pause_play);
        nextBtn=findViewById(R.id.next);
        prevBtn=findViewById(R.id.previous);
        musicIcon=findViewById(R.id.music_icon_big);

        titleTextView.setSelected(true);

        songsList= (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();
        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTextView.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));
                    if (mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    void setResourcesWithMusic(){
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTextView.setText(currentSong.getTitle());
        totalTimeTextView.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextsong());
        prevBtn.setOnClickListener(v-> playPrevSong());
        playMusic();
    }
    private void playMusic(){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void playNextsong(){
        if(MyMediaPlayer.currentIndex==songsList.size()-1){
            return;
        }
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void playPrevSong(){
        if(MyMediaPlayer.currentIndex==0){
            return;
        }
        MyMediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }
    private void pausePlay(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }
    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis)%TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(millis)%TimeUnit.MINUTES.toSeconds(1));
    }
}