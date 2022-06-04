package com.yu.zz.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdLibParam {
    private static final String TAG = "ThirdLibParam";

    static public final String VERSION_RETROFIT = "2.9.0";
    static public final String VERSION_OKHTTP = "4.8.0";

    static public final String RETROFIT = "com.squareup.retrofit2:retrofit:" + VERSION_RETROFIT;
    static public final String RETROFIT_ADAPTER = "com.squareup.retrofit2:adapter-rxjava2:" + VERSION_RETROFIT;
    static public final String RETROFIT_CONVERTER = "com.squareup.retrofit2:converter-gson:" + VERSION_RETROFIT;

    static public final String OKHTTP = "com.squareup.okhttp3:okhttp:" + VERSION_OKHTTP;
    static public final String OKHTTP_LOG = "com.squareup.okhttp3:logging-interceptor:" + VERSION_OKHTTP;
    static public final List<String> NET_LIB = new ArrayList<>() {{
        add(RETROFIT);
        add(RETROFIT_ADAPTER);
        add(RETROFIT_CONVERTER);
        add(OKHTTP);
        add(OKHTTP_LOG);
    }};

    static public final String VERSION_RX = "2.2.21";
    static public final String VERSION_RX_ANDROID = "2.1.1";

    static public final String RX = "io.reactivex.rxjava2:rxjava:" + VERSION_RX;
    static public final String RX_ANDROID = "io.reactivex.rxjava2:rxandroid:" + VERSION_RX_ANDROID;

    static public final List<String> RX_LIB = new ArrayList<>() {{
        add(RX);
        add(RX_ANDROID);
    }};

    static public final String VERSION_GLIDE = "4.13.2";
    static public final String GLIDE = "com.github.bumptech.glide:glide:" + VERSION_GLIDE;
    static public final String IMAGE_LIB = GLIDE;

    static public final String VERSION_GIF = "1.2.24";
    static public final String GIF = "pl.droidsonroids.gif:android-gif-drawable:" + VERSION_GIF;
    static public final String GIF_LIB = GIF;


    public static final String VERSION_LOTTIE = "5.1.1";

    static public final String LOTTIE_LIB = "com.airbnb.android:lottie:" + VERSION_LOTTIE;

    static public final String VERSION_DAGGER = "2.42";
    static public final Map<String, String> DAGGER_MAP = new HashMap<>() {{
        put("dagger", "com.google.dagger:dagger:" + VERSION_DAGGER);
        put("dagger_android", "com.google.dagger:dagger-android:" + VERSION_DAGGER);
        put("dagger_support", "com.google.dagger:dagger-android-support:" + VERSION_DAGGER);
    }};
    static public final Collection<String> DAGGER_LIB = DAGGER_MAP.values();

    static public final Map<String, String> DAGGER_KAPT_MAP = new HashMap<>() {{
        put("dagger-android-processor", "com.google.dagger:dagger-android-processor:" + VERSION_DAGGER);
        put("dagger-compiler", "com.google.dagger:dagger-compiler:" + VERSION_DAGGER);
    }};

    static public final Collection<String> DAGGER_KAPT_LIB = DAGGER_KAPT_MAP.values();

    private ThirdLibParam() {

    }
}
