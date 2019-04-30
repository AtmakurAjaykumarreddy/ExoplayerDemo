package com.example.hp.exoplayerdemo;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class MainActivity extends AppCompatActivity {
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String URL="http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";
    private boolean playWhenReady;
    private long current_position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        exoPlayerView=findViewById(R.id.simple_my_exoplayer);
        BandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
        TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
        exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);

        Uri uri=Uri.parse(URL);
        DefaultHttpDataSourceFactory dfhsf=new DefaultHttpDataSourceFactory("exoplayer_video");
        ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
        MediaSource mediaSource=new ExtractorMediaSource(uri,dfhsf,extractorsFactory,null,null);
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        exoPlayer.prepare(mediaSource);

        if(savedInstanceState!=null){
            current_position=savedInstanceState.getLong("current_position");
            exoPlayer.seekTo(current_position);
            playWhenReady=savedInstanceState.getBoolean("play_back");
            exoPlayer.setPlayWhenReady(playWhenReady);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
            layoutParams.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            layoutParams.height=Resources.getSystem().getDisplayMetrics().heightPixels;
            exoPlayerView.setLayoutParams(layoutParams);
        }else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) exoPlayerView.getLayoutParams();
            layoutParams.width=Resources.getSystem().getDisplayMetrics().widthPixels;
            layoutParams.height=600;
            exoPlayerView.setLayoutParams(layoutParams);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(exoPlayer!=null){
            outState.putLong("current_position",exoPlayer.getCurrentPosition());
            outState.putBoolean("play_back",exoPlayer.getPlayWhenReady());
        }
    }
}
