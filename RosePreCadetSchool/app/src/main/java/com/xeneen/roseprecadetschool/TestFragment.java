package com.xeneen.roseprecadetschool;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private int mParam2 = 0;

    WebView wb;
    ProgressBar pb;
    TextView pr;
    int netstatus;
    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("message");
            mParam2 = getArguments().getInt("stid");
            netstatus = getArguments().getInt("netstatus");
            //Toast.makeText(this, "Bangladesh", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View kkk =  inflater.inflate(R.layout.fragment_test, container, false);
        pb = kkk.findViewById(R.id.pb);
        pr = kkk.findViewById(R.id.valg);
        wb = kkk.findViewById(R.id.wb);
        wb.setBackgroundColor(0xFFFFC0CB);
        WebSettings ws = wb.getSettings();

        /*SharedPreferences sh = (SharedPreferences) this.getActivity().getSharedPreferences("ROSEPCS",MODE_PRIVATE);
        int netstatus = sh.getInt("netstatus", 0);
        */
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadsImagesAutomatically(true);
        wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.setWebViewClient(new WebViewClient());

        if(netstatus==1){
            if(mParam1==""){
                wb.loadUrl("https://rose.xeneen.com");
            } else {
                wb.loadUrl(mParam1);
            }
        } else {
            wb.loadUrl("file:///android_asset/test.html");

            /*wb.loadData(
                    "<html><body><center><br><br><br>No Internet Connection Detected!"+netstatus+"</center></body></html>",
                    "text/html",
                    "UTF-8");*/
        }

        wb.setWebChromeClient(new WebChromeClient(){
            //@Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress<100){
                    pb.setVisibility(View.VISIBLE);
                    pr.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                    pr.setText(newProgress + "%");




                    //Toast.makeText(getActivity(),"Changed : "+str+" progress : " + newProgress,Toast.LENGTH_SHORT).show();
                }else {
                    String str = wb.getUrl();
                    String pack = str.substring(24, 31) ;
                    String fack = "storage";

                    if(pack.equals(fack)){
                        //Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                        Uri uri = Uri.parse(str);
                        String user = uri.getQueryParameter("u");
                        String fname = uri.getQueryParameter("n");
                        String level = uri.getQueryParameter("l");
                        String pth = uri.getQueryParameter("p");

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ROSEPCS", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("username", user);
                        myEdit.putString("fullname", fname);
                        myEdit.putString("level", level);
                        myEdit.putString("photo", pth);
                        myEdit.putString("login", "yes");
                        myEdit.apply();
                        wb.loadUrl("https://rose.xeneen.com/index.php");
                    }
                    pb.setVisibility(View.GONE);
                    pr.setVisibility(View.GONE);
                }
            }
        });

        return kkk;
    }




}