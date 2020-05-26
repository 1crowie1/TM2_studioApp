package com.example.healthfinder.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.healthfinder.R;
import com.example.healthfinder.entities.Consultation;
import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class DashboardFragment extends Fragment {



    private TextView activeText;
    private TextView title;
    private TextView emailText;
    private TextView nameText;
    private TextView emailValue;
    private TextView nameValue;
    private TextView informationText;
    private TextView specialitiesText;
    private EditText informationValue;
    private EditText specialitiesValue;
    private ImageButton deleteButton;

    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDoctor;
    private DatabaseReference mConsultation;
    private DatabaseReference mChats;

    private String uid;
    private String patId;
    private String docId;
    private String contactEmail;
    private String contactName;
    private String details;
    private String specialities;
    private String clinic;

    private boolean docStatus;
    private boolean chats;

    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_dashboard, container, false);


        activeText = (TextView) view.findViewById(R.id.activeText);
        emailText = (TextView) view.findViewById(R.id.contactEmailDisplay);
        nameText = (TextView) view.findViewById(R.id.contactNameDisplay);
        emailValue = (TextView) view.findViewById(R.id.contactEmailValue);
        nameValue = (TextView) view.findViewById(R.id.contactNameValue);
        informationText = (TextView) view.findViewById(R.id.informationDisplay);
        informationValue = (EditText) view.findViewById(R.id.informationValue);
        deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        title = (TextView) view.findViewById(R.id.titleDisplay);
        specialitiesText = (TextView) view.findViewById(R.id.specialitiesText);
        specialitiesValue = (EditText) view.findViewById(R.id.specialitiesDisplayValue);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users/");
        mDoctor = mDatabase.child("doctors/");
        mConsultation = mDatabase.child("consultations/");

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        chats = false;

        checkDoctor(uid);


        return view;
    }


    private void checkDoctor(final String uid){
        final String temp = uid;
        mDoctor.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    docStatus = true;
                    displayDoctorConsultations(uid);

                }
                else{
                    docStatus = false;
                    displayPatientConsultations(uid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

    }

    private void displayPatientConsultations(final String uid){
        final String temp = uid;
        mConsultation.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chats = true;
                    Consultation consultation = dataSnapshot.getValue(Consultation.class);
                    docId = consultation.docId;
                    patId = uid;
                    contactEmail = consultation.docEmail;
                    details = consultation.details;
                    setNameClinic(docId);
                }
                else{
                    chats = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }

    private void displayDoctorConsultations(final String uid){

    }

    private void setNameClinic(final String docId){
        mDoctor.child(docId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                specialities = doctor.specialties;
                clinic = doctor.clinic;
                setInfo();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

        mUser.child(docId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                contactName = user.username;
                setInfo();
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }

    private void setInfo(){
        emailValue.setText(contactEmail);
        nameValue.setText(contactName);
        informationValue.setText(details);
        specialitiesValue.setText(specialities);
        updateUI();
    }

    @Override
    public void onStart() {
          super.onStart();
        updateUI();
    }

    private void updateUI(){
          if(chats){
              activeText.setVisibility(View.GONE);
              emailValue.setVisibility(View.VISIBLE);
              emailText.setVisibility(View.VISIBLE);
              nameValue.setVisibility(View.VISIBLE);
              nameText.setVisibility(View.VISIBLE);
              informationValue.setVisibility(View.VISIBLE);
              informationText.setVisibility(View.VISIBLE);
              title.setVisibility(View.VISIBLE);
              deleteButton.setVisibility(View.VISIBLE);
              specialitiesText.setVisibility(View.VISIBLE);
              specialitiesValue.setVisibility(View.VISIBLE);
          }
          else{
              activeText.setVisibility(View.VISIBLE);
              activeText.setText("No Active Consultations");
              emailValue.setVisibility(View.GONE);
              emailText.setVisibility(View.GONE);
              nameValue.setVisibility(View.GONE);
              nameText.setVisibility(View.GONE);
              informationValue.setVisibility(View.GONE);
              informationText.setVisibility(View.GONE);
              title.setVisibility(View.GONE);
              deleteButton.setVisibility(View.GONE);
              specialitiesValue.setVisibility(View.GONE);
              specialitiesText.setVisibility(View.GONE);
              specialitiesValue.setVisibility(View.GONE);
          }
    }

}
