package com.yu.zz.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class DemoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply start ")
        ProjectBean bean = new ProjectBean()
        bean.isApp = project.plugins.hasPlugin(AppPlugin)
        bean.isLibrary = project.plugins.hasPlugin(LibraryPlugin)
        bean.isKotlin = project.plugins.hasPlugin(KotlinAndroidPluginWrapper)
        println(bean.toString())
        println("apply end")
    }
}

class ProjectBean {
    // 是否 Application
    boolean isApp
    // 是否 为android Library
    boolean isLibrary
    // 是否支持 kotlin
    boolean isKotlin

    @Override
    String toString() {
        return "ProjectBean{" +
                "isApp=" + isApp +
                ", isLibrary=" + isLibrary +
                ", isKotlin=" + isKotlin +
                '}'
    }
}