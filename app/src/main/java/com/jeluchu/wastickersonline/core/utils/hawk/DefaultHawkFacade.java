package com.jeluchu.wastickersonline.core.utils.hawk;

public class DefaultHawkFacade implements HawkFacade {

  private final Storage storage;
  private final Converter converter;
  private final Serializer serializer;
  private final LogInterceptor logInterceptor;

  public DefaultHawkFacade(HawkBuilder builder) {
    storage = builder.getStorage();
    converter = builder.getConverter();
    serializer = builder.getSerializer();
    logInterceptor = builder.getLogInterceptor();
  }

  @Override
  public <T> boolean put(String key, T value) {

    HawkUtils.checkNull("Key", key);
    log("Hawk.put -> key: " + key + ", value: " + value);

    if (value == null) {
      log("Hawk.put -> Value is null. Any existing value will be deleted with the given key");
      return delete(key);
    }

    String plainText = converter.toString(value);
    log("Hawk.put -> Converted to " + plainText);
    if (plainText == null) {
      log("Hawk.put -> Converter failed");
      return false;
    }

    String serializedText = serializer.serialize(plainText, value);
    log("Hawk.put -> Serialized to " + serializedText);
    if (serializedText == null) {
      log("Hawk.put -> Serialization failed");
      return false;
    }

    if (storage.put(key, serializedText)) {
      log("Hawk.put -> Stored successfully");
      return true;
    } else {
      log("Hawk.put -> Store operation failed");
      return false;
    }
  }

  @Override
  public <T> T get(String key) {
    log("Hawk.get -> key: " + key);
    if (key == null) {
      log("Hawk.get -> null key, returning null value ");
      return null;
    }

    String serializedText = storage.get(key);
    log("Hawk.get -> Fetched from storage : " + serializedText);
    if (serializedText == null) {
      log("Hawk.get -> Fetching from storage failed");
      return null;
    }

    DataInfo dataInfo = serializer.deserialize(serializedText);
    log("Hawk.get -> Deserialized");
    if (dataInfo == null) {
      log("Hawk.get -> Deserialization failed");
      return null;
    }

    T result = null;
    try {
      result = converter.fromString(dataInfo.cipherText, dataInfo);
      log("Hawk.get -> Converted to : " + result);
    } catch (Exception e) {
      log("Hawk.get -> Converter failed");
    }

    return result;
  }

  @Override
  public <T> T get(String key, T defaultValue) {
    T t = get(key);
    if (t == null) return defaultValue;
    return t;
  }

  @Override
  public boolean deleteAll() {
    return storage.deleteAll();
  }

  @Override
  public boolean delete(String key) {
    return storage.delete(key);
  }

  @Override
  public boolean contains(String key) {
    return storage.contains(key);
  }

  @Override
  public boolean isBuilt() {
    return true;
  }

  private void log(String message) {
    logInterceptor.onLog(message);
  }
}
