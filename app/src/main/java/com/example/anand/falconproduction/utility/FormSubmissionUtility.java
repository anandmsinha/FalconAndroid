package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.activity.MainActivity;
import com.example.anand.falconproduction.activity.ViewRequestActivity;
import com.example.anand.falconproduction.models.create.DisplayGroupAdvanced;
import com.example.anand.falconproduction.models.create.FieldAdvanced;
import com.example.anand.falconproduction.models.create.RequestForm;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anand on 3/2/15.
 * <p/>
 * Class acts as helper for form submission.
 */
public class FormSubmissionUtility {

  private static final String TAG = FormSubmissionUtility.class.getName();

  Activity activity;
  ProgressDialog progressDialog;
  RequestForm requestForm;
  boolean isUpdate;
  long baId;
  String authToken;
  long actionId;
  int group;

  public FormSubmissionUtility(Activity activity, ProgressDialog progressDialog, RequestForm requestForm, boolean isUpdate, long baId, String authToken, long actionId, int group) {
    this.activity = activity;
    this.progressDialog = progressDialog;
    this.requestForm = requestForm;
    this.isUpdate = isUpdate;
    this.baId = baId;
    this.authToken = authToken;
    this.actionId = actionId;
    Log.d(TAG, "group id recieved - " + group);
    this.group = group;
    new SubmitRequestForm().execute(this.requestForm);
  }

  /**
   * Right now we are using asynctask later on we need to find better solution
   * to this as file upload and process can be really long. Using Async Task here
   * is not totally incorrect as The main problem with long running process in AsyncTask
   * is memory leak due t change of activty but we are blocking our activity son chances
   * of memory leak should not be there.
   */
  class SubmitRequestForm extends AsyncTask<RequestForm, String, Boolean> {

    private final String TAG = SubmitRequestForm.class.getName();
    RequestForm mainForm;
    long newActionId;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      progressDialog.show();
    }

    @Override
    protected Boolean doInBackground(RequestForm... requestForms) {
      mainForm = requestForms[0];
      newActionId = 0;
      return handleFormSubmission();
    }

    @Override
    protected void onPostExecute(Boolean status) {
      if (status) {
        progressDialog.dismiss();
        Toast.makeText(activity, activity.getResources().getString(R.string.request_succ), Toast.LENGTH_LONG).show();
        Intent mainIntent;
        if (newActionId == 0) {
          mainIntent = new Intent(activity, MainActivity.class);
          activity.startActivity(mainIntent);
        } else {
          mainIntent = new Intent(activity, ViewRequestActivity.class);
          Log.d(TAG, "starting action with id " + newActionId);
          mainIntent.putExtra("actionId", newActionId);
          mainIntent.putExtra("baId", baId);
          mainIntent.putExtra("group", group);
          Log.d(TAG, "starting new activity with group as  - " + group);
          activity.startActivity(mainIntent);
          activity.finish();
        }
      } else {
        //progressDialog.dismiss();
        Toast.makeText(activity, activity.getResources().getString(R.string.request_fail), Toast.LENGTH_LONG).show();
      }
    }

    @Override
    protected void onProgressUpdate(String... messages) {
      progressDialog.setMessage(messages[0]);
    }

