package vaibhav.mishu.com.bakingapp;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import vaibhav.mishu.com.bakingapp.databinding.FragmentStepBinding;
import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepFragment extends Fragment {

    JsonUtil.Recipe recipe;
    int index;

    FragmentStepBinding mBinding;

    SimpleExoPlayer player;

    private boolean playWhenReady = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step, container, false);
        return mBinding.getRoot();
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
        mBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player!=null) player.stop();
                index++;
                setFields();
            }
        });
        mBinding.previous.setOnClickListener(new View.OnClickListener() {
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
                new DefaultRenderersFactory(getActivity()),
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
