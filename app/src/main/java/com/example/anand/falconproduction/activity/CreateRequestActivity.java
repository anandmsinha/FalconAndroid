package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by anand on 16/12/14.
 *
 * Activity class to handle create request of Ba.
 */
public class CreateRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  long baId;

  @Override
  protected void onCreate(Bundle savedInstance) {
    super.onCreate(savedInstance);
    setContentView(R.layout.create_request);
  }

  @Override
  public void fillDetails(int groupCount) {
    baId = intentBundle.getLong("baId");
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Loading....");
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();
    Ion.with(this)
        .load(ApiUrlBuilder.getRequestForm(baId))
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
           if (e != null || !result.has("actionDisplayGroups")) {
             Toast.makeText(CreateRequestActivity.this, "Failed to load form try again", Toast.LENGTH_LONG).show();
             return;
           }
          }
        });
  }

  /**
   * This method has been overriden so that we can put ba id into intent.
   *
   * @param intent - passing intent
   */
  @Override
  public void startActivity(Intent intent) {
    intent.putExtra("baId", baId);
    super.startActivity(intent);
  }
}
