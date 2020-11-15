package com.jeluchu.wastickersonline.core.utils.hawk;

public interface Serializer {

  <T> String serialize(String cipherText, T value);
  DataInfo deserialize(String plainText);

}