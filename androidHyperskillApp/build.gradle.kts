import com.android.build.api.dsl.ApplicationBuildType
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.propertyString

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.android.ui.material)
    implementation(libs.android.ui.appcompat)
    implementation(libs.android.ui.constraintlayout)
    implementation(libs.android.ui.swiperefreshlayout)
    implementation(libs.android.ui.core.ktx)
    implementation(libs.android.ui.fragment)
    implementation(libs.android.ui.fragment.ktx)
    implementation(libs.android.lifecycle.runtime)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.kit.view.ui)
    implementation(libs.kit.view.injection)
    implementation(libs.kit.view.redux)
    implementation(libs.kit.view.navigation)

    implementation(libs.bundles.ktor.common)

    implementation(libs.gms.services)
    implementation(libs.gms.login)
    implementation(libs.viewbinding)
    implementation(libs.kit.ui.adapters)

    testImplementation(libs.bundles.android.test)

    ktlintRuleset(libs.ktlintRules)

    implementation(libs.android.material.progress.bar)
    implementation(libs.android.sentry)
    implementation(libs.android.sentry.fragment)
    implementation(libs.android.parcelable)

    debugImplementation(libs.android.flipper)
    debugImplementation(libs.android.soloader)
    releaseImplementation(libs.android.flipper.noop)

    implementation(libs.android.reactivex.rxandroid)
    implementation(libs.android.reactivex.rxjava)
    implementation(libs.android.reactivex.rxkotlin)
    implementation(libs.android.coil)
    implementation(libs.android.coil.svg)
    implementation(libs.android.splashscreen)
    implementation(libs.android.timepicker)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.preview)
    implementation(libs.compose.material)
    debugImplementation(libs.compose.tooling)
    implementation(libs.accompanist.themeadapter)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.lifecycle)

    coreLibraryDesugaring(libs.android.desugar.jdk)
}

android {
    compileSdk = appVersions.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.hyperskill.app.android"
        minSdk = appVersions.versions.minSdk.get().toInt()
        targetSdk = appVersions.versions.targetSdk.get().toInt()
        versionCode = appVersions.versions.versionCode.get().toInt()
        versionName = appVersions.versions.versionName.get()

        // Prevent other localisations because of ru.nobird.ui ru localization
        resourceConfigurations.add("en")
    }

    signingConfigs {
        getByName("debug") {
            if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return@getByName
            val properties = loadProperties("${project.rootDir}/androidHyperskillApp/keys/debug_keystore.properties")

            storeFile = file("../buildsystem/certs/debug.keystore")
            storePassword = properties.getProperty("HYPERSKILL_DEBUG_STORE_PASSWORD")
            keyAlias = properties.getProperty("HYPERSKILL_DEBUG_KEY_ALIAS")
            keyPassword = properties.getProperty("HYPERSKILL_DEBUG_KEY_PASSWORD")

            enableV3Signing = true
            enableV4Signing = true
        }

        create("release") {
            if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return@create

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
        fun applyFlavorConfigsFromFile(applicationBuildType: ApplicationBuildType) {
            if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return
            val properties = loadProperties("${project.rootDir}/androidHyperskillApp/keys/${applicationBuildType.name}.properties")
            properties.keys.forEach { name ->
                name as String
                applicationBuildType.buildConfigField(
                    type = "String",
                    name = name,
                    value = requireNotNull(System.getenv(name) ?: properties.propertyString(name))
                )
            }
        }

        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applyFlavorConfigsFromFile(this)
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            applyFlavorConfigsFromFile(this)
        }
        create("internalRelease") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks.add("release")
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0-alpha02"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true

        // Sets Java compatibility to Java 8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

ktlint {
    filter {
        exclude { element -> element.file.path.contains("build/") }
    }
}