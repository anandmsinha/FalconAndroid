package com.example.anand.falconproduction.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.example.anand.falconproduction.utility.ApiUrlBuilder;
import com.example.anand.falconproduction.utility.FormSubmissionUtility;
import com.example.anand.falconproduction.utility.FormValidator;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by anand on 20/1/15.
 * <p/>
 * Activity for editing action
 */
public class EditActionActivity extends BaseDrawerActivity implements ProcessAfterDrawer, View.OnClickListener {

  private static final String TAG = EditActionActivity.class.getName();

  long baId;
  long actionId;
  RequestForm updateForm;
  ProgressDialog mProgressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit_action_activity, this);
  }

  public void fillDetails(int groupsCount) {
    Log.d(TAG, "fillDetails called");
    baId = intentBundle.getLong("baId");
    actionId = intentBundle.getLong("actionId");
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle(s(R.string.loading));
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();
    Ion.with(this)
        .load(ApiUrlBuilder.getUpdateActionForm(baId, actionId))
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (e != null || !result.has("actionDisplayGroups")) {
              Log.e(TAG, "Error in fetching update action form");
              progressDialog.dismiss();
              Toast.makeText(EditActionActivity.this,
                  s(R.string.error_upd_form), Toast.LENGTH_SHORT).show();
            } else {
              Log.d(TAG, "Update action form successfully fetched.");
              updateForm = new RequestForm(result, baId, true);
              buildUi(progressDialog);
            }
          }
        });
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG, "On activity result called");
    if (resultCode == RESULT_OK && requestCode > 0) {
      ArrayList<File> files = (ArrayList<File>) data.getSerializableExtra("upload");
      UiBuilder.addFilesToList((long) requestCode, files, updateForm);
    }
  }

  private void buildUi(ProgressDialog progressDialog) {
    Log.d(TAG, "buildUi called");
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.edit_action_main_content);
    if (updateForm != null) {
      TextView formTitle = (TextView) findViewById(R.id.main_action_edit_title);
      formTitle.setText(updateForm.getFormDisplayName());
      UiBuilder.buildFormUi(this, mainLayout, updateForm, this);
    }
    progressDialog.dismiss();
  }

  @Override
  public void onClick(View view) {
    Log.d(TAG, "Form submission started.");
    view.setEnabled(false);
    FormValidator formValidator = updateForm.isValid();
    if (formValidator.getErrorMessages().isEmpty()) {
      if (!formValidator.isTextError()) {
        mProgressDialog = UiBuilder.createProgressDialog(this, s(R.string.submission_progress), s(R.string.preparing_form));
        new FormSubmissionUtility(this, mProgressDialog, updateForm, true, baId, authToken, actionId, groupId);
      }
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    view.setEnabled(true);
  }

  public long getBaId() {
    return baId;
  }


}
