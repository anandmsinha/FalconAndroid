package com.example.anand.falconproduction.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.ProcessAfterDrawer;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.models.DisplayGroup;
import com.example.anand.falconproduction.utility.LayoutBuilder;
import com.example.anand.falconproduction.utility.SearchQueryBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by anand on 1/12/14.
 *
 * Main ba feed adapter.
 */
public class ViewRequestActivity extends BaseDrawerActivity implements ProcessAfterDrawer {

  private static final String tag = "ViewRequestActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_request_activity, this);
  }

  @Override
  public void fillDetails(int groupCount) {
    long baId = intentBundle.getLong("baId");
    long actionId = intentBundle.getLong("actionId");
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
                Log.d("viewa", e.getMessage());
              } else {
                Log.d("viewa", result.toString());
              }
              progressDialog.dismiss();
              Toast.makeText(getApplicationContext(), "Unable to fetch action right now.",
                  Toast.LENGTH_LONG).show();
              return;
            }

            ActionModel actionModel = new ActionModel(result);
            buildUi(actionModel, progressDialog);
          }
        });
  }

  private void buildUi(ActionModel actionModel, ProgressDialog progressDialog) {
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
}
