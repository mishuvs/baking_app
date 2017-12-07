package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import vaibhav.mishu.com.bakingapp.databinding.ActivityStepBinding;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepActivity extends AppCompatActivity {

    JsonUtil.Recipe recipe;
    int index;

    ActivityStepBinding mBinding;

    SimpleExoPlayer player;

    private boolean playWhenReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_step);

        Intent i = getIntent();
        recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");
        index = i.getIntExtra("index",0);

        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                index++;
                setFields();
            }
        });
        mBinding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                index--;
                setFields();
            }
        });

        setFields();

    }

    private void checkButtons(){
        if(index==0) mBinding.previous.setEnabled(false);
        else mBinding.previous.setEnabled(true);
        if(index==recipe.steps.size()-1) mBinding.next.setEnabled(false);
        else mBinding.next.setEnabled(true);
    }

    private void setFields() {

        checkButtons();

        if(recipe.steps.get(index).videoURL.length()!=0) {
            mBinding.playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }
        else mBinding.playerView.setVisibility(View.GONE);
        mBinding.stepDescription.setText(recipe.steps.get(index).description);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mBinding.playerView.setPlayer(player);

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
