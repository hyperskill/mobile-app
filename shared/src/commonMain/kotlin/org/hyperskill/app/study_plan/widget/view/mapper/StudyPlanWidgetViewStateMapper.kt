package org.hyperskill.app.study_plan.widget.view.mapper

import kotlin.math.roundToLong
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.view.mapper.LearningActivityTextsMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.ContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.ALL_PAGES_LOADED
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.ERROR
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.FIRST_PAGE_LOADED
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.FIRST_PAGE_LOADING
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.IDLE
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.SectionContentStatus.NEXT_PAGE_LOADING
import org.hyperskill.app.study_plan.widget.presentation.getCurrentActivity
import org.hyperskill.app.study_plan.widget.presentation.getCurrentSection
import org.hyperskill.app.study_plan.widget.presentation.getLoadedSectionActivities
import org.hyperskill.app.study_plan.widget.presentation.getUnlockedActivitiesCount
import org.hyperskill.app.study_plan.widget.presentation.isPaywallShown
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState.SectionContent

class StudyPlanWidgetViewStateMapper(private val dateFormatter: SharedDateFormatter) {
    fun map(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState =
        when (state.sectionsStatus) {
            ContentStatus.IDLE -> StudyPlanWidgetViewState.Idle
            ContentStatus.LOADING -> StudyPlanWidgetViewState.Loading
            ContentStatus.ERROR -> StudyPlanWidgetViewState.Error
            ContentStatus.LOADED -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: StudyPlanWidgetFeature.State): StudyPlanWidgetViewState.Content {
        val currentSectionId = state.getCurrentSection()?.id
        val currentActivityId = state.getCurrentActivity()?.id

        return StudyPlanWidgetViewState.Content(
            sections = state.studyPlanSections.values.map { sectionInfo ->
                val section = sectionInfo.studyPlanSection

                val shouldShowSectionStatistics = currentSectionId == section.id || sectionInfo.isExpanded

                StudyPlanWidgetViewState.Section(
                    id = section.id,
                    title = section.title,
                    subtitle = section.subtitle.takeIf { it.isNotEmpty() },
                    isCurrent = section.id == currentSectionId,
                    content = getSectionContent(
                        state = state,
                        sectionInfo = sectionInfo,
                        currentActivityId = currentActivityId
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
            },
            isPaywallBannerShown = state.isPaywallShown()
        )
    }

    private fun getSectionContent(
        sectionInfo: StudyPlanWidgetFeature.StudyPlanSectionInfo,
        state: StudyPlanWidgetFeature.State,
        currentActivityId: Long?
    ): SectionContent =
        if (sectionInfo.isExpanded) {
            when (sectionInfo.sectionContentStatus) {
                IDLE -> SectionContent.Collapsed
                ERROR -> SectionContent.Error
                FIRST_PAGE_LOADING,
                NEXT_PAGE_LOADING -> {
                    getContent(
                        state = state,
                        sectionInfo = sectionInfo,
                        currentActivityId = currentActivityId,
                        emptyActivitiesState = SectionContent.Loading
                    )
                }
                FIRST_PAGE_LOADED,
                ALL_PAGES_LOADED -> {
                    getContent(
                        state = state,
                        sectionInfo = sectionInfo,
                        currentActivityId = currentActivityId,
                        emptyActivitiesState = SectionContent.Error
                    )
                }
            }
        } else {
            SectionContent.Collapsed
        }

    private fun getContent(
        state: StudyPlanWidgetFeature.State,
        sectionInfo: StudyPlanWidgetFeature.StudyPlanSectionInfo,
        currentActivityId: Long?,
        emptyActivitiesState: SectionContent
    ): SectionContent {
        val sectionId = sectionInfo.studyPlanSection.id
        val loadedActivities = state.getLoadedSectionActivities(sectionId).toList()
        return if (loadedActivities.isEmpty()) {
            emptyActivitiesState
        } else {
            getContent(
                activities = loadedActivities,
                currentActivityId = currentActivityId,
                unlockedActivitiesCount = state.getUnlockedActivitiesCount(sectionId),
                nextPageLoadingState = when (sectionInfo.sectionContentStatus) {
                    IDLE,
                    ERROR,
                    FIRST_PAGE_LOADING,
                    ALL_PAGES_LOADED -> StudyPlanWidgetViewState.SectionContentPageLoadingState.IDLE
                    FIRST_PAGE_LOADED -> StudyPlanWidgetViewState.SectionContentPageLoadingState.LOAD_MORE
                    NEXT_PAGE_LOADING -> StudyPlanWidgetViewState.SectionContentPageLoadingState.LOADING
                },
                completedPageLoadingState = StudyPlanWidgetViewState.SectionContentPageLoadingState.LOAD_MORE
            )
        }
    }

    private fun getContent(
        activities: List<LearningActivity>,
        currentActivityId: Long?,
        unlockedActivitiesCount: Int?,
        nextPageLoadingState: StudyPlanWidgetViewState.SectionContentPageLoadingState,
        completedPageLoadingState: StudyPlanWidgetViewState.SectionContentPageLoadingState
    ): SectionContent.Content =
        SectionContent.Content(
            sectionItems = activities.mapIndexed { index, activity ->
                mapSectionItem(
                    activity = activity,
                    currentActivityId = currentActivityId,
                    isLocked = unlockedActivitiesCount != null && index + 1 > unlockedActivitiesCount
                )
            },
            nextPageLoadingState = nextPageLoadingState,
            completedPageLoadingState = completedPageLoadingState
        )

    private fun mapSectionItem(
        activity: LearningActivity,
        currentActivityId: Long?,
        isLocked: Boolean
    ): StudyPlanWidgetViewState.SectionItem =
        StudyPlanWidgetViewState.SectionItem(
            id = activity.id,
            title = LearningActivityTextsMapper.mapLearningActivityToTitle(activity),
            subtitle = LearningActivityTextsMapper.mapLearningActivityToSubtitle(activity),
            state = if (isLocked) {
                StudyPlanWidgetViewState.SectionItemState.LOCKED
            } else {
                when (activity.state) {
                    LearningActivityState.TODO -> if (activity.id == currentActivityId) {
                        StudyPlanWidgetViewState.SectionItemState.NEXT
                    } else {
                        StudyPlanWidgetViewState.SectionItemState.IDLE
                    }
                    LearningActivityState.SKIPPED -> StudyPlanWidgetViewState.SectionItemState.SKIPPED
                    LearningActivityState.COMPLETED -> StudyPlanWidgetViewState.SectionItemState.COMPLETED
                    null -> StudyPlanWidgetViewState.SectionItemState.IDLE
                }
            },
            isIdeRequired = activity.isIdeRequired,
            progress = activity.progressPercentage,
            formattedProgress = LearningActivityTextsMapper.mapLearningActivityToProgressString(activity),
            hypercoinsAward = activity.hypercoinsAward.takeIf { it > 0 }
        )

    private fun formatTopicsCount(completedTopicsCount: Int, topicsCount: Int): String? =
        if (topicsCount > 0) {
            "$completedTopicsCount / $topicsCount"
        } else {
            null
        }
}