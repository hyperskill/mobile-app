enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Hyperskill-Mobile"
include(":androidHyperskillApp")
include(":shared")

dependencyResolutionManagement {
    versionCatalogs {
        create("appVersions") {
            from(files("gradle/app.versions.toml"))
        }
    }
}