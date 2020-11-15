package com.jeluchu.wastickersonline.core.utils.hawk;

import android.content.Context;

import com.google.gson.Gson;

public class HawkBuilder {

  private static final String STORAGE_TAG_DO_NOT_CHANGE = "Hawk2";

  private final Context context;
  private Storage cryptoStorage;
  private Converter converter;
  private Parser parser;
  private Serializer serializer;
  private LogInterceptor logInterceptor;

  public HawkBuilder(Context context) {
    HawkUtils.checkNull("Context", context);

    this.context = context.getApplicationContext();
  }

  LogInterceptor getLogInterceptor() {
    if (logInterceptor == null) {
      logInterceptor = message -> { };
    }
    return logInterceptor;
  }

  Storage getStorage() {
    if (cryptoStorage == null) {
      cryptoStorage = new SharedPreferencesStorage(context, STORAGE_TAG_DO_NOT_CHANGE);
    }
    return cryptoStorage;
  }

  Converter getConverter() {
    if (converter == null) {
      converter = new HawkConverter(getParser());
    }
    return converter;
  }

  Parser getParser() {
    if (parser == null) {
      parser = new GsonParser(new Gson());
    }
    return parser;
  }


  Serializer getSerializer() {
    if (serializer == null) {
      serializer = new HawkSerializer(getLogInterceptor());
    }
    return serializer;
  }

  public void build() {
    Hawk.build(this);
  }
}
