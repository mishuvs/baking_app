package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepFragment extends Fragment {

    JsonUtil.Recipe recipe;
    int index;
    
    SimpleExoPlayer player;
    @BindView(R.id.next) Button next;
    @BindView(R.id.previous) Button previous;
    @BindView(R.id.player_view) SimpleExoPlayerView playerView;
    @BindView(R.id.step_description) TextView stepDescription;
    @BindView(R.id.step_thumbnail) ImageView stepThumbnail;
    
    private boolean playWhenReady = true;

    private long currentPlayerPosition = 0;

    Bundle playerStateBundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    public interface StepInterface{
        void onStepSelected(int position);
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

        if (savedInstanceState != null) {
            playerStateBundle = savedInstanceState.getBundle("STATE_BUNDLE");
            if(playerStateBundle!=null) {
                currentPlayerPosition = playerStateBundle.getLong("PLAYER_POSITION");
                playWhenReady = playerStateBundle.getBoolean("PLAYBACK_STATE");
                playerStateBundle = null;
            }
                recipe = (JsonUtil.Recipe) savedInstanceState.getSerializable("RECIPE_STEPS");
                index = savedInstanceState.getInt("RECIPE_INDEX");
                //set playerStateBundle as null to make currentPlayerPosition 0
        }
        else{
            Intent i = getActivity().getIntent();
            if(i!=null){
                recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");
                index = i.getIntExtra("index",0);
            }
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

    private void setFields() {

        checkButtons();

        if(recipe.steps.get(index).videoURL.length()!=0) {
            playerView.setVisibility(View.VISIBLE);
            stepThumbnail.setVisibility(View.GONE);
            initializePlayer();
        }
        else if(recipe.steps.get(index).thumbnailURL!=null && !recipe.steps.get(index).thumbnailURL.isEmpty()){
            playerView.setVisibility(View.GONE);
            //to avoid NUllPointerEx:
            View view = getView();
            if(getView()!=null){
                Picasso.with(getView().getContext()).load(Uri.parse(recipe.steps.get(index).thumbnailURL)).into(stepThumbnail);
            }
        }
        else {
            playerView.setVisibility(View.GONE);
            stepThumbnail.setVisibility(View.GONE);
        }

        stepDescription.setText(recipe.steps.get(index).description);
    }

    private void checkButtons(){
        if(index==0) previous.setEnabled(false);
        else previous.setEnabled(true);
        if(index==recipe.steps.size()-1) next.setEnabled(false);
        else next.setEnabled(true);
    }

    private void initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);

        Uri uri = Uri.parse(recipe.steps.get(index).videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        // Prepare the player with the source.

        player.prepare(mediaSource);
        if (currentPlayerPosition != 0) {
            player.seekTo(currentPlayerPosition);
            currentPlayerPosition = 0;
        }
    }

    private void releasePlayer() {
        if (player != null) {
            //save fragment state
            playerStateBundle = new Bundle();
            playerStateBundle.putLong("PLAYER_POSITION", player.getCurrentPosition());
            playerStateBundle.putBoolean("PLAYBACK_STATE",player.getPlayWhenReady());
            //release player
            player.release();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle("STATE_BUNDLE",playerStateBundle);
        outState.putInt("RECIPE_INDEX", index);
        outState.putSerializable("RECIPE_STEPS", recipe);
        super.onSaveInstanceState(outState);
    }
}
