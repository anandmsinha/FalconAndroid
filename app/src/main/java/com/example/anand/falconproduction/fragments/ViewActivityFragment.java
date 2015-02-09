package com.example.anand.falconproduction.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anand.falconproduction.R;
import com.example.anand.falconproduction.interfaces.GetActionModel;
import com.example.anand.falconproduction.models.ActionModel;
import com.example.anand.falconproduction.utility.CommonRequestsUtility;
import com.example.anand.falconproduction.utility.SearchQueryBuilder;
import com.example.anand.falconproduction.utility.UiBuilder;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by anand on 5/2/15.
 * <p/>
 * Main view activity fragment.
 */
public class ViewActivityFragment extends Fragment {

  private static final String TAG = ViewActivityFragment.class.getName();

  private boolean fetchAction = true;
  private ActionModel actionModel;

  private long actionId;
  private long baId;
  private String authToken;

  public static ViewActivityFragment init(long baId, long actionId, String authToken) {
    Log.d(TAG, "init called.");
    ViewActivityFragment viewActivityFragment = new ViewActivityFragment();
    Bundle args = new Bundle();
    args.putLong("baId", baId);
    args.putLong("actionId", actionId);
    args.putString("authToken", authToken);
    viewActivityFragment.setArguments(args);
    return viewActivityFragment;
  }

  public static ViewActivityFragment init(long baId, String authToken, ActionModel actionModel) {
    ViewActivityFragment viewActivityFragment = init(baId, actionModel.getActionId(), authToken);
    viewActivityFragment.setFetchAction(false);
    viewActivityFragment.setActionModel(actionModel);
    return viewActivityFragment;
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      actionId = args.getLong("actionId");
      baId = args.getLong("baId");
      authToken = args.getString("authToken");
    }
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
    Log.d(TAG, "On create view called.");
    final View tmpView = inflater.inflate(R.layout.view_activity_fragments, parent, false);
    final LinearLayout view = (LinearLayout) tmpView.findViewById(R.id.view_activity_fragment_root);
    final Button updateActionButton = (Button) view.findViewById(R.id.update_action_button);
    final ProgressDialog progressDialog = UiBuilder.createProgressDialog(getActivity(), "Loading...", "");
    progressDialog.show();
    Log.d(TAG, "Progress dialog done.");
    if (fetchAction) {
      CommonRequestsUtility.fetchActionModel(getActivity(), authToken, baId, actionId, new GetActionModel() {
        @Override
        public void processActionModel(ActionModel actionModel) {
          ViewActivityFragment.this.actionModel = actionModel;
          ViewActivityFragment.this.fetchAction = false;
          UiBuilder.buildViewActivityUi(getActivity(), ViewActivityFragment.this.actionModel, updateActionButton, view, progressDialog);
        }

        @Override
        public void onActionModelFetchError(boolean networkError) {
          int resId = (networkError) ? R.string.fetch_error : R.string.permission_view;
          progressDialog.dismiss();
          Toast.makeText(getActivity(), getResources().getString(resId), Toast.LENGTH_LONG).show();
          buildErrorUi(!networkError, view);
        }
      });
    } else {
      UiBuilder.buildViewActivityUi(getActivity(), this.actionModel, updateActionButton, view, progressDialog);
    }

    return tmpView;
  }

  /**
   * Show action fetch error in ui
   * @param permissionError - permission error or network error.
   * @param view - Layout
   */
  private void buildErrorUi(boolean permissionError, LinearLayout view) {
    Log.d(TAG, "buildErrorUi called");
    int resId = (permissionError) ? R.string.permission_view : R.string.fetch_error;
    UiBuilder.fillLayoutWithMessage(getActivity(), view, getResources().getString(resId));
  }

  public void setActionModel(ActionModel actionModel) {
    this.actionModel = actionModel;
  }

  public void setFetchAction(boolean fetchAction) {
    this.fetchAction = fetchAction;
  }
}
