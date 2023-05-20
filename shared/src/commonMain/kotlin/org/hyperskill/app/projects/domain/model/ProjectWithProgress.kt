package org.hyperskill.app.projects.domain.model

data class ProjectWithProgress(
    val project: Project,
    val progress: ProjectProgress
) {
    companion object
}

/**
 * Sorts projects by feature, base or default score in descending order.
 * Order of projects with the same score is determined by their ids.
 *
 * Don't change the order of selector lambdas, it's sorted by importance of parameters.
 */
fun Iterable<ProjectWithProgress>.sortByScore(
    compareByFeatureScore: Boolean
): List<ProjectWithProgress> =
    sortedWith { a, b ->
        compareValuesBy(
            b, a,
            {
                if (compareByFeatureScore) {
                    it.progress.featureScore
                } else {
                    null
                }
            },
            { it.progress.baseScore },
            { it.project.defaultScore },
            { it.project.id }
        )
    }