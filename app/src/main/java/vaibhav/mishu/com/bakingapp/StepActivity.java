package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepActivity extends AppCompatActivity {

    JsonUtil.Recipe recipe;
    int index;

    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;

    private boolean playWhenReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent i = getIntent();
        recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");
        index = i.getIntExtra("index",0);

        TextView stepText = findViewById(R.id.step_description);
        stepText.setText(recipe.steps.get(index).description);

        playerView = (SimpleExoPlayerView) findViewById(R.id.step_video);

        if(recipe.steps.get(index).videoURL.length()!=0) {
            initializePlayer();
        }
        else playerView.setVisibility(View.GONE);

    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);

        Uri uri = Uri.parse(recipe.steps.get(index).videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, true);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}
