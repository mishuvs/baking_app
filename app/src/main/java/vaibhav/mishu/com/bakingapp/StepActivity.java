package vaibhav.mishu.com.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import vaibhav.mishu.com.bakingapp.util.JsonUtil;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent i = getIntent();
        JsonUtil.Recipe recipe = (JsonUtil.Recipe) i.getSerializableExtra("recipe");
        int index = i.getIntExtra("index",0);

        VideoView stepVideo = findViewById(R.id.step_video);
        TextView stepText = findViewById(R.id.step_description);
        stepText.setText(recipe.steps.get(index).description);
    }
}
