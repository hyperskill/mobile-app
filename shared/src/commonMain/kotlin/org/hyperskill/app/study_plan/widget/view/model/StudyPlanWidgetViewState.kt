package org.hyperskill.app.study_plan.widget.view.model

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
        val subtitle: String?,
        val isCurrent: Boolean,
        val formattedTopicsCount: String?,
        val formattedTimeToComplete: String?,
        val content: SectionContent
    ) {
        val isCurrentBadgeShown: Boolean
            get() = isCurrent && content is SectionContent.Collapsed
    }

    sealed interface SectionContent {
        object Collapsed : SectionContent

        object Loading : SectionContent

        object Error : SectionContent

        data class Content(
            val sectionItems: List<SectionItem>
        ) : SectionContent
    }

    data class SectionItem(
        val id: Long,
        val title: String,
        val subtitle: String?,
        val state: SectionItemState,
        val isIdeRequired: Boolean,
        val progress: Int,
        val formattedProgress: String?,
        val hypercoinsAward: Int?
    ) {
        val isClickable: Boolean
            get() = state == SectionItemState.NEXT || state == SectionItemState.IDLE
    }

    enum class SectionItemState {
        IDLE,
        NEXT,
        SKIPPED,
        COMPLETED
    }
}