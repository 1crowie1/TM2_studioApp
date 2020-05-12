package com.example.healthfinder.ui.notifications;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.healthfinder.AppActivity;
import com.example.healthfinder.R;
import com.example.healthfinder.entities.Consultation;
import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.Prescription;
import com.example.healthfinder.entities.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class NotificationsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDoctor;
    private DatabaseReference mConsultation;

    private TextView title;
    private TextView nameTitle;
    private EditText nameValue;
    private EditText details;
    private TextView urgentText;
    private Switch urgentSwitch;
    private Button submitButton;

    private String uid;
    private boolean docStatus;

    final static int LAUNCH = 44;

    private FirebaseUser user;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_notifications, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users/");
        mDoctor = mDatabase.child("doctors/");
        mConsultation = mDatabase.child("consultations/");
        user = FirebaseAuth.getInstance().getCurrentUser();

        title = (TextView) view.findViewById(R.id.consultTitle);
        nameTitle = (TextView) view.findViewById(R.id.userTitle);
        nameValue = (EditText) view.findViewById(R.id.nameValue);
        details = (EditText) view.findViewById(R.id.detailsValue);
        urgentText = (TextView) view.findViewById(R.id.urgentCaseText);
        urgentSwitch = (Switch) view.findViewById(R.id.urgentCaseSwitch);

        uid = user.getUid();

        checkDoctorUI(uid);

        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.submitButton:
                        if(docStatus){
                            checkPrescription(nameValue.getText().toString(), details.getText().toString());
                        }
                        else {
                            checkConsultation(nameValue.getText().toString(), details.getText().toString(), urgentSwitch.isChecked());
                        }
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        title.setText("Consultation Form");
        nameTitle.setText("Doctor Email:");
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
        nameTitle.setText("Patient Email:");
        urgentText.setVisibility(View.GONE);
        urgentSwitch.setVisibility(View.GONE);
        docStatus = true;
    }
    private void resetUI(){
        nameValue.setText("");
        details.setText("");
        urgentSwitch.setChecked(false);

    }

    private void checkConsultation(final String docEmail, final String details, final boolean urgency){
        mUser.orderByChild("email").equalTo(docEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()) {
                    String tempId = "";
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        tempId = child.getKey();
                    }
                    writeNewConsultation(docEmail, details, urgency, tempId);
                } else{
                    Toast.makeText(getActivity(), "Invalid recipient", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }

    private void checkPrescription(final String patEmail, final String details){
        mUser.orderByChild("email").equalTo(patEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String tempId = ""; //string
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        tempId = child.getKey();
                    }
                    writeNewPrescription(patEmail, details, tempId);
                }else{
                    Toast.makeText(getActivity(), "Invalid recipient", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }

    private void writeNewConsultation(String docEmail, String details, boolean urgency, String docId){
        Consultation consultation = new Consultation(user.getEmail(), docEmail, details, urgency, docId);
        mDatabase.child("consultations").child(uid).setValue(consultation);
        sendConsultation(docEmail, details, urgency);
    }

    private void writeNewPrescription(String patEmail, String details, String patId){
        Prescription prescription = new Prescription(user.getEmail(), patEmail, details);
        mDatabase.child("prescriptions").child(patId).setValue(prescription);
        sendPrescription(patEmail, details);
    }

    private void sendConsultation(String docEmail, String details, boolean urgency){
        String subject = "Consultation Request";

        if(urgency){
            subject = "URGENT Consultation Request";
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{docEmail});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , details);

        try {
            startActivityForResult(Intent.createChooser(i, "Send mail..."), LAUNCH);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPrescription(String patEmail, String details){

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{patEmail});
        i.putExtra(Intent.EXTRA_SUBJECT, "Prescription Form");
        i.putExtra(Intent.EXTRA_TEXT   , details);

        try {
            startActivityForResult(Intent.createChooser(i, "Send mail..."), LAUNCH);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH) {
                resetUI();
                Toast.makeText(getActivity(), "Your mail client has handled your request", Toast.LENGTH_SHORT).show();
        }
    }//onActivityResult
}
