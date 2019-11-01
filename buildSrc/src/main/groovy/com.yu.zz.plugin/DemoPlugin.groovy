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

    static String getApiApt(Project project) {
        if (!Judge.isAndroidProject(project)) {
            throw new RuntimeException("not android project ")
        }
        String defaultApt = "annotationProcessor"
        if (Judge.isKotlin(project)) {
            if (!Judge.isHasKapt(project)) {
                println("android project is apply kotlin ,but no has apply kapt")
            }
            return "kapt"
        }
        return "annotationProcessor"
    }
}

class Judge {
    private Judge() {

    }

    public static boolean isAndroidProject(Project project) {
        // 其实还有几个 plugin 是android 项目用的，不常用，这里就没有写，看官自己看需求加就可以了
        return isApp(project) || isLibrary(project)
    }

    public static boolean isApp(Project project) {
        return project.plugins.hasPlugin(AppPlugin)
    }

    public static boolean isLibrary(Project project) {
        return project.plugins.hasPlugin(LibraryPlugin)
    }

    public static boolean isKotlin(Project project) {
        return project.plugins.hasPlugin(KotlinAndroidPluginWrapper)
    }

    public static boolean isHasKapt(Project project) {
        return project.plugins.hasPlugin(Kapt3GradleSubplugin)
    }
}


class ProjectUtils {
    private ProjectUtils() {

    }

}

/**
 * 这个类没有什么用，只是为了toString 方便
 */
class ProjectBean {
    // 是否 Application
    boolean isApp
    // 是否 为android Library
    boolean isLibrary
    // 是否支持 kotlin
    boolean isKotlin
    // 是否 已支持apt
    boolean isKapt

    @Override
    public String toString() {
        return "ProjectBean{" +
                "isApp=" + isApp +
                ", isLibrary=" + isLibrary +
                ", isKotlin=" + isKotlin +
                ", isKapt=" + isKapt +
                '}'
    }
}