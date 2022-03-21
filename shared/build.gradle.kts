import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.propertyString

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
}

version = "1.0"

kotlin {
    android()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutines.core)

                //network
                implementation(libs.bundles.ktor.common)

                implementation(libs.kit.model)

                api(libs.kit.presentation.redux)
                implementation(libs.kit.presentation.reduxCoroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.coroutines.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.bundles.android.test)
                implementation(libs.kotlin.coroutines.test)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)

            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.ktor.ios)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)

            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdkVersion(appVersions.versions.compileSdk.get().toInt())
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(appVersions.versions.minSdk.get().toInt())
        targetSdkVersion(appVersions.versions.targetSdk.get().toInt())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources {
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/AL2.0"
        }
    }
}

buildkonfig {
    packageName = "org.hyperskill.app.config"

    defaultConfigs {
        // required
    }

    fun applyFlavorConfigsFromFile(flavor: String) {
        defaultConfigs(flavor) {
            val properties = loadProperties("${project.rootDir}/shared/keys/$flavor.properties")
            properties.keys.forEach { name ->
                name as String
                buildConfigField(
                    type = STRING,
                    name = name,
                    value = requireNotNull(System.getenv(name) ?: properties.propertyString(name))
                )
            }
        }
    }

    applyFlavorConfigsFromFile("production")
    // add flavors for release.hyperskill.org / dev.hyperskill.org on demand
}