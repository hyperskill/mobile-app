package org.hyperskill.app.study_plan.widget.view

sealed interface StudyPlanWidgetViewState {
    object Idle : StudyPlanWidgetViewState

    object Loading : StudyPlanWidgetViewState

    object Error : StudyPlanWidgetViewState

    data class Content(
        val sections: List<Section>
    ) : StudyPlanWidgetViewState

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