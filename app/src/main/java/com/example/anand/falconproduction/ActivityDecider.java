package com.example.anand.falconproduction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.anand.falconproduction.activity.LoginActivity;
import com.example.anand.falconproduction.activity.MainActivity;
import com.example.anand.falconproduction.utility.ApplicationConstants;


public class ActivityDecider extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences =
                getSharedPreferences(ApplicationConstants.appSharedPreference, MODE_PRIVATE);
        Intent mainIntenet;
        if (sharedPreferences.contains(ApplicationConstants.appAuthToken)) {
            mainIntenet = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            mainIntenet = new Intent(getApplicationContext(), LoginActivity.class);
        }
        startActivity(mainIntenet);
    }

}
