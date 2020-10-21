package com.icandothisallday.instantnoodles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LaunchOrderActivity extends AppCompatActivity {
    ImageView order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_order);

        order=findViewById(R.id.order);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Animation animation= AnimationUtils.loadAnimation(LaunchOrderActivity.this,
                R.anim.alpha);
        order.setVisibility(View.VISIBLE);
        order.setAnimation(animation);
    }

    public void goINActivity(View view) {
        Intent intent = new Intent(LaunchOrderActivity.this,InstantNoodlesActivity.class);
        startActivity(intent);
    }
}
