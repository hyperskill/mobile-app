package org.hyperskill.app.study_plan.widget.view

import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.firstSection
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState.SectionContent
import kotlin.math.roundToLong

class StudyPlanWidgetViewStateMapper(private val dateFormatter: DateFormatter) {
    fun map(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState =
        when (state.sectionsStatus) {
            StudyPlanWidgetFeature.ContentStatus.IDLE -> StudyPlanWidgetViewState.Idle
            StudyPlanWidgetFeature.ContentStatus.LOADING -> StudyPlanWidgetViewState.Loading
            StudyPlanWidgetFeature.ContentStatus.ERROR -> StudyPlanWidgetViewState.Error
            StudyPlanWidgetFeature.ContentStatus.LOADED -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState.Content {
        val firstSectionId = state.firstSection()?.id

        return StudyPlanWidgetViewState.Content(
            sections = state.studyPlanSections.map { (_, sectionInfo) ->
                val section = sectionInfo.studyPlanSection
                StudyPlanWidgetViewState.Section(
                    id = section.id,
                    title = section.title,
                    subtitle = section.subtitle.takeIf { it.isNotEmpty() },
                    content = getSectionContent(
                        state = state,
                        sectionInfo = sectionInfo
                    ),
                    formattedTopicsCount = if (firstSectionId == section.id) {
                        formatTopicsCount(
                            completedTopicsCount = section.completedTopicsCount,
                            topicsCount = section.topicsCount
                        )
                    } else {
                        null
                    },
                    formattedTimeToComplete = if (firstSectionId == section.id) {
                        section.secondsToComplete
                            ?.roundToLong()
                            ?.let(dateFormatter::hoursWithMinutesCount)
                    } else {
                        null
                    }
                )
            }
        )
    }

    private fun getSectionContent(
        sectionInfo: StudyPlanWidgetFeature.StudyPlanSectionInfo,
        state: StudyPlanWidgetFeature.State
    ): SectionContent =
        if (sectionInfo.isExpanded) {
            when (sectionInfo.contentStatus) {
                StudyPlanWidgetFeature.ContentStatus.IDLE -> SectionContent.Collapsed
                StudyPlanWidgetFeature.ContentStatus.LOADING -> {
                    val activities = getSectionActivities(state, sectionInfo.studyPlanSection.id)
                    if (activities.isNullOrEmpty()) {
                        SectionContent.Loading
                    } else {
                        getContent(activities)
                    }
                }
                StudyPlanWidgetFeature.ContentStatus.ERROR -> SectionContent.Error
                StudyPlanWidgetFeature.ContentStatus.LOADED -> {
                    val activities = getSectionActivities(state, sectionInfo.studyPlanSection.id)
                    if (activities != null) {
                        getContent(activities)
                    } else {
                        SectionContent.Error
                    }
                }
            }
        } else {
            SectionContent.Collapsed
        }

    private fun getContent(activities: List<LearningActivity>): SectionContent.Content =
        SectionContent.Content(
            sectionItems = activities.map { activity ->
                StudyPlanWidgetViewState.SectionItem(
                    id = activity.id,
                    title = activity.id.toString(), // TODO: replace with real data with new activities API
                    state = when (activity.state) {
                        LearningActivityState.TODO -> if (activity.isCurrent) {
                            StudyPlanWidgetViewState.SectionItemState.NEXT
                        } else {
                            StudyPlanWidgetViewState.SectionItemState.LOCKED
                        }
                        LearningActivityState.SKIPPED -> StudyPlanWidgetViewState.SectionItemState.SKIPPED
                        LearningActivityState.COMPLETED -> StudyPlanWidgetViewState.SectionItemState.COMPLETED
                        null -> StudyPlanWidgetViewState.SectionItemState.IDLE
                    },
                    progress = null, // TODO: add data with new activities API
                    formattedProgress = null // TODO: add data with new activities API
                )
            }
        )

    private fun getSectionActivities(state: StudyPlanWidgetFeature.State, sectionId: Long): List<LearningActivity>? =
        state.activities[sectionId]?.toList()

    private fun formatTopicsCount(completedTopicsCount: Int, topicsCount: Int): String? =
        if (completedTopicsCount > 0  && topicsCount > 0) {
            "$completedTopicsCount / $topicsCount"
        } else {
            null
        }
}