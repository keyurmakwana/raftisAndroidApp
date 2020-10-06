package com.keyur.raftis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class NoNetwork extends AppCompatActivity {
    LottieAnimationView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);

        button = findViewById(R.id.check_again);
        final String activityName = getIntent().getStringExtra("activity");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amIConnected()) {
                    switch (activityName) {
                        case "login":
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                            break;
                        case "signup":
                            startActivity(new Intent(getApplicationContext(), SignUp.class));
                            finish();
                            break;
                        case "forgot":
                            startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                            finish();
                            break;

                    }
                } else {
                }
            }
        });
    }

    public boolean amIConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}