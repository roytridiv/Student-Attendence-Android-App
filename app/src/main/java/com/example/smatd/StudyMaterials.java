package com.example.smatd;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class StudyMaterials extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener , YouTubePlayer.PlaybackEventListener , YouTubePlayer.PlayerStateChangeListener {

    YouTubePlayerView youTubePlayerView  ;
  //  Button but;

    YouTubePlayer.OnInitializedListener onInitializedListener ;
    String api = "AIzaSyCVsJRCq8MJxd6k_I19xzna7rJrEJoC6Qk";

    String vid =  "Yp6UQXu4rPI";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_materials);

      //  but = findViewById(R.id.playButton);
        youTubePlayerView = findViewById(R.id.youtubePlay);


        youTubePlayerView.initialize(api,StudyMaterials.this);


        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


                youTubePlayer.loadPlaylist(vid);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

//        but.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                youTubePlayerView.initialize(YoutubeConfig.getApiKey() , onInitializedListener);
//
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudyMaterials.this, UserDashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {


        youTubePlayer.setPlayerStateChangeListener(StudyMaterials.this);
        youTubePlayer.setPlaybackEventListener(StudyMaterials.this);

        if(!b){
            youTubePlayer.cueVideo(vid);
        }



    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {



    }
}
