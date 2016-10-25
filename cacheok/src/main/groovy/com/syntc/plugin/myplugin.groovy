package com.syntc.plugin

import com.syntc.bean.DateAndTimePluginExtension;

// 注意com.abc.xiaoming.properties  这个文件的命名，你可以随意取名(和包名无关)，但是后面使用这个插件的时候(其他module)，会用到这个名字。比如，你取名为com.abc.xiaoming.properties ，而在其他build.gradle文件中使用自定义的插件时候则需写成：
// apply plugin: 'com.abc.xiaoming'


import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

            project.extensions.create("dateAndTime", DateAndTimePluginExtension)


            System.out.println("========================");
            System.out.println("hello gradle plugin!");
            System.out.println("========================");


        //创建两个Task

            project.task('ashowTime') << {
                println "Current time is " + new Date().format(project.dateAndTime.timeFormat)
            }

            project.tasks.create('ashowDate') << {
                println "Current date is " + new Date().format(project.dateAndTime.dateFormat)
            }






    }
}






