package com.yu.zz.help;

import org.gradle.api.Project;

public class CommonHelper {

    public static void importCommon(Project project) {
        project.getDependencies().add("implementation", project.project(":common"));
    }
}
