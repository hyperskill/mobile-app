package org.hyperskill.app.study_plan.widget.view

import kotlin.math.roundToLong
import org.hyperskill.app.core.view.mapper.DateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.firstVisibleSection
import org.hyperskill.app.study_plan.widget.presentation.getSectionActivities
import org.hyperskill.app.study_plan.widget.view.StudyPlanWidgetViewState.SectionContent

class StudyPlanWidgetViewStateMapper(private val dateFormatter: DateFormatter) {
    fun map(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState =
        when (state.sectionsStatus) {
            StudyPlanWidgetFeature.ContentStatus.IDLE -> StudyPlanWidgetViewState.Idle
            StudyPlanWidgetFeature.ContentStatus.LOADING -> StudyPlanWidgetViewState.Loading
            StudyPlanWidgetFeature.ContentStatus.ERROR -> StudyPlanWidgetViewState.Error
            StudyPlanWidgetFeature.ContentStatus.LOADED -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState.Content {
        val firstVisibleSectionId = state.firstVisibleSection()?.id

        return StudyPlanWidgetViewState.Content(
            sections = state.studyPlan?.sections?.mapNotNull { sectionId ->
                val sectionInfo = state.studyPlanSections[sectionId] ?: return@mapNotNull null
                val section = sectionInfo.studyPlanSection

                val shouldShowSectionStatistics = firstVisibleSectionId == section.id || sectionInfo.isExpanded

                StudyPlanWidgetViewState.Section(
                    id = section.id,
                    title = section.title,
                    subtitle = section.subtitle.takeIf { it.isNotEmpty() },
                    content = getSectionContent(
                        state = state,
                        sectionInfo = sectionInfo
                    ),
                    formattedTopicsCount = if (shouldShowSectionStatistics) {
                        formatTopicsCount(
                            completedTopicsCount = section.completedTopicsCount,
                            topicsCount = section.topicsCount
                        )
                    } else {
                        null
                    },
                    formattedTimeToComplete = if (shouldShowSectionStatistics) {
                        section.secondsToComplete
                            ?.roundToLong()
                            ?.let(dateFormatter::hoursWithMinutesCount)
                            ?.takeIf { it.isNotEmpty() }
                    } else {
                        null
                    }
                )
            } ?: emptyList()
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
                    val activities = state.getSectionActivities(sectionInfo.studyPlanSection.id)
                    if (activities.isEmpty()) {
                        SectionContent.Loading
                    } else {
                        getContent(activities)
                    }
                }
                StudyPlanWidgetFeature.ContentStatus.ERROR -> SectionContent.Error
                StudyPlanWidgetFeature.ContentStatus.LOADED -> {
                    val activities = state.getSectionActivities(sectionInfo.studyPlanSection.id)
                    if (activities.isNotEmpty()) {
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
                    title = activity.title.ifBlank { activity.id.toString() },
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
                    isIdeRequired = activity.isIdeRequired,
                    progress = null, // TODO: ALTAPPS-713 add data with new activities API
                    formattedProgress = null, // TODO: ALTAPPS-713 add data with new activities API
                    hypercoinsAward = activity.hypercoinsAward.takeIf { it > 0 }
                )
            }
        )

    private fun formatTopicsCount(completedTopicsCount: Int, topicsCount: Int): String? =
        if (completedTopicsCount > 0 && topicsCount > 0) {
            "$completedTopicsCount / $topicsCount"
        } else {
            null
        }
}