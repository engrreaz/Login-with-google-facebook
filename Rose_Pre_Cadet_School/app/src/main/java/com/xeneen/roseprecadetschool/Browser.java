package com.xeneen.roseprecadetschool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

public class Browser extends AppCompatActivity {
    WebView brow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView brow = findViewById(R.id.safari);

        brow.getSettings().setJavaScriptEnabled(true);
        brow.getSettings().setLoadsImagesAutomatically(true);
        brow.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        brow.getSettings().setUseWideViewPort(true);
        brow.getSettings().setLoadWithOverviewMode(true);
        brow.setWebViewClient(new WebViewClient());

        if (isConnected()){
            String url = "https://www.rose.xeneen.com";
            brow.loadUrl(url);
            Toast.makeText(getApplicationContext(),"Internet Connection Established",Toast.LENGTH_SHORT).show();

        } else {
            //Toast.makeText(getApplicationContext(),"NAI ......",Toast.LENGTH_SHORT).show();
            brow.loadData(
                    "<html><body><center><br><br><br>No Internet Connection Detected!</center></body></html>",
                    "text/html",
                    "UTF-8");
        }

        brow.setWebChromeClient(new WebChromeClient(){
            //@Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    String str = brow.getUrl();
                    Toast.makeText(getApplicationContext(),"Changed : "+str+" progress : " + newProgress,Toast.LENGTH_SHORT).show();
                }
            }
        });
//onProgressChangedonPageFinished

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
        Toast.makeText(getApplicationContext(),"back button pressed",Toast.LENGTH_LONG).show();
        boolean mag = brow.canGoBack();
        if(mag){
            Toast.makeText(getApplicationContext(),"..................",Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        //return super.getOnBackInvokedDispatcher();
        Toast.makeText(getApplicationContext(),"back***** button pressed",Toast.LENGTH_LONG).show();
        boolean mag = brow.canGoBack();
        if(mag){
            Toast.makeText(getApplicationContext(),"*********",Toast.LENGTH_LONG).show();
        }
        return null;
    }



}