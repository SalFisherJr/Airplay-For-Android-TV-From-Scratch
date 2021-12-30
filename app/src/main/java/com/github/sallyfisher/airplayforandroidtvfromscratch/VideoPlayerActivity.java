package com.github.sallyfisher.airplayforandroidtvfromscratch;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        playVideo(getIntent().getStringExtra(MainActivity.VIDEO_URL_KEY));
    }

    private void playVideo(String videoUrl){
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        PlayerView playerView = (PlayerView) findViewById(R.id.player_view);
        playerView.setPlayer(player);
        MediaItem video = MediaItem.fromUri(videoUrl);
        player.setMediaItem(video);
        player.prepare();
        player.setPlayWhenReady(true);
    }

}