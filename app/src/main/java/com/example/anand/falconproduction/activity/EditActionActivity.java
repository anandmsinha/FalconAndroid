package com.example.anand.falconproduction.activity;

import android.os.Bundle;
import android.util.Log;

/**
 * Created by anand on 20/1/15.
 *
 * Activity for editing action
 */
public class EditActionActivity extends BaseDrawerActivity {

  private static final String TAG = EditActionActivity.class.getName();

  long baId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(TAG, "onCreate called");
    super.onCreate(savedInstanceState);
  }

  public long getBaId() {
    return baId;
  }
}
