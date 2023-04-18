package com.xeneen.logoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity2 extends AppCompatActivity {

    TextView uname, email;
    ImageView pic;
    Button sout;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        uname = findViewById(R.id.uname);
        email = findViewById(R.id.email);
        pic = findViewById(R.id.pic);
        sout = findViewById(R.id.sout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount aaa = GoogleSignIn.getLastSignedInAccount(this);
        if(aaa != null){
            String a = aaa.getDisplayName();
            String e = aaa.getEmail();
            uname.setText(a); email.setText(e);

        }

        sout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sOut();
            }
        });

    }

    private void sOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                Intent inta = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(inta);

            }
        });
    }
}