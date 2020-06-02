package com.example.healthfinder.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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


    private static final int LAUNCH = 298 ;
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
    private ImageButton locationButton;

    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDoctor;
    private DatabaseReference mConsultation;


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
        locationButton = (ImageButton) view.findViewById(R.id.locationButton);
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

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.deleteButton:
                            approveConsultation();
                        break;
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.locationButton:
                        searchMaps(clinic);
                        break;
                }
            }
        });


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
        mConsultation.orderByChild("docId").equalTo(uid).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chats=true;
                    docId = uid;
                    patId = "";
                    specialities = "";
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        contactEmail = child.child("patEmail").getValue(String.class);
                        details = child.child("details").getValue(String.class);
                    }

                    setName(contactEmail);
                }else{
                    chats=false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
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

    private void setName(String patEmail){
        mUser.orderByChild("email").equalTo(patEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                    String tempId = "";
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        tempId = child.getKey();
                        patId = tempId;
                        User user = child.getValue(User.class);
                        contactName = user.username;
                        setInfo();
                    }

            }
            @Override
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
              locationButton.setVisibility(View.VISIBLE);

              if(docStatus){
                  specialitiesValue.setVisibility(View.GONE);
                  specialitiesText.setVisibility(View.GONE);
                  locationButton.setVisibility(View.GONE);
              }
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
              locationButton.setVisibility(View.GONE);
          }

    }

    private void approveConsultation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("    This will delete the consultation request")
                .setTitle("Approve Consultation")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mConsultation.child(patId).removeValue();
                        refreshUI();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){}})
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void refreshUI(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    private void searchMaps(String clinicAddress){
        String map = "http://maps.google.co.in/maps?q=" + clinicAddress;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);
    }

}
