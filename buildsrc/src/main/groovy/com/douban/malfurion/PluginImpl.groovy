package com.douban.malfurion

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginImpl implements Plugin<Project> {

    void apply(Project project) {
        println("=====================")
        println("Hello, Malfurion")
        println("=====================")

        def isApp = project.plugins.hasPlugin(AppPlugin)
        if (isApp) {
            def android = project.extensions.getByType(AppExtension)
            def transform = new TransformImpl(project)
            android.registerTransform(transform)
        }
    }
}