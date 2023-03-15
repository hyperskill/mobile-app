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

        sealed interface Expanded : SectionContent

        object Loading : Expanded

        object Error : Expanded

        data class Content(
            val sectionItems: List<SectionItem>
        ) : Expanded
    }

    data class SectionItem(
        val id: Long
    )
}