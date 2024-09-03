package org.hyperskill.app.study_plan.widget.view.mapper

import kotlin.math.roundToLong
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.view.mapper.LearningActivityTextsMapper
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.ContentStatus
import org.hyperskill.app.study_plan.widget.presentation.StudyPlanWidgetFeature.PageContentStatus
import org.hyperskill.app.study_plan.widget.presentation.getCurrentActivity
import org.hyperskill.app.study_plan.widget.presentation.getCurrentSection
import org.hyperskill.app.study_plan.widget.presentation.getLoadedSectionActivities
import org.hyperskill.app.study_plan.widget.presentation.getUnlockedActivitiesCount
import org.hyperskill.app.study_plan.widget.presentation.isPaywallShown
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState.SectionContent
import org.hyperskill.app.study_plan.widget.view.model.StudyPlanWidgetViewState.SectionContentPageLoadingState

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
            when (sectionInfo.mainPageContentStatus) {
                ContentStatus.IDLE -> SectionContent.Collapsed
                ContentStatus.ERROR -> SectionContent.Error
                ContentStatus.LOADING -> getContent(
                    state = state,
                    sectionInfo = sectionInfo,
                    currentActivityId = currentActivityId,
                    emptyActivitiesState = SectionContent.Loading
                )
                ContentStatus.LOADED -> getContent(
                    state = state,
                    sectionInfo = sectionInfo,
                    currentActivityId = currentActivityId,
                    emptyActivitiesState = SectionContent.Error
                )
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
            val unlockedActivitiesCount = state.getUnlockedActivitiesCount(sectionId)
            SectionContent.Content(
                sectionItems = loadedActivities.mapIndexed { index, activity ->
                    mapSectionItem(
                        activity = activity,
                        currentActivityId = currentActivityId,
                        isLocked = unlockedActivitiesCount != null && index + 1 > unlockedActivitiesCount
                    )
                },
                nextPageLoadingState = mapPageContentStatusToViewState(sectionInfo.nextPageContentStatus),
                completedPageLoadingState = mapPageContentStatusToViewState(sectionInfo.completedPageContentStatus)
            )
        }
    }

    private fun mapPageContentStatusToViewState(pageContentStatus: PageContentStatus): SectionContentPageLoadingState =
        when (pageContentStatus) {
            PageContentStatus.IDLE,
            PageContentStatus.ERROR,
            PageContentStatus.LOADED -> SectionContentPageLoadingState.HIDDEN
            PageContentStatus.AWAIT_LOADING -> SectionContentPageLoadingState.LOAD_MORE
            PageContentStatus.LOADING -> SectionContentPageLoadingState.LOADING
        }

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