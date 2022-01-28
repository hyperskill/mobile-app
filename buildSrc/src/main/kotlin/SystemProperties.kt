import org.gradle.api.Project

object SystemProperties {
    fun get(project: Project, name: String): String? =
        System.getenv(name) ?: project.properties[name] as? String
}
