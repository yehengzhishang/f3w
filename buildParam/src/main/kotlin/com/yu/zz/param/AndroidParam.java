package com.yu.zz.param;

import com.android.build.gradle.api.AndroidBasePlugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AndroidParam implements Plugin<Project> {
    public static final int MIN_SDK = 21;

    @Override
    public void apply(Project project) {
        boolean isAndroid = project.getPlugins().hasPlugin(AndroidBasePlugin.class);
        System.out.println("rainrain isAndroidProject = " + isAndroid);
    }
}
