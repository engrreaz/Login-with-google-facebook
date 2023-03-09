package com.xeneen.roseprecadetschool;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.xeneen.roseprecadetschool.databinding.FragmentHomeBinding;
import com.xeneen.roseprecadetschool.ui.home2.HomeViewModel;

public class HomeFragment extends Fragment {

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
    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View kkk =  inflater.inflate(R.layout.fragment_home, container, false);
        pb = kkk.findViewById(R.id.pbh);
        pr = kkk.findViewById(R.id.valgh);
        wb = kkk.findViewById(R.id.wbh);
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
        }

        wb.setWebChromeClient(new WebChromeClient(){
            //@Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress<100){
                    pb.setVisibility(View.VISIBLE);
                    pr.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                    pr.setText(newProgress + "%");
                    String str = wb.getUrl();
                    //Toast.makeText(getApplicationContext(),"Changed : "+str+" progress : " + newProgress,Toast.LENGTH_SHORT).show();
                } else {
                    pb.setVisibility(View.GONE);
                    pr.setVisibility(View.GONE);
                }
            }
        });

        return kkk;
    }
}