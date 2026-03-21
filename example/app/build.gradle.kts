plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.goodoo_pay_native"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.goodoo_pay_native"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        val releaseKeystorePath = project.findProperty("release.keystore.path") as String?
        val releaseKeystorePassword = project.findProperty("release.keystore.password") as String?
        val releaseKeyAlias = project.findProperty("release.keystore.keyAlias") as String?
        val releaseKeyPassword = project.findProperty("release.keystore.keyPassword") as String?
        if (releaseKeystorePath != null && releaseKeystorePassword != null && releaseKeyAlias != null && releaseKeyPassword != null) {
            create("release") {
                storeFile = file(releaseKeystorePath)
                storePassword = releaseKeystorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("io.flutter:arm64_v8a_release:1.0.0-cf56914b326edb0ccb123ffdc60f00060bd513fa")
    implementation("io.flutter:flutter_embedding_release:1.0.0-cf56914b326edb0ccb123ffdc60f00060bd513fa")
    implementation("io.github.goodoollc:goodoo_pay_flutter:1.0.4")
    implementation("io.github.goodoollc:goodoo_pay_sdk:1.0.4")
}

