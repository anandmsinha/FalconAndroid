package com.example.anand.falconproduction.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.FormSubmissionUtility;
import com.example.anand.falconproduction.utility.FormValidator;
import com.example.anand.falconproduction.utility.LayoutBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 16/12/14.
 * <p/>
 * Activity class to handle create request of Ba.
 */
public class CreateRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer, View.OnClickListener {

  private static final String TAG = CreateRequestActivity.class.getName();
  long baId;
  RequestForm requestForm;
  ProgressDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstance) {
    Log.d(TAG, "onCreate called");
    super.onCreate(savedInstance);
    setContentView(R.layout.create_request, this);
  }

  @Override
  public void fillDetails(int groupCount) {
    Log.d(TAG, "fillDetails called");
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
              progressDialog.dismiss();
              return;
            }

            requestForm = new RequestForm(result, baId);
            buildUi(progressDialog);
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
    Log.d(TAG, "startActivity called");
    intent.putExtra("baId", baId);
    intent.putExtra("group", groupId);
    super.startActivity(intent);
  }

  /**
   * Set files parameter of FieldAdvanced after recieving intent data from file upload
   * activity
   *
   * @param requestCode - id of FieldAdvanced
   * @param resultCode  - result code
   * @param data        - actual data
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG, "onActivityResult called");
    if (requestCode > 0 && resultCode == RESULT_OK) {
      ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra("upload");
      UiBuilder.addFilesToList((long) requestCode, files, requestForm);
    }
  }

  private void buildUi(ProgressDialog progressDialog) {
    Log.d(TAG, "buildUi called");
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_request_create_content);
    if (requestForm != null) {
      UiBuilder.buildFormUi(this, mainLayout, requestForm, this);
    }
    progressDialog.dismiss();
  }

  @Override
  public void onClick(View v) {
    Log.d(TAG, "onClick called");
    v.setEnabled(false);

    FormValidator formValidator = requestForm.isValid();
    if (formValidator.getErrorMessages().isEmpty()) {
      if (!formValidator.isTextError()) {
        mProgressDialog = UiBuilder.createProgressDialog(this, "Submission in progress", "Preparing form");
        new FormSubmissionUtility(this, mProgressDialog, requestForm, false, baId, authToken, 0, groupId);
      }
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(CreateRequestActivity.this);
      StringBuilder stringBuilder = new StringBuilder();
      for (String tmp : formValidator.getErrorMessages()) {
        stringBuilder.append(tmp);
      }
      builder.setMessage(stringBuilder.toString())
          .setCancelable(false)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
          });
      AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }
    v.setEnabled(true);
  }

  @Override
  public long getBaId() {
    return baId;
  }

}
