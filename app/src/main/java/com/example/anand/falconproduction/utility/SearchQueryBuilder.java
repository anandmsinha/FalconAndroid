package com.example.anand.falconproduction.utility;

/**
 * Created by anand on 1/12/14.
 *
 * This class helps in generating search query for diffrent types of tasks.
 */
public class SearchQueryBuilder {

  private static final String searchBase = ApplicationConstants.baseAppUrl +
    "actions/search";

  /**
   * Generate search query for getting an action from database.
   *
   * @param baId - business area id
   * @param actionId - action id
   * @return - url
   */
  public static String getAnAction(long baId, long actionId) {
    return getBaseClientAndUserIds() + "&baId=" + baId
        + "&search=r__" + actionId;
  }

  /**
   * Right now clientId and baId are hardcoded later on they will be
   * extracted from sharedpreferences.
   * @return - base url
   */
  private static String getBaseClientAndUserIds() {
    return searchBase + "?clientId=" + 1 + "&userId=" + 1;
  }
}
