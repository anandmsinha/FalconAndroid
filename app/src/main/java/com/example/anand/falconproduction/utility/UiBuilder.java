package com.example.anand.falconproduction.utility;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by anand on 1/12/14.
 */
public class UiBuilder {

  public static TextView getTextView(Activity activity, String text) {
    TextView tmpTextView = new TextView(activity);
    tmpTextView.setText(text);
    tmpTextView.setTextSize(14);
    return tmpTextView;
  }

  public static TextView createBoldTextView(Activity activity, String text) {
    TextView tmpTextView = getTextView(activity, text);
    tmpTextView.setTextSize(16);
    tmpTextView.setTypeface(null, Typeface.BOLD);
    return tmpTextView;
  }

}
