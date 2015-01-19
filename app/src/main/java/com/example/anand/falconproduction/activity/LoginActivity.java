package com.example.anand.falconproduction.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anand.falconproduction.ActivityDecider;
import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.ApplicationConstants;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

  public static final String TAG = LoginActivity.class.getName();

  private EditText mUserName;
  private EditText mPassword;

  public void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mUserName = (EditText) findViewById(R.id.username);
    mPassword = (EditText) findViewById(R.id.password);
    Button mSubmitButton = (Button) findViewById(R.id.username_sign_in_button);

    mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        v.setEnabled(false);
        final String userName = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        if (userName != null && password != null && !userName.equals("") && !password.equals("")) {
          processLogin(userName, password, v);
        } else {
          Log.d(TAG, "Invalid form data submitted");
          Toast.makeText(LoginActivity.this, "Invalid form data", Toast.LENGTH_LONG).show();
          v.setEnabled(true);
        }
      }
    });
  }

  private void processLogin(final String username, final String password, final View v) {
    Log.d(TAG, "processLogin called");
    Ion.with(getApplicationContext())
        .load(ApiUrlBuilder.getLogin())
        .setBodyParameter("username", username)
        .setBodyParameter("password", password)
        .setBodyParameter("clientId", ApiUrlBuilder.getClientId())
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            Log.i(TAG, "Request complete");
            v.setEnabled(true);
            if (e == null) {
              if (result.has(ApplicationConstants.serverResponseToken)) {
                SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstants.appSharedPreference, MODE_PRIVATE);
                SharedPreferences.Editor tokenEditor = sharedPreferences.edit();
                tokenEditor.putString(ApplicationConstants.appAuthToken, result.get(ApplicationConstants.serverResponseToken).getAsString());
                tokenEditor.commit(); // commit it instantly
                Toast.makeText(LoginActivity.this, "Authentication done", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, ActivityDecider.class));
                return;
              }
              Log.i(TAG, "Output - " + result.toString());
            } else {
              Log.e(TAG, "Some exception occured - " + e.toString());
            }
            Log.e(TAG, "Authentication failed");
            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_LONG).show();
          }
        });
  }

  /**
   * If user is in LoginActivity we do not allow him to go back to any other
   * Activity except change client activity.
   */
  @Override
  public void onBackPressed() {
  }
}



