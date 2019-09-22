package com.thumbnaildownloader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashAct extends AppCompatActivity {

    private ImageView im;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thumbnaildownloader.R.layout.activity_splash);


        im = findViewById(com.thumbnaildownloader.R.id.imageView2);
        Animation myanim = AnimationUtils.loadAnimation(this, com.thumbnaildownloader.R.anim.mytransit);
        im.startAnimation(myanim);



        final Intent i = new Intent(this,MainActivity.class);
        Thread timer = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);

                    finish();
                }
            }
        };
        timer.start();
    }
}
