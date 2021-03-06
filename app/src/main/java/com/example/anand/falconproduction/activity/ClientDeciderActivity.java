package com.example.anand.falconproduction.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anand.falconproduction.ActivityDecider;
import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.utility.ApplicationConstants;

/**
 * Created by anand on 23/12/14.
 *
 * This class let's user decide the client. User can input a token which is client
 * token it helps in deciding the base url for api.
 */
public class ClientDeciderActivity extends Activity {

  public void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.client_d_layout);
    final EditText tokenText = (EditText) findViewById(R.id.clientToken);
    Button submitButton = (Button) findViewById(R.id.token_submit_button);
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = tokenText.getText().toString();
        if (ApplicationConstants.clientTokes.containsKey(text)) {
          SharedPreferences sharedPreferences = getSharedPreferences(ApplicationConstants.appSharedPreference, MODE_PRIVATE);
          SharedPreferences.Editor tokenEditor = sharedPreferences.edit();
          tokenEditor.putString(ApplicationConstants.clientToken, text);
          tokenEditor.apply();
          startActivity(new Intent(ClientDeciderActivity.this, ActivityDecider.class));
        } else {
          Toast.makeText(ClientDeciderActivity.this, getResources().getString(R.string.invalid_token), Toast.LENGTH_LONG).show();
        }
      }
    });
  }

}
