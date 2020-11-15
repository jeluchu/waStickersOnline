package com.jeluchu.wastickersonline.core.utils.hawk;

public interface HawkFacade {

  <T> boolean put(String key, T value);

  <T> T get(String key);

  <T> T get(String key, T defaultValue);

  boolean deleteAll();

  boolean delete(String key);

  boolean contains(String key);

  boolean isBuilt();

  class EmptyHawkFacade implements HawkFacade {

    @Override
    public <T> boolean put(String key, T value) {
      throwValidation();
      return false;
    }

    @Override
    public <T> T get(String key) {
      throwValidation();
      return null;
    }

    @Override
    public <T> T get(String key, T defaultValue) {
      throwValidation();
      return null;
    }

    @Override
    public boolean deleteAll() {
      throwValidation();
      return false;
    }

    @Override
    public boolean delete(String key) {
      throwValidation();
      return false;
    }

    @Override
    public boolean contains(String key) {
      throwValidation();
      return false;
    }

    @Override
    public boolean isBuilt() {
      return false;
    }

    private void throwValidation() {
      throw new IllegalStateException("Hawk is not built. " +
          "Please call build() and wait the initialisation finishes.");
    }
  }
}
