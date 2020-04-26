package com.example.healthfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthfinder.entities.User;
import com.example.healthfinder.model.CurrentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.healthfinder.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;
import java.io.*;

public class AppActivity extends AppCompatActivity {

    private AppDatabase AppDb;


    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Uri userPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        AppDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();

        userFirstName = intent.getStringExtra("userFirstName");
        userLastName = intent.getStringExtra("userLastName");
        userEmail = intent.getStringExtra("userEmail");
        userPhoto = intent.getData();


        //add user to database
        addNewUser(userFirstName, userLastName, userEmail);



    }


    public void addNewUser(final String userFirstName, final String userLastName, final String userEmail){
        //AppExecutor to run database population on background thread
        AppExecutors.getInstance().diskIO().execute(new Runnable(){
            @Override
            public void run(){
                //check if user exists by searching database for existing email
                List<User> users = AppDb.userDao().getExistingUsers(userEmail);
                if(users != null && !users.isEmpty()){ //if user is new, add them to database
                    User user = new User(userFirstName, userLastName, userEmail);
                    AppDb.userDao().insert(user);
                }
            }
        });

    }



}
