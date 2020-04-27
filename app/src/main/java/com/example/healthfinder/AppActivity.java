package com.example.healthfinder;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthfinder.entities.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class AppActivity extends AppCompatActivity {

    private Long userID;
    private AppDatabase AppDb;

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

        userID = intent.getLongExtra("userID", 0);

    }


    public long getCurrentUserID(){
        return userID;
    }

    public String getCurrentFirstName(long currentUserID){
        return AppDb.userDao().getfNameByID(currentUserID);
    }

    public String getCurrentLastName(long currentUserID){
        return AppDb.userDao().getlNameByID(currentUserID);
    }

    public String getCurrentEmail(long currentUserID){
        return AppDb.userDao().getemailByID(currentUserID);
    }

    public void setCurrentFirstName(final long userID, final String userFirstName){
        AppExecutors.getInstance().diskIO().execute(new Runnable(){
            @Override
            public void run(){
                AppDb.userDao().updateLastName(userID, userFirstName);
            }
        });
    }

    public void setCurrentLastName(final long userID, final String userLastName){
        AppExecutors.getInstance().diskIO().execute(new Runnable(){
            @Override
            public void run(){
                AppDb.userDao().updateLastName(userID, userLastName);
            }
        });
    }
}
