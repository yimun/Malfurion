package me.yimu.malfurion

import org.gradle.api.Plugin
import org.gradle.api.Project

public class MainPlugin implements Plugin<Project> {

    void apply(Project project) {
        println("===================")
        println("hello, malfurion")
        println("===================")
    }
}