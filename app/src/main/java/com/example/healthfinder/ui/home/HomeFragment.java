package com.example.healthfinder.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.healthfinder.AppActivity;
import com.example.healthfinder.MainActivity;
import com.example.healthfinder.R;
import com.example.healthfinder.entities.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URL;

public class HomeFragment extends Fragment {

    private TextView fullName;
    private TextView email;
    private ImageView profilePic;
    FirebaseUser user;
    private Button signOutButton;

    private String name;
    private String emailAd;
    private Uri photoUrl;

    private DatabaseReference mDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_home, container, false);

        fullName = (TextView) view.findViewById(R.id.nameText);
        email = (TextView) view.findViewById(R.id.emailText);
       profilePic = (ImageView) view.findViewById(R.id.profileImage);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        AppActivity activity = (AppActivity) getActivity();



        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            currentSignOut();
        }
        else{
            name = user.getDisplayName();
            emailAd = user.getEmail();
            photoUrl = user.getPhotoUrl();
        }
        writeNewUser(user.getUid(), name, emailAd);
        fullName.setText(name);
        email.setText(emailAd);

        Glide.with(this).load(photoUrl).into(profilePic);
        //profilePic.setImageBitmap();

        signOutButton = view.findViewById(R.id.signOutButton);

        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.signOutButton:
                        currentSignOut();
                        break;
                }
            }
        });



        return view;
    }

    private void currentSignOut(){
        AppActivity activity = (AppActivity) getActivity();
        activity.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }




}
