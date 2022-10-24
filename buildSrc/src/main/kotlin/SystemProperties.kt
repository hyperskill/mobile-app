import org.gradle.api.Project

object SystemProperties {
    fun get(project: Project, name: String): String? =
        System.getenv(name) ?: project.properties[name] as? String

    fun isCI(): Boolean =
        System.getenv("CI")?.toBoolean() ?: false

    fun isGitCryptUnlocked(): Boolean =
        System.getenv("IS_GIT_CRYPT_UNLOCKED")?.toBoolean() ?: false
}
