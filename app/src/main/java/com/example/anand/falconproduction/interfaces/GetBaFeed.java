package com.example.anand.falconproduction.interfaces;

import com.example.anand.falconproduction.models.BaFeed;

import java.util.List;

/**
 * Created by anand on 28/11/14.
 *
 * This interface is to process fetched ba feed.
 */
public interface GetBaFeed {

  void processBaFeed(long baId, List<BaFeed> list);

}
