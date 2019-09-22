package com.thumbnaildownloader;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class InfoAct extends AppCompatActivity {

    private DrawerLayout d;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thumbnaildownloader.R.layout.activity_info);

        d = findViewById(com.thumbnaildownloader.R.id.draw);
        b = findViewById(com.thumbnaildownloader.R.id.hit);

        NavigationView n = findViewById(com.thumbnaildownloader.R.id.navs);
        n.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case com.thumbnaildownloader.R.id.home:
                       /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                new AboutFragment()).commit();*/
                        Intent in = new Intent(InfoAct.this,MainActivity.class);
                        startActivity(in);

                        break;

                }
                d.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    public void gmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:harishthedev@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Thumbnail Downloader[FeedBack]");
        startActivity(Intent.createChooser(intent, "Send feedback"));
    }

    public void insta(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://instagram.com/_u/" + "_harish_vs_"));
            intent.setPackage("com.instagram.android");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/" + "_harish_vs_")));
        }
    }

    public void hj(View view){
        d.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if(d.isDrawerOpen(GravityCompat.START)){
            d.closeDrawer(GravityCompat.START);
        }
    }
}
