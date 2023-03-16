package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.LearningActivity
import org.hyperskill.app.study_plan.presentation.StudyPlanViewState.SectionContent

internal object StudyPlanViewStateMapper {
    fun map(state: StudyPlanFeature.State): StudyPlanViewState =
        when (state.sectionsStatus) {
            StudyPlanFeature.ContentStatus.IDLE -> StudyPlanViewState.Idle
            StudyPlanFeature.ContentStatus.LOADING -> StudyPlanViewState.Loading
            StudyPlanFeature.ContentStatus.ERROR -> StudyPlanViewState.Error
            StudyPlanFeature.ContentStatus.LOADED -> {
                StudyPlanViewState.Content(
                    sections = state.studyPlanSections.map { (_, sectionInfo) ->
                        val section = sectionInfo.studyPlanSection
                        StudyPlanViewState.Section(
                            id = section.id,
                            title = section.title,
                            subtitle = section.subtitle,
                            content = getSectionContent(sectionInfo, state)
                        )
                    }
                )
            }
        }

    private fun getSectionContent(
        sectionInfo: StudyPlanFeature.StudyPlanSectionInfo,
        state: StudyPlanFeature.State
    ): SectionContent =
        if (sectionInfo.isExpanded) {
            when (sectionInfo.contentStatus) {
                StudyPlanFeature.ContentStatus.IDLE -> SectionContent.Collapsed
                StudyPlanFeature.ContentStatus.LOADING -> {
                    val activities = getSectionActivities(state, sectionInfo.studyPlanSection.id)
                    if (activities.isNullOrEmpty()) {
                        SectionContent.Loading
                    } else {
                        getContent(activities)
                    }
                }
                StudyPlanFeature.ContentStatus.ERROR -> SectionContent.Error
                StudyPlanFeature.ContentStatus.LOADED -> {
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
                StudyPlanViewState.SectionItem(activity.id)
            }
        )

    private fun getSectionActivities(state: StudyPlanFeature.State, sectionId: Long): List<LearningActivity>? =
        state.activities[sectionId]?.toList()
}