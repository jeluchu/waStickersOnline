package com.jeluchu.wastickersonline.core.utils.hawk;

import android.content.Context;

public final class Hawk {

  private Hawk() { }

  static HawkFacade hawkFacade = new HawkFacade.EmptyHawkFacade();

  public static HawkBuilder init(Context context) {
    HawkUtils.checkNull("Context", context);
    hawkFacade = null;
    return new HawkBuilder(context);
  }

  static void build(HawkBuilder hawkBuilder) {
    hawkFacade = new DefaultHawkFacade(hawkBuilder);
  }

  public static <T> boolean put(String key, T value) {
    return hawkFacade.put(key, value);
  }

  public static <T> T get(String key) {
    return hawkFacade.get(key);
  }

  public static <T> T get(String key, T defaultValue) {
    return hawkFacade.get(key, defaultValue);
  }

  public static boolean deleteAll() {
    return hawkFacade.deleteAll();
  }

  public static boolean delete(String key) {
    return hawkFacade.delete(key);
  }

  public static boolean contains(String key) {
    return hawkFacade.contains(key);
  }

  public static boolean isBuilt() {
    return hawkFacade.isBuilt();
  }

}
