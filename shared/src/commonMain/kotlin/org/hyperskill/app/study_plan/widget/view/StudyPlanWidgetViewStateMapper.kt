package org.hyperskill.app.study_plan.widget.view

import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState.SectionContent
import kotlin.math.roundToLong

class StudyPlanWidgetViewStateMapper(private val dateFormatter: DateFormatter) {
    fun map(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState =
        when (state.sectionsStatus) {
            StudyPlanWidgetFeature.ContentStatus.IDLE -> StudyPlanWidgetViewState.Idle
            StudyPlanWidgetFeature.ContentStatus.LOADING -> StudyPlanWidgetViewState.Loading
            StudyPlanWidgetFeature.ContentStatus.ERROR -> StudyPlanWidgetViewState.Error
            StudyPlanWidgetFeature.ContentStatus.LOADED -> {
                StudyPlanWidgetViewState.Content(
                    sections = state.studyPlanSections.map { (_, sectionInfo) ->
                        val section = sectionInfo.studyPlanSection
                        StudyPlanWidgetViewState.Section(
                            id = section.id,
                            title = section.title,
                            subtitle = section.subtitle.takeIf { it.isNotEmpty() },
                            content = getSectionContent(sectionInfo, state),
                            formattedTopicsCount = formatTopicsCount(
                                completedTopicsCount = section.completedTopicsCount,
                                topicsCount = section.topicsCount
                            ),
                            formattedTimeToComplete = section.secondsToComplete
                                ?.roundToLong()
                                ?.let(dateFormatter::hoursWithMinutesCount)
                        )
                    }
                )
            }
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
                StudyPlanWidgetViewState.SectionItem(activity.id)
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