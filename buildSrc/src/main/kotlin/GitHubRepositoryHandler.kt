import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.kotlin.dsl.maven

private const val KEY_GITHUB_USER = "GITHUB_USER"
private const val KEY_GITHUB_PERSONAL_ACCESS_TOKEN = "GITHUB_PERSONAL_ACCESS_TOKEN"

fun RepositoryHandler.github(
    project: Project,
    url: Any,
    action: (MavenArtifactRepository.() -> Unit)? = null
): MavenArtifactRepository =
    maven(url) {
        credentials {
            username = SystemProperties.get(project, KEY_GITHUB_USER)
            password = SystemProperties.get(project, KEY_GITHUB_PERSONAL_ACCESS_TOKEN)
        }
        action?.invoke(this)
    }
