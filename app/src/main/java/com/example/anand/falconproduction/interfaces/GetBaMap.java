package com.example.anand.falconproduction.interfaces;

import com.example.anand.falconproduction.models.BaGroups;

import java.util.List;

/**
 * Created by anand on 28/11/14.
 *
 * This interface is to recieve baGroups list and process it.
 */
public interface GetBaMap {

  void processBaMap(List<BaGroups> groups);

}
