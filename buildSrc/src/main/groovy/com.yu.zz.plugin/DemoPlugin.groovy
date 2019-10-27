package com.yu.zz.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply start ")
        ProjectBean bean = new ProjectBean()
        bean.isApp = project.plugins.hasPlugin(AppPlugin)
        bean.isLibrary = project.plugins.hasPlugin(LibraryPlugin)
        
        println(bean.toString())
        println("apply end")
    }
}

class ProjectBean {
    // 是否 Application
    boolean isApp
    // 是否 为android Library
    boolean isLibrary

    @Override
    String toString() {
        return "ProjectBean{" +
                "isApp=" + isApp +
                ", isLibrary=" + isLibrary +
                '}'
    }
}