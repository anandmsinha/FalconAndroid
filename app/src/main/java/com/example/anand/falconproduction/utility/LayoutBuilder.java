package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.anand.falconproduction.R;

/**
 * Created by anand on 1/12/14.
 *
 * This is helper class for building layouts.
 */
public class LayoutBuilder {

  public static LinearLayout getStandardFalconLayout(Activity activity) {
    LinearLayout linearLayout = new LinearLayout(activity);
    linearLayout.setOrientation(LinearLayout.VERTICAL);
    linearLayout.setPadding(10, 10, 10, 10);
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.setMargins(8, 8, 8, 8);
    linearLayout.setLayoutParams(params);
    linearLayout.setBackgroundColor(activity.getResources().getColor(R.color.white));
    return linearLayout;
  }

}
