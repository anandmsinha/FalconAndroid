package com.example.anand.falconproduction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.anand.falconproduction.activity.ClientDeciderActivity;
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
    if (sharedPreferences.contains(ApplicationConstants.clientToken)) {
      String token = sharedPreferences.getString(ApplicationConstants.clientToken, "token");
      String url = "";
      if (token.equals("anand")) {
        url = "http://192.168.0.04:8080/falcon-dms/rest/api/";
        ApplicationConstants.setClientId(1L);
      } else if (token.equals("6081test")) {
        url = "https://mytbits.com:6081/falcon-dms/rest/api/";
        ApplicationConstants.setClientId(1L);
      } else if (token.equals("3081test")) {
        url = "https://mytbits.com:3081/falcon-dms/rest/api/";
        ApplicationConstants.setClientId(1L);
      }
      if (!url.equals("")) {
        ApplicationConstants.setBaseAppUrl(url);
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
