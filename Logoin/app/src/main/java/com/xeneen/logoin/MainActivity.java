package com.xeneen.logoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView gbtn;

    int bc = 0;
    LoginButton fbb;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);



        setContentView(R.layout.activity_main);
        gbtn = findViewById(R.id.imageView);
        fbb = findViewById(R.id.login_button);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        //signIn();
        gbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bc = 1;
                signIn();
            }
        });


        callbackManager = CallbackManager.Factory.create();
        fbb.setPermissions(Arrays.asList("user_gender, user_friends"));

        fbb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "CANCllllllllllllllllll", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(MainActivity.this, "EEEEEEEEEE", Toast.LENGTH_SHORT).show();
            }
        });




        fbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FACEBOOK
                bc = 2;
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
            }
        });
    }



    private void signIn() {
        Intent signinIntent = gsc.getSignInIntent();
        startActivityForResult(signinIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(bc == 2){
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);

            GraphRequest gr = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                            String ap = jsonObject.toString();
                            Toast.makeText(MainActivity.this, ap, Toast.LENGTH_SHORT).show();
                        }
                    });

            Bundle bundle = new Bundle();
            bundle.putString("fields", "gender, name, id, first_name, last_name");

            gr.setParameters(bundle);
            gr.executeAsync();


        }
        else if(bc == 1){
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1000){
                Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);

                try {
                    task.getResult(ApiException.class);
                    navigate2();
                } catch (ApiException e) {
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    AccessTokenTracker att = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
            if(accessToken1 == null){
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        att.stopTracking();
    }

    private void navigate2() {
        finish();
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}