buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.0")
        classpath("com.android.tools.build:gradle:7.0.3")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        repositories {
            maven {
                name = "GitHub"
                url = uri("https://maven.pkg.github.com/eadm/AndroidKit")
                credentials {
                    username = System.getenv("GITHUB_USER")
                        ?: project.properties["GITHUB_USER"] as String?

                    password = System.getenv("GITHUB_PERSONAL_ACCESS_TOKEN")
                        ?: project.properties["GITHUB_PERSONAL_ACCESS_TOKEN"] as String?
                }
            }
        }
        mavenLocal()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}