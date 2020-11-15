package com.jeluchu.wastickersonline.core.utils.hawk;

public interface Storage {

  <T> boolean put(String key, T value);
  <T> T get(String key);

  boolean delete(String key);
  boolean deleteAll();
  boolean contains(String key);

}
