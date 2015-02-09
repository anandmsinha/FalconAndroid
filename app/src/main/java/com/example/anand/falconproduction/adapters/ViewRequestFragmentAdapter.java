package com.example.anand.falconproduction.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anand.falconproduction.fragments.ViewActivityFragment;
import com.example.anand.falconproduction.models.ActionModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by anand on 5/2/15.
 * <p/>
 * This class represets Main request view adapter.
 */
public class ViewRequestFragmentAdapter extends FragmentPagerAdapter {

  long actionId;
  long baId;
  String authToken;
  ActionModel baseActionModel;
  private List<Long> actionIds;

  public ViewRequestFragmentAdapter(FragmentManager fm) {
    super(fm);
  }

  /**
   * We are passsing actionmodel instance of first action which has to be seen
   * through this we populate list of other actions in the request.
   *
   * @param fm          - Fragment manager
   * @param authToken   - Token
   * @param actionModel - Default model
   */
  public ViewRequestFragmentAdapter(FragmentManager fm, String authToken, ActionModel actionModel) {
    this(fm);
    this.baseActionModel = actionModel;
    this.actionId = this.baseActionModel.getActionId();
    this.baId = this.baseActionModel.getBusinessArea().getBaId();
    this.authToken = authToken;
    this.baseActionModel.getPermissibleActionIds().add(this.actionId);
    Collections.sort(this.baseActionModel.getPermissibleActionIds());
    this.actionIds = this.baseActionModel.getPermissibleActionIds();
  }

  public List<Long> getActionIds() {
    return actionIds;
  }

  @Override
  public Fragment getItem(int position) {
    if (actionIds.get(position) == actionId) {
      return ViewActivityFragment.init(baId, authToken, baseActionModel);
    } else {
      return ViewActivityFragment.init(baId, actionIds.get(position), authToken);
    }
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return String.format("%d_%d", baseActionModel.getRequestDisplayId(), position + 1);
  }

  @Override
  public int getCount() {
    return actionIds.size();
  }
}
