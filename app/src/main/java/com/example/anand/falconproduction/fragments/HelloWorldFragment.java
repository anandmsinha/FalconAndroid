package com.example.anand.falconproduction.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anand.falconproduction.R;

/**
 * Created by anand on 6/2/15.
 *
 * Just for testing purpose.
 */
public class HelloWorldFragment extends Fragment {

  public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
    return layoutInflater.inflate(R.layout.hello_world, parent, false);
  }

}
