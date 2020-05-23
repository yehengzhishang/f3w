package com.yu.zz.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ApiPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply start ")
        String apt = ProjectUtils.getApiApt(project)
        println(apt)
        bean(project)
        println("apply end")
    }

    static ProjectBean bean(Project project) {
        ProjectBean bean = new ProjectBean()
        bean.isApp = Judge.isApp project
        bean.isLibrary = Judge.isLibrary project
        bean.isKotlin = Judge.isKotlin project
        bean.isKapt = Judge.isHasKapt project
        println(bean.toString())
        return bean
    }

}

class Judge {
    private Judge() {
        throw new RuntimeException("cant init Judge")
    }

    public static boolean isAndroidProject(Project project) {
        // 尝试性api，看注释是这样用的
        return project.plugins.hasPlugin(AndroidBasePlugin)
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
        throw new RuntimeException("cant init Judge")
    }

    /**
     * 根据 {@link Project#getPlugins}判断应该用哪一个方法
     * apt相关plugin 从我已知，大概经历了三个阶段
     *   android-apt：民间提供，不再维护，也不本方法考虑的范围之内
     *   annotationProcessor：android-apt 官方版本
     *   kapt : annotationProcessor 的 kotlin 版本，坑挺多，更新中
     * @param project 项目不解释
     * @return kapt or annotationProcessor
     */
    static String getApiApt(Project project) {
        if (!Judge.isAndroidProject(project)) {
            throw new RuntimeException("not android project ")
        }
        String targetApt = "annotationProcessor"
        if (Judge.isKotlin(project)) {
            if (!Judge.isHasKapt(project)) {
                println("android project is apply kotlin ,but no has apply kapt")
                applyPlugin project, "kotlin-kapt"

            }
            targetApt = "kapt"
        }
        return targetApt
    }

    /**
     * eg. apply plugin: 'com.android.library'
     */
    static void applyPlugin(Project project, String pluginId) {
        project.apply {
            plugin pluginId
        }
        // 新版本推荐 plugins, 当前版本不支持写在脚本里。新版本有可能会解禁
//        project.plugins {
//            id pluginId
//        }
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