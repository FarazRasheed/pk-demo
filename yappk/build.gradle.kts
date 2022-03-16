plugins {
    id(BuildPluginsConfig.androidLibrary)
    kotlin(BuildPluginsConfig.kotlinAndroid)
    kotlin(BuildPluginsConfig.kotlinKapt)
    id(BuildPluginsConfig.kotlinParcelize)
    id(BuildPluginsConfig.androidHilt)
}

apply {
    from("../uploadLibrary.gradle")
}
android {
    compileSdk = (BuildAndroidConfig.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdk = BuildAndroidConfig.MIN_SDK_VERSION
        targetSdk = BuildAndroidConfig.TARGET_SDK_VERSION

        testInstrumentationRunner = BuildAndroidConfig.androidTestInstrumentation
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true
        }
    }

    viewBinding {
        android.buildFeatures.viewBinding = true
    }
    buildFeatures {
        this.dataBinding = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(DependenciesManager.kotlinImplementation)
    implementation(DependenciesManager.lifeCycleKtxImplementation)
    implementation(DependenciesManager.androidxImplementation)
    implementation(DependenciesManager.hiltImplementation)
    implementation(DependenciesManager.navigationImplementation)
    implementation(DependenciesManager.mapImplementation)
    implementation(AndroidxDependencies.CAMERAX)
    implementation(AndroidxDependencies.CAMERAX_LIFE_CYCLE)
    implementation(AndroidxDependencies.CAMERAX_VIEW)
    implementation(ThirdPartyDependencies.SSP)
    implementation(ThirdPartyDependencies.EASY_IMAGE)
    implementation(ThirdPartyDependencies.LIB_PHONE)
    implementation(ThirdPartyDependencies.QR_CODE_VIEW)

    api(AndroidAnimations.ANDROID_YOYO)
    api(AndroidAnimations.ANDROID_YOYO_EASING)

    kapt(HiltDaggerDependencies.DAGGER_COMPILER)

    api("com.digitify.core:core:1.0.0-beta16")
    api("com.yap.uikit:uikit:0.0.1.0-alpha06")
    api("com.yap.permissionx:permissionx:1.0.0-beta03")
    api("com.digitify.identityscanner:identityscanner:1.0.1.4-beta01")
    testImplementation(DependenciesManager.testingImplementation)
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    androidTestImplementation(DependenciesManager.androidTestImplementation)
    implementation(TestDependencies.COROUTINES)
    implementation(TestDependencies.ANDROIDX_ARCH_CORE)
    implementation(DependenciesManager.networkImplementation)
    implementation("com.google.android.gms:play-services-ads-identifier:17.1.0")

}