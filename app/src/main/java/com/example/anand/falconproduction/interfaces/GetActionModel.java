package com.example.anand.falconproduction.interfaces;

import com.example.anand.falconproduction.models.ActionModel;

/**
 * Created by anand on 9/2/15.
 *
 * Method to be executed after action fetch
 */
public interface GetActionModel {
  void processActionModel(ActionModel actionModel);
  void onActionModelFetchError(boolean networkError);
}
