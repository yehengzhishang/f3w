package com.yu.zz.help;

import com.android.build.gradle.BaseExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.internal.CompileOptions;
import com.android.build.gradle.internal.dsl.BuildType;
import com.android.build.gradle.internal.dsl.DefaultConfig;
import com.yu.zz.param.AndroidParam;

import org.gradle.api.Action;
import org.gradle.api.JavaVersion;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionAware;
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions;

public class LibraryHelper {
    private static final int COMPILE_SDK = AndroidParam.SDK_BASE;
    private static final int MIN_SDK = AndroidParam.MIN_SDK;
    private static final int VERSION_CODE = 1;
    private static final int TARGET_SDK = AndroidParam.SDK_BASE;
    private static final String VERSION_NAME = "1.0";
    private static final JavaVersion JAVA_VERSION = JavaVersion.VERSION_1_8;
    private static final String KOTLIN_JAVA_JVM = "1.8";

    private final int compileSdk;
    private final int minSdk;
    private final int targetSdk;
    private final int versionCode;
    private final String versionName;
    private final JavaVersion javaVersion;
    private final String kotlinJavaJvm;

    public LibraryHelper(int compileSdk, int minSdk, int targetSdk, int versionCode, String versionName,
                         JavaVersion javaVersion, String kotlinJavaJvm) {
        this.compileSdk = compileSdk;
        this.minSdk = minSdk;
        this.targetSdk = targetSdk;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.javaVersion = javaVersion;
        this.kotlinJavaJvm = kotlinJavaJvm;
    }

    public static void android(Project project) {
        new LibraryHelper(COMPILE_SDK, MIN_SDK, TARGET_SDK, VERSION_CODE, VERSION_NAME, JAVA_VERSION, KOTLIN_JAVA_JVM).androidInner(project);
    }

    public void androidInner(Project project) {
        BaseExtension extension = (LibraryExtension) project.getExtensions().getByName("android");
        DefaultConfig defaultConfig = extension.getDefaultConfig();
        setCompileSdk(extension, compileSdk);
        setMinSdk(defaultConfig, minSdk);
        setTargetSdk(defaultConfig, targetSdk);
        setVersion(defaultConfig, versionCode, versionName);
        setJava(extension.getCompileOptions(), javaVersion);
        setKotlin(extension, kotlinJavaJvm);
        //gradle.properties 设置
//        extension.getBuildFeatures().setViewBinding(true);
    }

    private static void setCompileSdk(BaseExtension extension, int compileSdk) {
        String sdkVersion = extension.getCompileSdkVersion();
        if (sdkVersion != null) {
            return;
        }
        extension.setCompileSdkVersion(compileSdk);
    }

    private static void setMinSdk(DefaultConfig defaultConfig, int minSdk) {
        if (defaultConfig.getMinSdk() != null) {
            return;
        }
        defaultConfig.setMinSdk(minSdk);
    }

    private static void setTargetSdk(DefaultConfig defaultConfig, int targetSdk) {
        if (defaultConfig.getTargetSdk() == null) {
            return;
        }
        defaultConfig.setTargetSdk(targetSdk);
    }

    private static void setVersion(DefaultConfig defaultConfig, int versionCode, String versionName) {
        if (defaultConfig.getVersionCode() != null || defaultConfig.getVersionName() != null) {
            return;
        }
        defaultConfig.versionCode(versionCode);
        defaultConfig.versionName(versionName);
    }

    private static void setJava(CompileOptions compileOptions, JavaVersion version) {
        if (compileOptions.getSourceCompatibility() != version) {
            compileOptions.setSourceCompatibility(version);
        }
        if (compileOptions.getTargetCompatibility() != version) {
            compileOptions.setTargetCompatibility(version);
        }
    }

    private static void setKotlin(BaseExtension extension, String kotlinJavaJvm) {
        KotlinJvmOptions s = (KotlinJvmOptions) ((ExtensionAware) extension).getExtensions().getByName("kotlinOptions");
        s.setJvmTarget(kotlinJavaJvm);
    }

    /// 基本没啥用。。。写在这里打个样。。。需要自取
    private static void buildTypes(BaseExtension extension) {
        NamedDomainObjectContainer<BuildType> namedDomainObjectContainer = extension.getBuildTypes();
        namedDomainObjectContainer.create("aDebug", new Action<BuildType>() {
            @Override
            public void execute(BuildType buildType) {
                buildType.minifyEnabled(false);
                buildType.debuggable(true);
            }
        });
        System.out.println("rainrain buildType " + namedDomainObjectContainer.getByName("release"));
        System.out.println("rainrain buildType " + namedDomainObjectContainer.getByName("debug"));
    }
}
