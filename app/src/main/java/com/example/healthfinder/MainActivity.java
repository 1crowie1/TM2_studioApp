package com.example.healthfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthfinder.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    //tag for login error
    private static final String TAG = "";
    SignInButton signIn;
    //Creating sign-in client
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Default google sign-in client to gather profile information for user account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //When sign-in button click run the signIn() method
        signIn = findViewById(R.id.getStarted);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.getStarted:
                        signIn();
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        loginExisting(account); // If user previously logged in, login with their account, supersedes normal sign in
    }

    public void loginExisting(GoogleSignInAccount account) {
        //Method runs once user is logged into valid account
        //Obtains user info from google account and sends it to the main activity

        if (account != null) {

            String userName = account.getDisplayName();
            String userFirstName = account.getGivenName();
            String userLastName = account.getFamilyName();
            String userEmail = account.getEmail();
            Uri userPhoto = account.getPhotoUrl();

            Intent myIntent = new Intent(MainActivity.this, AppActivity.class);
            myIntent.putExtra("userName", userName);
            myIntent.putExtra("userFirstName", userFirstName); //Optional parameters
            myIntent.putExtra("userLastName", userLastName);
            myIntent.putExtra("userEmail", userEmail);
            myIntent.setData(userPhoto);
            MainActivity.this.startActivity(myIntent);

        }
    }


    private void signIn() {
        //Runs google signIn option
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Creates a task to handle results of google sign-in
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            loginExisting(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Refer to the GoogleSignInStatusCodes class reference
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            loginExisting(null);
        }
    }
}