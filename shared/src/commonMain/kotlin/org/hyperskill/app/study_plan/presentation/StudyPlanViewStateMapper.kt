package org.hyperskill.app.study_plan.presentation

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
            val contentStatus = state.sectionsContentStatuses.getOrElse(sectionInfo.studyPlanSection.id) {
                StudyPlanFeature.ContentStatus.IDLE
            }
            when (contentStatus) {
                StudyPlanFeature.ContentStatus.IDLE -> SectionContent.Collapsed
                StudyPlanFeature.ContentStatus.LOADING -> SectionContent.Loading
                StudyPlanFeature.ContentStatus.ERROR -> SectionContent.Error
                StudyPlanFeature.ContentStatus.LOADED -> {
                    val activities = state.activities[sectionInfo.studyPlanSection.id]
                    if (activities != null) {
                        SectionContent.Content(
                            sectionItems = activities.map { activity ->
                                StudyPlanViewState.SectionItem(activity.id)
                            }
                        )
                    } else {
                        SectionContent.Error
                    }
                }
            }
        } else {
            SectionContent.Collapsed
        }
}