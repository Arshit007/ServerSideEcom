package com.example.arshit.serversideecom;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private FirebaseAuth auth;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//

        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(SplashScreen.this,SignIn.class));
            finish();
        }

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, SignIn.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
