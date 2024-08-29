package org.hyperskill.app.study_plan.widget.view.model

sealed interface StudyPlanWidgetViewState {
    data object Idle : StudyPlanWidgetViewState

    data object Loading : StudyPlanWidgetViewState

    data object Error : StudyPlanWidgetViewState

    data class Content(
        val isPaywallBannerShown: Boolean,
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
        data object Collapsed : SectionContent

        data object Loading : SectionContent

        data object Error : SectionContent

        data class Content(
            val sectionItems: List<SectionItem>,
            val nextPageLoadingState: SectionContentPageLoadingState,
            val completedPageLoadingState: SectionContentPageLoadingState
        ) : SectionContent
    }

    enum class SectionContentPageLoadingState {
        IDLE,
        LOAD_MORE,
        LOADING
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
    )

    enum class SectionItemState {
        IDLE,
        NEXT,
        SKIPPED,
        COMPLETED,
        LOCKED
    }
}