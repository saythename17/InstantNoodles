package com.icandothisallday.instantnoodles;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class CompleteActivity extends AppCompatActivity {
    ImageView yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        yes=findViewById(R.id.yes);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Animation animation= AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.only_alpha);
        yes.setVisibility(View.VISIBLE);
        yes.setAnimation(animation);
    }
}
