package org.hyperskill

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.projects.domain.model.ProjectWithProgress
import org.hyperskill.projects_selection.stub

class ProjectWithProgressTest {
    @Test
    fun testProjectWithProgressProgressPercentageIsCompleted() {
        val projectWithProgress = ProjectWithProgress.stub(isCompleted = true)

        assertEquals(100, projectWithProgress.progressPercentage)
    }

    @Test
    fun testProjectWithProgressProgressPercentageIsNotCompleted() {
        val projectWithProgress = ProjectWithProgress.stub(
            completedStages = listOf(0, 1, 2),
            stagesIds = listOf(0, 1, 2, 3, 4)
        )

        assertEquals(60, projectWithProgress.progressPercentage)
    }
}