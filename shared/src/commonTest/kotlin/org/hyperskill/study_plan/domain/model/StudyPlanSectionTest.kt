package org.hyperskill.study_plan.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection
import org.hyperskill.app.study_plan.domain.model.firstRootTopicsActivityIndexToBeLoaded
import org.hyperskill.app.study_plan.domain.model.rootTopicsActivitiesToBeLoaded

class StudyPlanSectionTest {
    @Test
    fun `firstRootTopicsActivityIndexToBeLoaded returns zero when nextActivityId is null`() {
        val section = StudyPlanSection.stub(
            nextActivityId = null,
            activities = listOf(1L, 2L, 3L)
        )
        assertEquals(0, section.firstRootTopicsActivityIndexToBeLoaded)
    }

    @Test
    fun `firstRootTopicsActivityIndexToBeLoaded returns index when nextActivityId is in activities`() {
        val section = StudyPlanSection.stub(
            nextActivityId = 2L,
            activities = listOf(1L, 2L, 3L)
        )
        assertEquals(1, section.firstRootTopicsActivityIndexToBeLoaded)
    }

    @Test
    fun `firstRootTopicsActivityIndexToBeLoaded returns zero when nextActivityId is not in activities`() {
        val section = StudyPlanSection.stub(
            nextActivityId = 4L,
            activities = listOf(1L, 2L, 3L)
        )
        assertEquals(0, section.firstRootTopicsActivityIndexToBeLoaded)
    }

    @Test
    fun `rootTopicsActivitiesToBeLoaded returns all activities when firstRootTopicsActivityIndex is zero`() {
        val section = StudyPlanSection.stub(
            nextActivityId = null,
            activities = listOf(1L, 2L, 3L)
        )
        assertEquals(listOf(1L, 2L, 3L), section.rootTopicsActivitiesToBeLoaded)
    }

    @Test
    fun `rootTopicsActivitiesToBeLoaded returns sublist when firstRootTopicsActivityIndex is non zero`() {
        val section = StudyPlanSection.stub(
            nextActivityId = 2L,
            activities = listOf(1L, 2L, 3L)
        )
        assertEquals(listOf(2L, 3L), section.rootTopicsActivitiesToBeLoaded)
    }
}