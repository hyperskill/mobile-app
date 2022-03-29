buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
        gradlePluginPortal()
        mavenLocal()
    }
    dependencies {
        classpath(libs.plugin.kotlin)
        classpath(libs.plugin.kotlinSerialization)
        classpath(libs.plugin.android)
        classpath(libs.plugin.ktlint)
        classpath(libs.plugin.gradleVersionUpdates)
        classpath(libs.plugin.buildKonfig)
        classpath(libs.plugin.mokoResources)
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "com.github.ben-manes.versions")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        github(project, "https://maven.pkg.github.com/eadm/AndroidKit")
        github(project, "https://maven.pkg.github.com/eadm/ktlint-rules")
        mavenLocal()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}