    private boolean handleFormSubmission() {
      // Fill data in request form.
      boolean outerbreak = false; // outerbreak is variable which is used to track file upload failure. In case of file upload fail we cancel form submission.
      for (DisplayGroupAdvanced displayGroupAdvanced : mainForm.getDisplayGroupsAdvanced()) {
        for (Map.Entry<Long, FieldAdvanced> entry : displayGroupAdvanced.getFieldsMap().entrySet()) {
          FieldAdvanced fieldAdvanced = entry.getValue();
          String[] tmpArray = fieldAdvanced.getFieldType().split("\\.");
          String actualType = "";
          if (tmpArray.length > 0) {
            actualType = tmpArray[tmpArray.length - 1];
          }
          if (fieldAdvanced.getFormComponent() != null) {
            View formComponent = fieldAdvanced.getFormComponent();
            if (fieldAdvanced.isFieldHasOptions()) {
              if (formComponent instanceof Spinner) {
                String key = ((Spinner) formComponent).getSelectedItem().toString();
                String value = fieldAdvanced.getFieldOptionsMap().get(key);
                if (value != null) {
                  fieldAdvanced.setFieldValue(value);
                }
              }
            } else if (actualType.equals("BooleanData")) {
              if (formComponent instanceof CheckBox) {
                fieldAdvanced.setFieldValue(String.valueOf(((CheckBox) formComponent).isChecked()));
              }
            } else if (actualType.equals("FileData")) {
              if (!fieldAdvanced.getFiles().isEmpty()) {
                String uuid = uploadFile(fieldAdvanced.getFiles().get(0));
                if (uuid.equals("failed")) {
                  outerbreak = true;
                  publishProgress(activity.getResources().getString(R.string.file_fail));
                  break;
                }
                fieldAdvanced.setFieldValue(uuid);
              }
            } else if (actualType.equals("FileDataList")) {
              if (!fieldAdvanced.getFiles().isEmpty()) {
                boolean wholeBreak = false;
                // we need to clear as in case of retry it will be already populated.
                fieldAdvanced.getFieldValues().clear();
                for (File file : fieldAdvanced.getFiles()) {
                  String uuid = uploadFile(file);
                  if (uuid.equals("failed")) {
                    outerbreak = true;
                    wholeBreak = true;
                    break;
                  }
                  fieldAdvanced.getFieldValues().add(uuid);
                }
                if (wholeBreak) {
                  break;
                }
              }
            } else {
              if (formComponent instanceof EditText) {
                String value = ((EditText) formComponent).getText().toString();
                if (actualType.equals("UserDataList") || actualType.equals("StringDataList") || actualType.equals("RealDataList") || actualType.equals("IntegerDataList")) {
                  String[] tmpVals = value.split(",");
                  List<String> values = new ArrayList<>();
                  for (String tmp : tmpVals) {
                    // If we don't do this this add empty fields in
                    if (!tmp.equals("")) {
                      values.add(tmp);
                    }
                  }
                  fieldAdvanced.setFieldValues(values);
                } else {
                  fieldAdvanced.setFieldValue(value);
                }
              }
            }
          }
        }
        if (outerbreak) {
          break;
        }
      }

      // If file upload has not failed the submit form
      if (!outerbreak) {
        publishProgress("Data submission started");
        try {
          String formUrl = (isUpdate) ? ApiUrlBuilder.submitUpdateActionForm(baId, actionId) : ApiUrlBuilder.submitRequestForm(baId);

          JsonObject jsonObject = Ion
              .with(activity)
              .load(formUrl)
              .setHeader("auth-token", authToken)
              .setTimeout(60 * 60 * 1000)
              .setJsonPojoBody(mainForm)
              .asJsonObject()
              .get();
          publishProgress(jsonObject.get("message").toString());
          if (jsonObject.has("message")) {
            if (jsonObject.get("message").getAsString().equals("Request created")) {
              newActionId = jsonObject.get("actionId").getAsLong();
              return true;
            }
          }
          return false;
        } catch (Exception e) {
          return false;
        }
      }
      return false;
    }

    private String uploadFile(File file) {
      try {
        // We are blocking the ui beacause until file is uploaded we can't get uuid
        // which is needed to submit form.
        publishProgress(activity.getResources().getString(R.string.file_uploading) + file.getName());
        JsonObject jsonObject = Ion
            .with(activity)
            .load(ApiUrlBuilder.uploadFile(baId))
            .setHeader("auth-token", authToken)
            .setTimeout(60 * 60 * 1000)
            .setMultipartFile("file", file)
            .asJsonObject()
            .get();
        if (jsonObject.has("uuid")) {
          publishProgress(activity.getResources().getString(R.string.file_succ));
          return jsonObject.get("uuid").getAsString();
        }
      } catch (Exception e) {
        // pass
      }
      publishProgress("File upload for " + file.getName() + " failed");
      return "failed";
    }
  }
}
