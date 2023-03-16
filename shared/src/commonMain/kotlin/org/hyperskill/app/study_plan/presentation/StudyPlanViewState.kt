package org.hyperskill.app.study_plan.presentation

sealed interface StudyPlanViewState {
    object Idle : StudyPlanViewState

    object Loading : StudyPlanViewState

    object Error : StudyPlanViewState

    data class Content(
        val sections: List<Section>
    ) : StudyPlanViewState

    data class Section(
        val id: Long,
        val title: String,
        val subtitle: String,
        val content: SectionContent
    )

    sealed interface SectionContent {
        object Collapsed : SectionContent

        object Loading : SectionContent

        object Error : SectionContent

        data class Content(
            val sectionItems: List<SectionItem>
        ) : SectionContent
    }

    data class SectionItem(
        val id: Long
    )
}