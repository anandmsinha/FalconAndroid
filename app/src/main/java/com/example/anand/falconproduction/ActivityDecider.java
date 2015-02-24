package com.example.anand.falconproduction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.anand.falconproduction.activity.ClientDeciderActivity;
import com.example.anand.falconproduction.activity.LoginActivity;
import com.example.anand.falconproduction.activity.MainActivity;
import com.example.anand.falconproduction.utility.ApplicationConstants;

/*
 * This activity is default activity from launcher after coming to this activity
 * next activity is decided from here based upon auth-token and client token.
 */
public class ActivityDecider extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  protected void onResume() {
    super.onResume();
    SharedPreferences sharedPreferences =
        getSharedPreferences(ApplicationConstants.appSharedPreference, MODE_PRIVATE);
    if (sharedPreferences.contains(ApplicationConstants.clientToken)) {
      String token = sharedPreferences.getString(ApplicationConstants.clientToken, "token");
      if (ApplicationConstants.clientTokes.containsKey(token)) {
        Intent mainIntenet;
        ApplicationConstants.setClientId(1L);
        ApplicationConstants.setBaseAppUrl(ApplicationConstants.clientTokes.get(token));
        if (sharedPreferences.contains(ApplicationConstants.appAuthToken)) {
          mainIntenet = new Intent(this, MainActivity.class);
        } else {
          mainIntenet = new Intent(this, LoginActivity.class);
        }
        startActivity(mainIntenet);
        return;
      }
    }
    startActivity(new Intent(this, ClientDeciderActivity.class));
  }

}
