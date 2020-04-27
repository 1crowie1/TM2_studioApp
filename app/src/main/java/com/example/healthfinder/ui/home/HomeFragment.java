package com.example.healthfinder.ui.home;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.healthfinder.AppActivity;
import com.example.healthfinder.AppDatabase;
import com.example.healthfinder.MainActivity;
import com.example.healthfinder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HomeFragment extends Fragment {

    EditText fullName;
    TextView email;
    ImageView profilePic;
    long userID;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

       // View root = inflater.inflate(R.layout.fragment_home, container, false);
        //return root;
        //userID = getArguments().getLong("userID");
        LayoutInflater lf = getActivity().getLayoutInflater();

        View view =  lf.inflate(R.layout.fragment_home, container, false);
        fullName = (EditText) view.findViewById(R.id.nameText);
        email = (TextView) view.findViewById(R.id.emailText);
        profilePic = (ImageView) view.findViewById(R.id.profileImage);



        AppActivity activity = (AppActivity) getActivity();
        userID = activity.getCurrentUserID();

        fullName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                temp.trim();
                String userFirstName = temp.substring(0, temp.indexOf(' '));
                String userLastName = temp.substring(temp.indexOf(' ') + 1);
                updateName(userFirstName, userLastName);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        AppActivity activity = (AppActivity) getActivity();

        String temp = activity.getCurrentFirstName(userID) + " " + activity.getCurrentLastName(userID);
        fullName.setText(temp);

        email.setText(activity.getCurrentEmail(userID));

    }

    public void updateName(String userFirstName, String userLastName){
        AppActivity activity = (AppActivity) getActivity();
        activity.setCurrentFirstName(getUserID(), userFirstName);
        activity.setCurrentLastName(getUserID(), userLastName);
    }

    public long getUserID(){return userID;}

}
