package com.thumbnaildownloader;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;


import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView tv,vidtit,chaid;
    private EditText ed;
    private Button sea , hit;
    private FButton down;
    private ImageView im;
    private CircleImageView chim;
    private String ans ,ans1, channelId;
    private String url,head = "",channelURL;
    private String[] arr,arr1;
    private CardView cd,search_card_view;
    Request req,req1;
    OkHttpClient client,c2;
    private DrawerLayout d;
    final String API_KEY = "AIzaSyApfu8BvWVs1J_IGm3Sm4ib63T056UdHJo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.thumbnaildownloader.R.layout.activity_main);

        /*f (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }*/

        android.support.v7.widget.Toolbar t = findViewById(com.thumbnaildownloader.R.id.toolbar);
        t.setNavigationIcon(com.thumbnaildownloader.R.drawable.menu);
        setSupportActionBar(t);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.openDrawer(Gravity.LEFT);
            }
        });


        ed = findViewById(com.thumbnaildownloader.R.id.url);
        hit = findViewById(com.thumbnaildownloader.R.id.hit);
        im = findViewById(com.thumbnaildownloader.R.id.harish);
        chim = findViewById(com.thumbnaildownloader.R.id.chim);
        chaid = findViewById(com.thumbnaildownloader.R.id.chaid);
        down = (FButton) findViewById(com.thumbnaildownloader.R.id.download);
        down.setEnabled(false);
        d = findViewById(com.thumbnaildownloader.R.id.draw);
        search_card_view = findViewById(com.thumbnaildownloader.R.id.card_view);
        vidtit = findViewById(com.thumbnaildownloader.R.id.vidTit);
        cd = findViewById(com.thumbnaildownloader.R.id.cd);


        NavigationView n = findViewById(com.thumbnaildownloader.R.id.navs);
        n.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case com.thumbnaildownloader.R.id.info:
                       /* getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                                new AboutFragment()).commit();*/
                        Intent in = new Intent(MainActivity.this,InfoAct.class);
                        startActivity(in);

                        break;

                }
                d.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        if (CheckConnection()) {
            Log.i("Net", "It has net");
        } else {
            Toast.makeText(getApplicationContext(), "No Internt Connection", Toast.LENGTH_LONG).show();
        }

        client = new OkHttpClient();
        c2 = new OkHttpClient();

        isStoragePermissionGranted();

        hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuu = ed.getText().toString();
                if (CheckConnection()) {
                    if (uuu.contains("https://") || uuu.contains("http://") || uuu.contains(".com")) {
                        Log.i("Net", "It has Network");

                        Toast.makeText(MainActivity.this,"Processing...",Toast.LENGTH_SHORT).show();
                        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=1&q=" + uuu + "&key=" + API_KEY;
                        req = new Request.Builder()
                                .url(url)
                                .build();





                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                isStoragePermissionGranted();
                            }
                        }
                        makeHTTPrequest();
                        down.setEnabled(true);
                        AnimationOb();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter a Valid URL", Toast.LENGTH_LONG).show();

                    }
                } else
                    Toast.makeText(getApplicationContext(), "No Internt Connection", Toast.LENGTH_LONG).show();

            }

        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        isStoragePermissionGranted();
                    } else {
                        if (CheckConnection()) {
                            Toast.makeText(MainActivity.this,"Downloading..",Toast.LENGTH_LONG).show();
                            Log.i("Net", "It has net");
                            String s = "https://i.ytimg.com/vi/" + arr[arr.length - 1] + "/maxresdefault.jpg";
                            Log.i("sing", s + " \n\n" + arr[arr.length - 1]);
                            download(s, arr[1]);

                            for (int i = 0; i < arr.length; i++) {
                                Log.i("arr", arr[i] + " ");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Internt Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


    }

    private String JsonParsing(String norURL) {
        String url = "";
        String tit = "";
        String id = "";
        try {
            JSONObject root = new JSONObject(norURL);
            JSONArray arr = root.getJSONArray("items");
            JSONObject first = arr.getJSONObject(0);
            JSONObject idid = first.getJSONObject("id");
            id = idid.getString("videoId");
            JSONObject snippet = first.getJSONObject("snippet");
            channelId = snippet.getString("channelId");
            Log.i("Harishda",channelId);
            tit = snippet.getString("title");
            head = tit;
            Log.i("HEAD",head);
            Log.i("id", id);
            JSONObject thumb = snippet.getJSONObject("thumbnails");
            JSONObject high = thumb.getJSONObject("high");
            url = high.getString("url");
            return url + " " + tit + " " + id;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url + " " + tit + " " + id;
    }

    private String JsonParsing12(String norURL) {
        String url = "";
        String tit = "";
        try {
            JSONObject root = new JSONObject(norURL);
            JSONArray arr = root.getJSONArray("items");
            JSONObject first = arr.getJSONObject(0);
            JSONObject snippet = first.getJSONObject("snippet");
            tit = snippet.getString("title");
            Log.i("TIT",tit);
            JSONObject thumb = snippet.getJSONObject("thumbnails");
            JSONObject high = thumb.getJSONObject("default");
            url = high.getString("url");
            return url + " " + tit;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url + " " + tit;
    }

    private void download(String url, String fName) {
        String DIR_NAME = "Thumbnail Downloader";
        String filename = fName + ".jpg";
        String downloadUrlOfImage = url;
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + DIR_NAME + "/");


        if (!direct.exists()) {
            direct.mkdir();
            Log.d("BOI", "dir created for first time");
        }

        DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + DIR_NAME + File.separator + filename);

        dm.enqueue(request);
    }

    private void makeHTTPrequest() {
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                tv.setText("Data Cannot be Parsed!");
                Log.i("Hairsh", "ERRORRRRE");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tv.setText("ERROOORRRR");
                        Toast.makeText(MainActivity.this, "No Response from the Server", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ans = JsonParsing(res);
                            arr = ans.split(" ");
                            Log.i("Bigils",head);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    vidtit.setText(head);
                                }
                            });
                            Picasso.get().load(arr[0]).into(im);
                            channelURL = "https://www.googleapis.com/youtube/v3/channels?part=snippet&id="+channelId+"&key="+API_KEY;
                            req1 = new Request.Builder()
                                    .url(channelURL)
                                    .build();
                            makeReqHTTP();


//                            tv.setText(ans);
                        }
                    });
                }
            }
        });
    }

    private void makeReqHTTP(){

        c2.newCall(req1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                tv.setText("Data Cannot be Parsed!");
                Log.i("Hairsh", "ERRORRRRE");
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tv.setText("ERROOORRRR");
                        Toast.makeText(MainActivity.this, "No Response from the Server", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.i("mass" ,res);
                            ans1 = JsonParsing12(res);
                            arr1 = ans1.split(" ");
//                            Log.i("Bigils",head);
                            /*MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                            public void run() {
                                vidtit.setText(head);
                            }
                        });*/
                            Picasso.get().load(arr1[0]).into(chim);
                            StringBuffer sb = new StringBuffer();
                            sb.append(arr1[1]);
                            for(int i=2; i<arr1.length; i++){
                                sb.append(" ");
                                sb.append(arr1[i]);
                            }
                            chaid.setText(sb.toString());
                            cd.setVisibility(View.VISIBLE);

//                            tv.setText(ans);
                        }
                    });
                }
            }
        });
    }

    public boolean isStoragePermissionGranted() {
        String TAG = "asd";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    private boolean CheckConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public void hj(View view){
        d.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if(d.isDrawerOpen(GravityCompat.START)){
            d.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    public void AnimationOb(){
        ObjectAnimator obY = ObjectAnimator.ofFloat(search_card_view,"y",220f);
//        ObjectAnimator obY1 = ObjectAnimator.ofFloat(down,"y",10f);
        obY.setDuration(1000);
//        obY1.setDuration(1000);
        AnimatorSet as = new AnimatorSet();
//        AnimatorSet sa = new AnimatorSet();
        as.playTogether(obY);
//        sa.playTogether(obY1);
        as.start();
//        sa.start();

        down.animate().translationY(790f).setDuration(1000).start();
    }

    /*public String title(String in){
        tit = in;
        return tit;
    }*/
}
