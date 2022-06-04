package com.yu.zz.param;

import java.util.ArrayList;
import java.util.List;

public class KotlinParam {

    static public final String VERSION = "1.6.10";
    static public final String VERSION_KTX = "1.7.0";

    static public final String KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:" + VERSION;
    static public final String KTX = "androidx.core:core-ktx:" + VERSION_KTX;

    static public final List<String> KOTLIN_LIB = new ArrayList<>() {{
        add(KOTLIN);
        add(KTX);
    }};

    private KotlinParam() {
    }
}
