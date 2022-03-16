// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(BuildClassesConfig.ANDROID_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.KOTLIN_GRADLE_PLUGIN)
        classpath(BuildClassesConfig.HILT_GRADLE_PLUGIN)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.0")


        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven {
            setUrl("https://jitpack.io")
        }
        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/mb28/android-yap-ui-kit/src/releases")
        }

        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/mb28/android-yap-permissions/src/releasesLib")
        }

        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/mb28/android-yap-scanner/src/releasesLib")
        }

        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/mb28/android-yap-core/src/releases")
        }

        maven {
            credentials {
                username = AppSecrets.USERNAME
                password = AppSecrets.PASSWORD
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
            setUrl("https://api.bitbucket.org/2.0/repositories/yap-technology/android-b2c-pk/src/releases")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}