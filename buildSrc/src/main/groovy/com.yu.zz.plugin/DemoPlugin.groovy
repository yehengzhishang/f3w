package com.yu.zz.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("apply start ")

        println("apply end")
    }
}