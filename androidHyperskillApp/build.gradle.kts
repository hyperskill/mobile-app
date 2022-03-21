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

    implementation("androidx.navigation:navigation-fragment:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
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

    signingConfigs {
        getByName("debug") {
            storeFile = file("../buildsystem/certs/debug.keystore")
            storePassword = HYPERSKILL_DEBUG_STORE_PASSWORD
            keyAlias = HYPERSKILL_DEBUG_KEY_ALIAS
            keyPassword = HYPERSKILL_DEBUG_KEY_PASSWORD

            enableV3Signing = true
            enableV4Signing = true
        }

        create("release") {
            val keystorePath = SystemProperties.get(project, "HYPERSKILL_KEYSTORE_PATH")
            if (keystorePath.isNullOrBlank()) return@create

            storeFile = file(keystorePath)
            storePassword = SystemProperties.get(project, "HYPERSKILL_RELEASE_STORE_PASSWORD")
            keyAlias = SystemProperties.get(project, "HYPERSKILL_RELEASE_KEY_ALIAS")
            keyPassword = SystemProperties.get(project, "HYPERSKILL_RELEASE_KEY_PASSWORD")

            enableV3Signing = true
            enableV4Signing = true
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
}