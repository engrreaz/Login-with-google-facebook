package com.xeneen.roseprecadetschool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.xeneen.roseprecadetschool.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView username, userlevel;
    ImageView avatar;
    NavigationView nview;
    View headerView;
    String lnk, sublnk;
    SharedPreferences stock;
    double posx, posy;

    private final static int REQUEST_CODE = 100;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //drawer = findViewById(R.id.drawer_layout);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(
                //view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show()
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "Test Toast", Toast.LENGTH_SHORT).show();
                        Bundle bdl = new Bundle();
                        String ups = "https://rose.xeneen.com/notification.php";
                        bdl.putString("message", ups );
                        int netstatus;
                        if(isConnected()){ netstatus = 1;} else {netstatus = 0;}
                        bdl.putInt("netstatus", netstatus);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bdl)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                    }
                }
        );





        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        stock = getSharedPreferences("ROSEPCS",MODE_PRIVATE);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        nview = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        username = (TextView) headerView.findViewById(R.id.userid);
        userlevel = (TextView) headerView.findViewById(R.id.userlevel);
        avatar = (ImageView) headerView.findViewById(R.id.logo3);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_dashboard, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        System.out.println(token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("ROSEPCS", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("deviceid", token);
                        myEdit.apply();

                    }
                });





        //Toast.makeText(this, info, Toast.LENGTH_LONG).show();

String fn="", ul="", pth="https://rose.xeneen.com/images/user/noimg.png";
        if(stock.getString("login","")!=""){
            fn = stock.getString("fullname", "");
            ul = stock.getString("level", "");
            pth = "https://rose.xeneen.com/images/user/"+ stock.getString("username","noimg") +".png";
        }

        username.setText(fn);
        userlevel.setText(ul);

        String url = pth;
        Glide.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .override(70,70)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(avatar);

        
        //getPhoneInfo();
        getLastLocation();


    }

    private void getPhoneInfo() {
        String mPhoneNumber = "Not Detect";
        if (ActivityCompat.checkSelfPermission(this, READ_SMS) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            mPhoneNumber="Need-Permission";
        } else {
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                mPhoneNumber = "88"+tMgr.getLine1Number();
            } else {
                mPhoneNumber = "notPOT";
            }
        }

        //Toast.makeText(this, lnk, Toast.LENGTH_LONG).show();
        String ma = Build.MANUFACTURER;
        String mo = Build.MODEL;
        sublnk  = "&manufac="+ma+"&model="+mo+"&simno="+mPhoneNumber;
        //Toast.makeText(this, lnk, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout pk = findViewById(R.id.drawer_layout);
        if(pk.isOpen()){
            pk.closeDrawer(GravityCompat.START);
        } else {
            WebView hh = this.findViewById(R.id.wb);
            if (hh.canGoBack()) {
                hh.goBack();
                Toast.makeText(MainActivity.this,"Web View Back",Toast.LENGTH_LONG).show();
            } else {
                super.onBackPressed();
            }
        }

        //TOAST ON BACK PRESS WEBVIEW/INTENT/APPS.............
        //Toast.makeText(MainActivity.this,"There is no back action",Toast.LENGTH_LONG).show();
        return;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout pk = findViewById(R.id.drawer_layout);
        pk.closeDrawer(GravityCompat.START);
        Bundle bundle = new Bundle();
        int netstatus;
        if(isConnected()){
            netstatus = 1;
        //    Toast.makeText(this, "111111111", Toast.LENGTH_SHORT).show();
        } else {
            netstatus = 0;
        //    Toast.makeText(this, "00000000000", Toast.LENGTH_SHORT).show();
        }
        bundle.putInt("netstatus", netstatus);

        lnk = "";
        int id = item.getItemId();
        if(id == R.id.nav_home){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        } else if(id == R.id.nav_about){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        } else if(id == R.id.nav_dashboard){
            lnk = "deviceid="+stock.getString("deviceid", "");;
            getPhoneInfo();
            lnk = lnk + sublnk;
            getLastLocation();
            lnk = lnk + "&geolat=" + stock.getString("posx","") + "&geolon=" + stock.getString("posy","");
            String myMessage = "https://rose.xeneen.com?" + lnk;
            bundle.putString("message", myMessage );

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
            lnk = "";
        } else if(id == R.id.nav_speech){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        } else if(id == R.id.nav_achieve){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        } else if(id == R.id.nav_academic){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        } else if(id == R.id.nav_attnd){
            getLastLocation();
            lnk = "&geolat=" + stock.getString("posx","") + "&geolon=" + stock.getString("posy","");
            String myMessage = "https://rose.xeneen.com/geoattnd.php?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        } else if(id == R.id.nav_settings){
            lnk = "netstatus="+netstatus;
            String myMessage = "file:///android_asset/test.html?" + lnk;
            bundle.putString("message", myMessage );
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_main, com.xeneen.roseprecadetschool.TestFragment.class, bundle).setReorderingAllowed(true).addToBackStack(null).commit();
            lnk = "";
        }



        return false;
    }


    public boolean isConnected () {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        return isConnected;
    }






    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null){
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    //lati.setText("Lati :" +);
                                    //longi.setText("Longi :"+);
                                    posx = addresses.get(0).getLatitude();
                                    posy = addresses.get(0).getLongitude();

                                    SharedPreferences sharedPreferences = getSharedPreferences("ROSEPCS",MODE_PRIVATE);
                                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                    myEdit.putString("posx", String.valueOf(posx));
                                    myEdit.putString("posy", String.valueOf(posy));
                                    myEdit.commit();
                                    String yy = addresses.get(0).getAddressLine(0);
                                    //Toast.makeText(MainActivity.this, yy, Toast.LENGTH_SHORT).show();

                                    //Toast.makeText(MainActivity.this, "Lati : "+posx+" Lati : " + posy, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else
        {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }

    /*public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=      PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                //textView.setText(mPhoneNumber);
                break;
        }
    }
     */
}