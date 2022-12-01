package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionViewModel
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTopicsRepetitionComponentImpl(
    appGraph: AppGraph,
    private val recommendedRepetitionsCount: Int
) : PlatformTopicsRepetitionComponent {

    private val topicsRepetitionComponent: TopicsRepetitionsComponent = appGraph.buildTopicsRepetitionsComponent()

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                TopicsRepetitionViewModel::class.java to
                    {
                        TopicsRepetitionViewModel(
                            topicsRepetitionComponent.topicsRepetitionsFeature.wrapWithViewContainer(),
                            recommendedRepetitionsCount = recommendedRepetitionsCount
                        )
                    }
            )
        )

    override val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper =
        topicsRepetitionComponent.topicsRepetitionsViewDataMapper
}