println("rainrain include settings start ")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        // 这里有个神奇的问题，"libs" 这个名字 可以换成别的 "zhaozhe" "shaX"都可以但是这个名字，只有当前的project可用，就是build-start文件下用
        create("libs") {
            // 这里又是一个神奇的问题 用toml 文件下，可以通过includeBuild透传到别的project,就是f3w下所有可用，
            // 透传的时候，一定用"libs"这个名字，不管create里面用什么名字，外面的project 只能识别libs
            // from 这个api，更神奇的问题，接收的是个List,但是接收之前，会判断如果List 非空。
            from(files("../gradle/libs.versions.toml"))
            // version library 等等一系列api ,不能通过 includeBuild透传，只能当前project可用
//            version("androidGradlePluginZz", "7.2.0")
//            library(
//                "android-gradelPlugin",
//                "com.android.tools.build",
//                "gradle"
//            ).versionRef("androidGradlePlugin")
        }

    }
}
include(":zz-convention")
println("rainrain include settings end ")