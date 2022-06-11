println("rainrain include settings start ")
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("androidGradlePlugin", "7.2.0")
            library(
                "android-gradelPlugin",
                "com.android.tools.build",
                "gradle"
            ).versionRef("androidGradlePlugin")
        }
        create("zhaozhe") {
            version("age", "30")
        }
    }
}
include(":zz-convention")
println("rainrain include settings end ")