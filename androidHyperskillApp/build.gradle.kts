plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.android.ui.material)
    implementation(libs.android.ui.appcompat)
    implementation(libs.android.ui.constraintlayout)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.bundles.ktor.common)

    testImplementation(libs.bundles.android.test)
}

android {
    compileSdkVersion(appVersions.versions.compileSdk.get().toInt())

    defaultConfig {
        applicationId = "org.hyperskill.app.android"
        minSdkVersion(appVersions.versions.minSdk.get().toInt())
        targetSdkVersion(appVersions.versions.targetSdk.get().toInt())
        versionCode = appVersions.versions.versionCode.get().toInt()
        versionName = appVersions.versions.versionName.get()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
}