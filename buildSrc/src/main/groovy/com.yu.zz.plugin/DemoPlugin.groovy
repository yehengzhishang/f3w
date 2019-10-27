package com.yu.zz.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class DemoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply start ")
        bean(project)

        println("apply end")
    }

    static ProjectBean bean(Project project) {
        ProjectBean bean = new ProjectBean()
        bean.isApp = project.plugins.hasPlugin(AppPlugin)
        bean.isLibrary = project.plugins.hasPlugin(LibraryPlugin)
        bean.isKotlin = project.plugins.hasPlugin(KotlinAndroidPluginWrapper)
        bean.isKapt = project.plugins.hasPlugin(Kapt3GradleSubplugin)
        println(bean.toString())
        return bean
    }

    static String getApiApt(boolean isAndroid, boolean isKotlin) {
        if (!isAndroid) {
            throw new RuntimeException("not android project ")
        }

        if (isKotlin) {

        }
        return "annotationProcessor"
    }
}

class ProjectUtils {
    private ProjectUtils() {

    }

}

class ProjectBean {
    // 是否 Application
    boolean isApp
    // 是否 为android Library
    boolean isLibrary
    // 是否支持 kotlin
    boolean isKotlin

    // 是否 已支持apt
    boolean isKapt

    boolean isAndroid() {
        return isApp || isLibrary
    }

    @Override
    String toString() {
        return "ProjectBean{" +
                "isApp=" + isApp +
                ", isLibrary=" + isLibrary +
                ", isKotlin=" + isKotlin +
                '}'
    }
}