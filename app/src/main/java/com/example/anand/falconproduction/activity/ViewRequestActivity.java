package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.models.view.DisplayGroup;
import com.example.anand.falconproduction.utility.LayoutBuilder;
import com.example.anand.falconproduction.utility.SearchQueryBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by anand on 1/12/14.
 * <p/>
 * Main ba feed adapter.
 */
public class ViewRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  private static final String tag = ViewRequestActivity.class.getName();
  long baId;
  long actionId;
  Button updateActionBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(tag, "onCreate called");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_request_activity, this);

    findViewById(R.id.back_to_ba_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(ViewRequestActivity.this, MainActivity.class));
      }
    });

    updateActionBtn = (Button) findViewById(R.id.update_action_button);
    updateActionBtn.setVisibility(View.INVISIBLE);
    updateActionBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent finalIntent = new Intent(ViewRequestActivity.this, EditActionActivity.class);
        finalIntent.putExtra("actionId", actionId);
        startActivity(finalIntent);
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
    Log.d(tag, "startActivity called");
    intent.putExtra("baId", baId);
    intent.putExtra("group", groupId);
    super.startActivity(intent);
  }

  @Override
  public void fillDetails(int groupCount) {
    Log.d(tag, "fillDetails called");
    baId = intentBundle.getLong("baId");
    actionId = intentBundle.getLong("actionId");
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle("Loading....");
    progressDialog.setIndeterminate(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();
    Ion.with(this)
        .load(SearchQueryBuilder.getAnAction(baId, actionId))
        .setHeader("auth-token", authToken)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (e != null || !result.has("businessArea")) {
              if (e != null) {
                buildErrorUi(false);
                Log.e(tag, e.getMessage());
              } else {
                buildErrorUi(true);
                Log.e(tag, result.toString());
              }
              progressDialog.dismiss();
              Toast.makeText(ViewRequestActivity.this, "Unable to fetch action right now.",
                  Toast.LENGTH_LONG).show();
              return;
            }

            ActionModel actionModel = new ActionModel(result);
            buildUi(actionModel, progressDialog);
          }
        });
  }

  private void buildErrorUi(boolean permissionError) {
    Log.d(tag, "buildErrorUi called");
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_action_view_content);
    int resId = (permissionError) ? R.string.permission_view : R.string.fetch_error;
    UiBuilder.fillLayoutWithMessage(this, mainLayout, this.getResources().getString(resId));
  }

  private void buildUi(ActionModel actionModel, ProgressDialog progressDialog) {
    Log.d(tag, "buildUi called");
    if (actionModel.isCanUpdateAction()) {
      updateActionBtn.setVisibility(View.VISIBLE);
    }
    LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_action_view_content);
    TextView mainActionTitle = (TextView) findViewById(R.id.main_action_view_title);
    mainActionTitle.setText(actionModel.getDisplayTitle());
    Log.d(tag, "Size of display groups " + actionModel.getDisplayGroups().size());
    for (DisplayGroup displayGroup : actionModel.getDisplayGroups()) {
      LinearLayout linearLayout = LayoutBuilder.getStandardFalconLayout(this);
      TextView dispHeading = UiBuilder.createBoldTextView(this, displayGroup.getTitle());
      linearLayout.addView(dispHeading);
      TextView otherDetails = new TextView(this);
      otherDetails.setText(Html.fromHtml(displayGroup.getFieldsHtml()));
      otherDetails.setMovementMethod(LinkMovementMethod.getInstance());
      linearLayout.addView(otherDetails);
      mainLayout.addView(linearLayout);
    }
    progressDialog.dismiss();
  }

  @Override
  public long getBaId() {
    return baId;
  }
}
