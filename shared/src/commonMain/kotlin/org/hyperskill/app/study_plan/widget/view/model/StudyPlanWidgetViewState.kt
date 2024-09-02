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
        ) : SectionContent {
            // TODO: ALTAPPS-1334 remove this property and use nextPageLoadingState directly
            @Deprecated("Is used only for iOS compatibility")
            val isLoadAllTopicsButtonShown: Boolean
                get() = nextPageLoadingState == SectionContentPageLoadingState.LOAD_MORE

            // TODO: ALTAPPS-1334 remove this property and use completedPageLoadingState directly
            @Deprecated("Is used only for iOS compatibility")
            val isNextPageLoadingShowed: Boolean
                get() = nextPageLoadingState == SectionContentPageLoadingState.LOADING
        }
    }

    enum class SectionContentPageLoadingState {
        HIDDEN,
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