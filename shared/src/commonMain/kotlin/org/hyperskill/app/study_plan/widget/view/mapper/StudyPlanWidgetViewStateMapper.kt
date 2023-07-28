package org.hyperskill.app.study_plan.widget.view.mapper

import kotlin.math.roundToLong
import org.hyperskill.app.core.view.mapper.SharedDateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.view.mapper.LearningActivityTextsMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.getCurrentSection
import org.hyperskill.app.study_plan.widget.presentation.getSectionActivities
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState.SectionContent

class StudyPlanWidgetViewStateMapper(private val dateFormatter: SharedDateFormatter) {
    fun map(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState =
        when (state.sectionsStatus) {
            StudyPlanWidgetFeature.ContentStatus.IDLE -> StudyPlanWidgetViewState.Idle
            StudyPlanWidgetFeature.ContentStatus.LOADING -> StudyPlanWidgetViewState.Loading
            StudyPlanWidgetFeature.ContentStatus.ERROR -> StudyPlanWidgetViewState.Error
            StudyPlanWidgetFeature.ContentStatus.LOADED -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState.Content {
        val currentSectionId = state.getCurrentSection()?.id

        return StudyPlanWidgetViewState.Content(
            sections = state.studyPlan?.sections?.mapNotNull { sectionId ->
                val sectionInfo = state.studyPlanSections[sectionId] ?: return@mapNotNull null
                val section = sectionInfo.studyPlanSection

                val shouldShowSectionStatistics = currentSectionId == section.id || sectionInfo.isExpanded

                StudyPlanWidgetViewState.Section(
                    id = section.id,
                    title = section.title,
                    subtitle = section.subtitle.takeIf { it.isNotEmpty() },
                    isCurrent = section.id == currentSectionId,
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
                            ?.let(dateFormatter::formatHoursWithMinutesCount)
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
                    title = LearningActivityTextsMapper.mapLearningActivityToTitle(activity),
                    subtitle = LearningActivityTextsMapper.mapLearningActivityToSubtitle(activity),
                    state = when (activity.state) {
                        LearningActivityState.TODO -> StudyPlanWidgetViewState.SectionItemState.NEXT
                        LearningActivityState.SKIPPED -> StudyPlanWidgetViewState.SectionItemState.SKIPPED
                        LearningActivityState.COMPLETED -> StudyPlanWidgetViewState.SectionItemState.COMPLETED
                        null -> StudyPlanWidgetViewState.SectionItemState.IDLE
                    },
                    isIdeRequired = activity.isIdeRequired,
                    progress = activity.progressPercentage,
                    formattedProgress = LearningActivityTextsMapper.mapLearningActivityToProgressString(activity),
                    hypercoinsAward = activity.hypercoinsAward.takeIf { it > 0 }
                )
            }
        )

    private fun formatTopicsCount(completedTopicsCount: Int, topicsCount: Int): String? =
        if (topicsCount > 0) {
            "$completedTopicsCount / $topicsCount"
        } else {
            null
        }
}