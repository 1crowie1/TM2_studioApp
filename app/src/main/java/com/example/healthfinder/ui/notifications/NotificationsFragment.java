package com.example.healthfinder.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Database;

import com.example.healthfinder.AppActivity;
import com.example.healthfinder.R;
import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDoctor;

    private TextView title;
    private TextView nameTitle;
    private EditText nameValue;
    private EditText details;
    private TextView urgentText;
    private Switch urgentSwitch;

    private String uid;

    private FirebaseUser user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_notifications, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users/");
        mDoctor = mDatabase.child("doctors/");
        user = FirebaseAuth.getInstance().getCurrentUser();

        final AppActivity activity = (AppActivity) getActivity();

        title = (TextView) view.findViewById(R.id.consultTitle);
        nameTitle = (TextView) view.findViewById(R.id.userTitle);
        nameValue = (EditText) view.findViewById(R.id.nameValue);
        details = (EditText) view.findViewById(R.id.detailsValue);
        urgentText = (TextView) view.findViewById(R.id.urgentCaseText);
        urgentSwitch = (Switch) view.findViewById(R.id.urgentCaseSwitch);

        uid = user.getUid();

        checkDoctorUI(uid);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        title.setText("Consultation Form");
        nameTitle.setText("Doctor Name");
        urgentText.setVisibility(View.VISIBLE);
        urgentSwitch.setVisibility(View.VISIBLE);
        urgentSwitch.setChecked(false);
    }

    private void checkDoctorUI(String uid){
        final String temp = uid;
        mDoctor.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    updateDoctorUI();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

    }

    private void updateDoctorUI(){
        title.setText("Prescription Form");
        nameTitle.setText("Patient Name:");
        urgentText.setVisibility(View.GONE);
        urgentSwitch.setVisibility(View.GONE);
    }
}
