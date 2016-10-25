## how to use 

只需要在其他的Module中引用 即可 
在module的 build.gradle中

```
apply plugin: 'com.abc.xiaoming'
//  单个 module中没有 buildscript，需我们自己添加
buildscript {
    repositories {
        maven {//本地Maven仓库地址
            url uri('/Users/ruulai/Test2')
        }
    }

    dependencies {
        //格式为-->group:module:version
        classpath 'com.dd.cc:cacheok:1.0.0'
    }
}

```
