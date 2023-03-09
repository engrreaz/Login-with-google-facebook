package com.xeneen.push;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//dhI6iMJrTEqNJqt2EOc0FS:APA91bEYvZCakucWvHSr1MiiCpMRjVqabDmCiTBBwLJrpRHA12418jBFcfWWQ9M8gjjSt7gIm6L8FVJjmPRR9KGnSYVkpTrBCXs-M6NQm5-OS-DDrjMq6lDJGv3cIgZU1MKapetL0JGd
public class MainActivity extends AppCompatActivity {
    EditText etToken;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView longi, lati;

    private  final  static int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etToken = findViewById(R.id.etToken);

        longi=findViewById(R.id.longi);
        lati=findViewById(R.id.lati);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);


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
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                        etToken.setText(token);
                    }
                });
        getLastLocation();
    }




    private void getLastLocation() {
        Toast.makeText(MainActivity.this, "One", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Two", Toast.LENGTH_SHORT).show();
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Toast.makeText(MainActivity.this, "Three", Toast.LENGTH_SHORT).show();
                            if (location !=null){
                                Toast.makeText(MainActivity.this, "Four", Toast.LENGTH_SHORT).show();
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    Toast.makeText(MainActivity.this, "Five", Toast.LENGTH_SHORT).show();
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    lati.setText("Lati :" +addresses.get(0).getLatitude());
                                    longi.setText("Longi :"+addresses.get(0).getLongitude());
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
}