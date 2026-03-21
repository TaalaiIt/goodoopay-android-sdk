plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // Local AAR repo (run ./scripts/build_aar.sh from project root first)
        // maven {
        //     url = java.io.File(rootProject.projectDir, "../../build/host/outputs/repo").toURI()
        // }
        // Flutter engine and embedding (required by flutter_release AAR)
        maven {
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
    }
}

