import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.propertyString

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.moko.kswift")
}

dependencies {
    ktlintRuleset(libs.ktlintRules)
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    if (OperatingSystem.current().isMacOsX) {
        cocoapods {
            summary = "Shared code between iOS and Android"
            homepage = "https://github.com/hyperskill/mobile-app"
            ios.deploymentTarget = "14.0"
            podfile = project.file("../iosHyperskillApp/Podfile")
            framework {
                baseName = "shared"
                isStatic = false
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.bundles.ktor.common)
                implementation(libs.kit.model)
                implementation(libs.kotlin.datetime)
                implementation(libs.kit.presentation.reduxCoroutines)

                api(libs.kit.presentation.redux)
                api(libs.mokoResources.main)
                api(libs.mokoKswiftRuntime.main)
                api(libs.multiplatform.settings)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.kotlin.coroutines.test)
                implementation(libs.mokoResources.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.android.security)
                implementation(libs.ktor.android)
                implementation(libs.kit.view.redux)
                implementation(libs.android.lifecycle.viewmodel.ktx)
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
    exposeObjectWithName = "BuildKonfig"

    defaultConfigs {
        // required
    }

    fun applyFlavorConfigsFromFile(flavor: String) {
        if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return
        defaultConfigs(flavor) {
            val properties = loadProperties("${project.rootDir}/shared/keys/$flavor.properties")
            buildConfigField(STRING, "FLAVOR", flavor)
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
    applyFlavorConfigsFromFile("dev")
    applyFlavorConfigsFromFile("release")
    // add flavors for release.hyperskill.org / dev.hyperskill.org on demand
}

// Resources directory - src/commonMain/resources/MR
multiplatformResources {
    multiplatformResourcesPackage = "org.hyperskill.app"
    multiplatformResourcesClassName = "SharedResources"
}

ktlint {
    filter {
        exclude { element -> element.file.path.contains("build/") }
    }
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)

    iosDeploymentTarget.set("14.0")
}

// Copies generated shared Swift file by moko-kswift to iosHyperskillApp
tasks.withType<KotlinNativeLink>()
    .matching { it.binary is Framework }
    .configureEach {
        doLast {
            val kSwiftGeneratedDir = destinationDirectory.get()
                .dir("${binary.baseName}Swift")
                .asFile
            val kSwiftSharedGeneratedSwiftFile = kSwiftGeneratedDir
                .resolve("Hyperskill-Mobile_shared.swift")

            val iosHyperskillAppSharedSwiftFile = rootDir
                .resolve("iosHyperskillApp/iosHyperskillApp/Sources/Frameworks/sharedSwift/Hyperskill-Mobile_shared.swift")

            kSwiftSharedGeneratedSwiftFile.copyTo(iosHyperskillAppSharedSwiftFile, overwrite = true)
        }
    }
