package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import butterknife.BindView;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepFragment extends Fragment {

    JsonUtil.Recipe recipe;
    int index;
    
    SimpleExoPlayer player;
    @BindView(R.id.next) Button next;
    @BindView(R.id.previous) Button previous;
    @BindView(R.id.player_view) SimpleExoPlayerView playerView;
    @BindView(R.id.step_description) TextView stepDescription;
    
    private boolean playWhenReady = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }

    public interface StepInterface{
        public void onStepSelected(int position);
    }

    public void updateContent(Bundle recipeInfo){
        releasePlayer();
        recipe = (JsonUtil.Recipe) recipeInfo.getSerializable("recipe");
        index = recipeInfo.getInt("index",0);
        initializeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent i = getActivity().getIntent();
        if(i!=null){
            recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");
            index = i.getIntExtra("index",0);
        }

        initializeFragment();

    }

    private void initializeFragment() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player!=null) player.stop();
                index++;
                setFields();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player!=null) player.stop();
                index--;
                setFields();
            }
        });

        setFields();
    }

    private void checkButtons(){
        if(index==0) previous.setEnabled(false);
        else previous.setEnabled(true);
        if(index==recipe.steps.size()-1) next.setEnabled(false);
        else next.setEnabled(true);
    }

    private void setFields() {

        checkButtons();

        if(recipe.steps.get(index).videoURL.length()!=0) {
            playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }
        else playerView.setVisibility(View.GONE);
        stepDescription.setText(recipe.steps.get(index).description);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
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
