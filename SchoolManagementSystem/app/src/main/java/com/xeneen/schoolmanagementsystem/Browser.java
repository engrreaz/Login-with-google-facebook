package com.xeneen.schoolmanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Browser extends AppCompatActivity {

    WebView brow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView brow = (WebView) findViewById(R.id.safari);


        brow.getSettings().setJavaScriptEnabled(true);
        brow.getSettings().setLoadsImagesAutomatically(true);
        brow.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        brow.getSettings().setUseWideViewPort(true);
        brow.getSettings().setLoadWithOverviewMode(true);
        brow.setWebViewClient(new WebViewClient());

        if (isConnected()==true){
            String url = "https://www.school.xeneen.com";
            brow.loadUrl(url);
            Toast.makeText(getApplicationContext(),"CONNECTED",Toast.LENGTH_LONG).show();

        } else {
            //Toast.makeText(getApplicationContext(),"NAI ......",Toast.LENGTH_SHORT).show();
            brow.loadData(
                    "<html><body>Hello, world<br>!</body></html>",
                    "text/html",
                    "UTF-8");
        }


    }


    public boolean isConnected () {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"BACKKKKK",Toast.LENGTH_LONG).show();
        if(brow.canGoBack())
            brow.goBack();// if there is previous page open it
        else
            super.onBackPressed();//if there is no previous page, close app
    }
}