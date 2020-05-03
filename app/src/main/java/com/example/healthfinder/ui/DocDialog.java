package com.example.healthfinder.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthfinder.R;
import com.google.android.material.snackbar.Snackbar;

public class DocDialog extends AppCompatActivity {

    private TextView regText;
    private TextView clinicText;
    private Button register;

    private String reg;
    private String clinic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.docdialog);

        //XML elements
        regText = (TextView) findViewById(R.id.regEdit);
        clinicText = (TextView) findViewById(R.id.clinicEdit);
        register = (Button) findViewById(R.id.register);

        //Instantiate fields
        reg = "";
        clinic = "";
        //Add ontextchange listeners to text fields
        checkText();

        //Listener to handle registration attempt
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switch (v.getId()){
                    case R.id.register:
                        grabData();
                        break;
                }
            }
        });
    }

    private void checkText(){
        //When the user updates text fields they are stored as new data in variables
        regText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                reg = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        clinicText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clinic = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void grabData(){
        //If either text field is blank, user gets an error popup
        if(reg == "" || clinic == ""){
            Snackbar.make(findViewById(android.R.id.content).getRootView(),
                    "Clinic and Registration Required",
                    Snackbar.LENGTH_SHORT).show();
        }else { // if both fields are filled out, data is stored and sent back to home fragment
            Intent returnIntent = new Intent();
            returnIntent.putExtra("reg", reg);
            returnIntent.putExtra("clinic", clinic);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }


}
