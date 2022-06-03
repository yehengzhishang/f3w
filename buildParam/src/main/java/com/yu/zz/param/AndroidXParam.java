package com.yu.zz.param;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AndroidXParam {
    static public final String VERSION_APPCOMPAT = "1.4.0";
    static public final String VERSION_MATERIAL = "1.4.0";
    static public final String VERSION_CONSTRAINT_LAYOUT = "2.1.3";
    static public final String VERSION_LIFECYCLE = "2.4.1";
    static public final String VERSION_LIFECYCLE_COMMON = "2.3.1";
    static public final String VERSION_VIEWPAGER2 = "1.0.0";
    static public final String VERSION_SWIPE_REFRESH_LAYOUT = "1.1.0";
    static public final String VERSION_ROOM = "2.4.2";
    static public final String VERSION_RECYCLERVIEW = "1.2.1";
    static public final String VERSION_RECYCLERVIEW_SELECTION = "1.1.0";

    static public final String APPCOMPAT = "androidx.appcompat:appcompat:" + AndroidXParam.VERSION_APPCOMPAT;
    static public final String MATERIAL = "com.google.android.material:material:" + AndroidXParam.VERSION_MATERIAL;

    static public final Map<String, String> ANDROIDX_MAP = new HashMap<>() {{
        put("APPCOMPAT", APPCOMPAT);
        put("MATERIAL", MATERIAL);
    }};

    static public final Collection<String> ANDROID_LIB = ANDROIDX_MAP.values();

    private AndroidXParam() {
    }
}
