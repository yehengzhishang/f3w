package com.yu.zz.plugin;

import org.gradle.api.NonNullApi;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

@NonNullApi
public class LibraryPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("rainrain Library ");
    }
}
