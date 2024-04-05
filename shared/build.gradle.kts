import com.android.build.api.dsl.LibraryBuildType
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.time.Year
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.konan.properties.Properties
import org.jetbrains.kotlin.konan.properties.loadProperties
import org.jetbrains.kotlin.konan.properties.propertyString
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.codingfeline.buildkonfig")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.moko.kswift")
    id("org.jetbrains.dokka")
}

dependencies {
    ktlintRuleset(libs.ktlintRules)
}

// Don't change version, because this forces iOS dependencies reinstall -> CI/CD execution time increases.
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
                // Delete options passed to a system linker after upgrading to the Kotlin 1.9.10
                // https://youtrack.jetbrains.com/issue/KT-60230
                linkerOpts += "-ld64"
                // Add export declarations to use moko-resources iOS extensions from Swift side
                export(libs.mokoResources.main)
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
                implementation(libs.kermit)

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
                implementation(libs.android.lifecycle.viewmodel.savedstate)
                implementation(libs.android.ui.fragment)
                implementation(libs.android.sentry.okhttp)
                implementation(libs.android.lifecycle.runtime)
                implementation(libs.android.parcelable)
                implementation(project.dependencies.platform(libs.firebase.bom))
                implementation(libs.firebase.messaging)
                implementation(libs.revenuecat)
                implementation(libs.kermit)
                implementation(libs.android.appsFlyer)
                implementation(libs.googlePlay.installreferrer)
            }
        }
        val androidUnitTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("reflect"))
                implementation(libs.bundles.android.test)
                implementation(libs.kotlin.coroutines.test)
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktor.ios)
            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosTest by creating {
            dependsOn(commonTest)
        }
        val iosX64Test by getting {
            dependsOn(iosTest)
        }
        val iosArm64Test by getting {
            dependsOn(iosTest)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(iosTest)
        }
    }
}

android {
    compileSdk = appVersions.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = appVersions.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packagingOptions {
        resources {
            excludes += "META-INF/LGPL2.1"
            excludes += "META-INF/AL2.0"
        }
    }
    namespace = "org.hyperskill.app"

    sourceSets {
        getByName("main").java.srcDirs("build/generated/moko/androidMain/src")
    }

    buildTypes {
        fun applyFlavorConfigsFromFile(libraryBuildType: LibraryBuildType) {
            if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return
            val properties: Properties =
                loadProperties("${project.rootDir}/shared/src/androidMain/keys/revenuecat.properties")
            properties.keys.forEach { name ->
                name as String
                libraryBuildType.buildConfigField(
                    type = "String",
                    name = name,
                    value = requireNotNull(properties.propertyString(name))
                )
            }
        }

        all {
            applyFlavorConfigsFromFile(this)
        }

        release {
            proguardFiles("proguard-rules.pro")
        }
    }
}

buildkonfig {
    packageName = "org.hyperskill.app.config"
    objectName = "InternalBuildKonfig"

    defaultConfigs {
        if (SystemProperties.isCI() && !SystemProperties.isGitCryptUnlocked()) return@defaultConfigs

        buildConfigField(
            type = BOOLEAN,
            name = "IS_INTERNAL_TESTING",
            value = SystemProperties.isInternalTesting()?.toString(),
            nullable = true
        )

        listOf("production", "dev", "release").forEach { flavor ->
            val properties = loadProperties("${project.rootDir}/shared/keys/$flavor.properties")

            val fieldNamePrefix = "${flavor.toUpperCase()}_"
            buildConfigField(
                type = STRING,
                name = "${fieldNamePrefix}FLAVOR",
                value = flavor,
                const = true
            )

            properties.keys.forEach { name ->
                name as String
                buildConfigField(
                    type = STRING,
                    name = "${fieldNamePrefix}$name",
                    value = requireNotNull(properties.propertyString(name)),
                    const = true
                )
            }
        }
    }
}

// Resources directory - src/commonMain/resources/MR
multiplatformResources {
    multiplatformResourcesPackage = "org.hyperskill.app"
    multiplatformResourcesClassName = "SharedResources"
}

ktlint {
    reporters {
        reporter(ReporterType.SARIF)
    }
    filter {
        exclude { element -> element.file.path.contains("build/") }
    }
    // This is necessary for GitHub Code Scanning,
    // so that GitHub knows where the repository is to place annotations correctly.
    // https://github.com/pinterest/ktlint/blob/9ed074638edf15986fa33d3c810acb1495a98612/ktlint-cli-reporter-sarif/src/main/kotlin/com/pinterest/ktlint/cli/reporter/sarif/SarifReporter.kt#L45
    System.setProperty("user.home", rootProject.projectDir.absolutePath)
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
                .resolve(
                    "iosHyperskillApp/iosHyperskillApp/Sources/Frameworks/sharedSwift/Hyperskill-Mobile_shared.swift"
                )
            if (!iosHyperskillAppSharedSwiftFile.exists()) {
                iosHyperskillAppSharedSwiftFile.createNewFile()
            }

            kSwiftSharedGeneratedSwiftFile.copyTo(iosHyperskillAppSharedSwiftFile, overwrite = true)
        }
    }

tasks.register("dokkaAnalytics", DokkaTask::class.java) {
    outputDirectory.set(buildDir.resolve("dokka/analytics"))

    moduleName.set("Hyperskill Mobile Analytics")
    moduleVersion.set(appVersions.versions.versionName.get())

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        footerMessage = "© ${Year.now().value} Hyperskill"
    }

    dokkaSourceSets {
        configureEach {
            // Suppress all packages and then enable only the ones we want
            perPackageOption {
                suppress.set(true)
                // Do not create index pages for empty packages
                skipEmptyPackages.set(true)
            }

            perPackageOption {
                matchingRegex.set(""".*\.domain.analytic.*""")
                suppress.set(false)
            }

            perPackageOption {
                matchingRegex.set(""".*\.analytic.domain.model.*""")
                suppress.set(false)
            }
        }
    }
}