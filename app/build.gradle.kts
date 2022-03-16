plugins {
    id(BuildPluginsConfig.androidApplication)
    id(BuildPluginsConfig.androidHilt)
    kotlin(BuildPluginsConfig.kotlinAndroid)
    kotlin(BuildPluginsConfig.kotlinKapt)
    id("kotlin-android")
}

android {
    compileSdk = (BuildAndroidConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId = BuildAndroidConfig.APPLICATION_ID
        minSdk = BuildAndroidConfig.MIN_SDK_VERSION
        targetSdk = BuildAndroidConfig.TARGET_SDK_VERSION

        testInstrumentationRunner = BuildAndroidConfig.androidTestInstrumentation
    }

    signingConfigs {
        create("release") {
            try {
                storeFile = File(
                    project.rootDir,
                    "app${findProperty("android_releasekey_storefile")}" as String?
                )
                keyAlias = project.property("android_releasekey_keyAlias") as String?
                keyPassword = project.property("android_releasekey_keyPassword") as String?
                storePassword = project.property("android_releasekey_storePassword") as String?

            } catch (e: Exception) {
                System.err.println("release signing.properties file is malformed")
            }
        }

        getByName("debug") {
            storeFile = File(
                project.rootDir,
                "app${findProperty("android_debugkey_storefile")}" as String?
            )
            keyAlias = findProperty("android_debugkey_keyAlias") as String?
            keyPassword = findProperty("android_debugkey_keyPassword") as String?
            storePassword = findProperty("android_debugkey_storePassword") as String?
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }

        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }

    flavorDimensions("version")
    productFlavors {
        create("live") {
            versionCode = 1
            versionName = "1.0.0"
            applicationId = "com.yap.pk.banking"
            dimension = "version"
            resValue("string", "app_name", "Yap Pakistan")
            resValue("string", "google_maps_key", "AIzaSyD14efXe-xXjpqUPX85ZhiKaKFHZyANSrE")

        }
        create("Preprod") {
            versionCode = 1
            versionName = "1.0.0"
            applicationId = "com.yap.pk.banking.preprod"
            dimension = "version"
            resValue("string", "app_name", "YapPkPP")
            resValue("string", "google_maps_key", "AIzaSyAwMbe3AAuJfPm6w1LEQpx7IME9iIUiSR4")

        }
        create("stg") {
            versionCode = 1
            versionName = "1.0.0"
            applicationIdSuffix = ".stg"
            dimension = "version"
            resValue("string", "app_name", "YapPkStg")
            resValue("string", "google_maps_key", "AIzaSyAwMbe3AAuJfPm6w1LEQpx7IME9iIUiSR4")

        }
        create("yapinternal") {
            versionCode = 1
            versionName = "1.0.0"
            applicationIdSuffix = ".internal"
            dimension = "version"
            resValue("string", "app_name", "YapPkInternal")
            resValue("string", "google_maps_key", "AIzaSyAwMbe3AAuJfPm6w1LEQpx7IME9iIUiSR4")

        }
        create("qa") {
            versionCode = 61
            versionName = "1.6.1"
            applicationIdSuffix = ".qa"
            dimension = "version"
            resValue("string", "app_name", "YapPkQa")
            resValue("string", "google_maps_key", "AIzaSyAwMbe3AAuJfPm6w1LEQpx7IME9iIUiSR4")

        }

        create("dev") {
            versionCode = 1
            versionName = "1.0.0"
            applicationIdSuffix = ".dev"
            dimension = "version"
            resValue("string", "app_name", "YapPkDev")
            resValue("string", "google_maps_key", "AIzaSyAwMbe3AAuJfPm6w1LEQpx7IME9iIUiSR4")
        }

    }
    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    buildFeatures {
        this.dataBinding = true
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE")
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/gradle/incremental.annotation.processors")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
//    implementation(project(":yappk"))
    implementation("com.yap.yappk:yappk:1.0.0-beta17") {
        exclude(group = "com.yap.uikit", module = "uikit")
//        exclude(group = "com.dlazaro66.qrcodereaderview", module = "qrcodereaderview")
    }
    implementation("com.yap.uikit:uikit:0.0.1.0-alpha06")
    implementation(DependenciesManager.kotlinImplementation)
    implementation(DependenciesManager.lifeCycleKtxImplementation)
    implementation(DependenciesManager.androidxImplementation)
    implementation(DependenciesManager.hiltImplementation)
    implementation(DependenciesManager.mapImplementation)
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.gms:play-services-mlkit-face-detection:16.2.0")
    kapt(HiltDaggerDependencies.DAGGER_COMPILER)
    implementation(DependenciesManager.navigationImplementation)

    testImplementation(DependenciesManager.testingImplementation)
    androidTestImplementation(DependenciesManager.androidTestImplementation)
    implementation(TestDependencies.ANDROIDX_ARCH_CORE)
    implementation(TestDependencies.COROUTINES)
    implementation(DependenciesManager.networkImplementation)
    implementation("com.google.android.gms:play-services-ads-identifier:17.1.0")

    implementation(FireBaseDependencies.FIREBASE_ANALYTICS)

    implementation(LeanplumDependencies.LEANPLUM_FCM)
    implementation(LeanplumDependencies.LEANPLUM_LOCATION)

}