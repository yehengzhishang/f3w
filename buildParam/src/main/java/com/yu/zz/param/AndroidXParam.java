package com.yu.zz.param;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AndroidXParam {
    static public final String VERSION_APPCOMPAT = "1.4.1";
    static public final String VERSION_MATERIAL = "1.4.0";
    static public final String VERSION_CONSTRAINT_LAYOUT = "2.1.3";
    static public final String VERSION_LIFECYCLE = "2.4.1";
    static public final String VERSION_LIFECYCLE_COMMON = "2.3.1";
    static public final String VERSION_VIEWPAGER2 = "1.0.0";
    static public final String VERSION_SWIPE_REFRESH_LAYOUT = "1.1.0";
    static public final String VERSION_RECYCLERVIEW = "1.2.1";

    //
    static public final String VERSION_RECYCLERVIEW_SELECTION = "1.1.0";
    static public final String VERSION_ROOM = "2.4.2";


    static public final String APPCOMPAT = "androidx.appcompat:appcompat:" + AndroidXParam.VERSION_APPCOMPAT;
    static public final String MATERIAL = "com.google.android.material:material:" + AndroidXParam.VERSION_MATERIAL;
    static public final String CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:" + VERSION_CONSTRAINT_LAYOUT;
    static public final String LIVEDATA = "androidx.lifecycle:lifecycle-livedata:" + AndroidXParam.VERSION_LIFECYCLE;
    static public final String VIEW_MODEL = "androidx.lifecycle:lifecycle-viewmodel:" + AndroidXParam.VERSION_LIFECYCLE;
    static public final String LIFECYCLE_COMMON = "androidx.lifecycle:lifecycle-common:" + AndroidXParam.VERSION_LIFECYCLE_COMMON;
    static public final String VIEWPAGER2 = "androidx.viewpager2:viewpager2:" + AndroidXParam.VERSION_VIEWPAGER2;
    static public final String SWIPE_REFRESH_LAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:" + AndroidXParam.VERSION_SWIPE_REFRESH_LAYOUT;
    static public final String RECYCLERVIEW = "androidx.recyclerview:recyclerview:" + AndroidXParam.VERSION_RECYCLERVIEW;

    static public final Map<String, String> ANDROIDX_MAP = new HashMap<>() {{
        put("APPCOMPAT", APPCOMPAT);
        put("MATERIAL", MATERIAL);
        put("CONSTRAINT_LAYOUT", CONSTRAINT_LAYOUT);
        put("LIVEDATA", LIVEDATA);
        put("VIEW_MODEL", VIEW_MODEL);
        put("LIFECYCLE_COMMON", LIFECYCLE_COMMON);
        put("VIEWPAGER2", VIEWPAGER2);
        put("SWIPE_REFRESH_LAYOUT", SWIPE_REFRESH_LAYOUT);
        put("RECYCLERVIEW", RECYCLERVIEW);
    }};

    static public final Collection<String> ANDROID_LIB = ANDROIDX_MAP.values();

    static public final String ROOM = "2.4.2";

    private AndroidXParam() {
    }
}
