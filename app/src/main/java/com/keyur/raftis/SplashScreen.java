package com.keyur.raftis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        Animation name_nimation= AnimationUtils.loadAnimation(this,R.anim.name_animation);
        Animation logo_animation= AnimationUtils.loadAnimation(this,R.anim.logo_animation);
        logo_animation.reset();
        name_nimation.reset();
        TextView tv=(TextView)findViewById(R.id.splash_text1);
        ImageView im=(ImageView)findViewById(R.id.splash_logo);
        tv.clearAnimation();
        tv.setAnimation(name_nimation);
        im.clearAnimation();
        im.setAnimation(logo_animation);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Calling New Activity after SPLASH_SCREEN seconds 1s = 1000
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),Login.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }, 2500);


    }

}