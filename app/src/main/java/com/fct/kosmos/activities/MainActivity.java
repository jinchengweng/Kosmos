package com.fct.kosmos.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fct.kosmos.R;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private Intent WelcomeActivity;

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WelcomeActivity = new Intent(this, WelcomeActivity.class);

        ImageView imagen = (ImageView)findViewById(R.id.imagen_logo_inicio);
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout_splash_inicio);

        //implements and starts animation
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.splash_scale);
        anim2.setStartOffset(1500);
        layout.startAnimation(anim1);
        imagen.startAnimation(anim2);
        openApp(true);

    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(WelcomeActivity);
                finish();
            }
        }, 3000);
    }

}
