import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    id("io.gitlab.arturbosch.detekt")
}

val configFile: ConfigurableFileCollection = rootProject.files("config/detekt/detekt.yml")
val baselineFile: File = rootProject.file("config/detekt/baseline.xml")

tasks.withType<Detekt> {
    config.from(configFile)
    setSource(files(projectDir))
    /*baseline.set(baselineFile)*/
    parallel = true
    buildUponDefaultConfig = true
    allRules = true

    include("**/*.kt")
    exclude("**/resources/**", "**/build/**")

    // Target version of the generated JVM bytecode. It is used for type resolution.
    this.jvmTarget = "1.8"
}

tasks.register<DetektCreateBaselineTask>("detektGenerateAllModulesBaseline") {
    description = "Custom DETEKT build to build baseline for all modules"
    setSource(files(rootDir))
    baseline.set(baselineFile)
    config.setFrom(configFile)
    include("**/*.kt")
    exclude("**/resources/**", "**/build/**")
}

dependencies {
    detektPlugins(libs.detekt.composeRules)
}
