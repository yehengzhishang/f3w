plugins {
    `kotlin-dsl`
}

group = "com.yu.zz.buildstart"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("com.android.tools.build:gradle:7.2.0")
}

gradlePlugin {
    plugins {
        register("ZzLibraryHelper") {
            // 在 app 模块需要通过 id 引用这个插件
            id = "zz.library"
            // 实现这个插件的类的路径,没有package ,直接写类名了
            implementationClass = "AndroidLibraryPlugin"
        }
    }
}