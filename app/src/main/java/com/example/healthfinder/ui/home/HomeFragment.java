package com.example.healthfinder.ui.home;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.healthfinder.AppActivity;

import com.example.healthfinder.R;
import com.example.healthfinder.entities.Doctor;
import com.example.healthfinder.entities.User;
import com.example.healthfinder.ui.DocDialog;
;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    //xml elements
    private TextView fullName;
    private TextView email;
    private ImageView profilePic;
    private FirebaseUser user;
    private Button signOutButton;
    private Button docButton;
    private TextView clinicPrint;
    private TextView regPrint;
    private TextView regValue;
    private TextView clinicValue;
    private Button resetButton;
    private EditText specialtiesValue;
    private TextView specialtiesText;

    //variables for displaying information on user page
    private String uid;
    private String name;
    private String emailAd;
    private Uri photoUrl;
    //doctor specific variables
    private String reg;
    private String clinic;
    private String specialties;
    //arbitrary launch code for creating popup window
    final static int LAUNCH = 1;
    //Database references
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDoctor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_home, container, false);

        //Assign variables to xml elements
        fullName = (TextView) view.findViewById(R.id.nameText);
        email = (TextView) view.findViewById(R.id.emailText);
        profilePic = (ImageView) view.findViewById(R.id.profileImage);
        clinicPrint = (TextView) view.findViewById(R.id.clinText);
        clinicValue = (TextView) view.findViewById(R.id.specialText);
        regPrint = (TextView) view.findViewById(R.id.regText);
        regValue = (TextView) view.findViewById(R.id.regNumberText);
        specialtiesValue = (EditText) view.findViewById(R.id.specialtiesValue);
        specialtiesText = (TextView) view.findViewById(R.id.specialHomeText);
        //Instantiate database references
        //Makes querying/reading data easier
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = mDatabase.child("users/");
        mDoctor = mDatabase.child("doctors/");

        final AppActivity activity = (AppActivity) getActivity();

        //Firebase user object created based on the google account currently signed in
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            currentSignOut(); //return to main page if user not logged in
        }
        else{
            //information grabbed from current account signed in
            uid = user.getUid();
            name = user.getDisplayName();
            emailAd = user.getEmail();
            photoUrl = user.getPhotoUrl();
        }


        //Check if the user is already in database. If not add them to it
        checkIfUserExists(uid);

        fullName.setText(name);
        email.setText(emailAd);
        Glide.with(this).load(photoUrl).into(profilePic);
        //profilePic.setImageBitmap();

        //Listener to handle sign out
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

        //Listener to handle registration as a doctor
        //opens DocDialog
        docButton = view.findViewById(R.id.docButton);
        docButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent i = new Intent(activity, DocDialog.class);
                startActivityForResult(i, LAUNCH);
            }
        });

        return view;
    }

    private void reset(){
        mUser.child(uid).child("doctorStatus").setValue(false);
        mDoctor.child(uid).removeValue();
    }

    private void currentSignOut(){
        //Run sign-out method in AppActivity to close activity
        AppActivity activity = (AppActivity) getActivity();
        activity.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Hide doctor information by default
          if (reg == null) {
            regPrint.setVisibility(View.INVISIBLE);
            regValue.setVisibility(View.INVISIBLE);
            clinicPrint.setVisibility(View.INVISIBLE);
            clinicValue.setVisibility(View.INVISIBLE);
            specialtiesText.setVisibility(View.INVISIBLE);
            specialtiesValue.setVisibility(View.INVISIBLE);
            docButton.setVisibility(View.VISIBLE);
        }else{
            setRegClinicSpecialties(reg, clinic, specialties);
            updateDoctorUI();
        }

    }

    private void writeNewUser(String userId, String name, String email) {
        //Creates a user in the database according to user entity constructor
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private void writeNewDoctor(String userId, String reg, String clinic, String specialties){
        //Creates a doctor in the database according to doctor entity constructor
        Doctor doctor = new Doctor(reg, clinic, specialties);
        mDatabase.child("doctors").child(userId).setValue(doctor);
        mUser.child(userId).child("doctorStatus").setValue(true);
        updateDoctorUI();
    }

    private void checkIfUserExists(String uid){
        //Method to check if user is already in database
        //Listener takes a snapshot of live data from the database
        final String temp = uid;
        //Below comment used for testing
        mUser.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){ //if user already exists

                    User user = dataSnapshot.getValue(User.class);
                    Boolean status = user.doctorStatus; //check if they are a doctor and grab info
                    if (status){
                        readRegClinicSpecialties(temp);
                        updateDoctorUI(); // if they are a doctor update UI with info
                    }
                }else{
                    writeNewUser(temp, name, emailAd); // add new user
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

    }

    private void readRegClinicSpecialties(String uid){
        //Listener to grab current doctor registration and clinic
        mDoctor.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                String tempReg = doctor.regNo;
                String tempClinic = doctor.clinic;
                String tempSpecialties = doctor.specialties;
                setRegClinicSpecialties(tempReg, tempClinic, tempSpecialties);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});
    }

    private void setRegClinicSpecialties(String reg, String clinic, String specialties){
        //Setter method for reg and clinic after reading them live
        //Live data is read asynchronous to main thread
        //Setter ensures UI is updated before method ends
        this.reg = reg;
        this.clinic = clinic;
        this.specialties = specialties;
        regValue.setText(reg);
        clinicValue.setText(clinic);
        specialtiesValue.setText(specialties);
    }

    private void updateDoctorUI(){
        //Method runs if user is recognized as a doctor
        //Makes doctor specific fields visible
        //Hides option to register as a doctor
        regPrint.setVisibility(View.VISIBLE);
        regValue.setVisibility(View.VISIBLE);
        clinicPrint.setVisibility(View.VISIBLE);
        clinicValue.setVisibility(View.VISIBLE);
        specialtiesText.setVisibility(View.VISIBLE);
        specialtiesValue.setVisibility(View.VISIBLE);
        docButton.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Grabs data from doctor dialog form after a user registers successfully
        if (requestCode == LAUNCH) {
            if(resultCode == Activity.RESULT_OK){
                reg = data.getStringExtra("reg");
                clinic = data.getStringExtra("clinic");
                specialties = data.getStringExtra("specialties");
                writeNewDoctor(uid, reg, clinic, specialties);

            }
            if (resultCode == Activity.RESULT_CANCELED) {}
        }
    }//onActivityResult

}
