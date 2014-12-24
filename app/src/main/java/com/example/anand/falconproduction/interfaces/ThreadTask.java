package com.example.anand.falconproduction.interfaces;

/**
 * Created by anand on 22/12/14.
 *
 * This is the interface used for passing to thread it has two methods pass and fail
 * which are executed depending on task fails or passes
 */
public interface ThreadTask {

  void pass();

  void fail();

}
