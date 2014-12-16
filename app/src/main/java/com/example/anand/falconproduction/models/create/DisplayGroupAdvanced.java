package com.example.anand.falconproduction.models.create;

/**
 * Created by anand on 16/12/14.
 *
 * This is a advanced display group and is used in case of create request activity.
 * Reason for having two diffrent types of DisplayGroup classes is that in case of
 * view request we need fewer fields compared to create request so in order to save network
 * transfer bandwidth we have created two diffrent classes.
 */
public class DisplayGroupAdvanced {

  String displayGroupName;

}
