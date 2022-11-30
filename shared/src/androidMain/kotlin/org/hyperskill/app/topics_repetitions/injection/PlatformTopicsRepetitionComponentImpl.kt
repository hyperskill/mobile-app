package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformTopicsRepetitionComponentImpl(private val appGraph: AppGraph) : PlatformTopicsRepetitionComponent {
    override val reduxViewModel: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                TopicsRepetitionViewModel::class.java to
                    {
                        TopicsRepetitionViewModel(
                            appGraph
                                .buildTopicsRepetitionsComponent()
                                .topicsRepetitionsFeature
                                .wrapWithViewContainer()
                        )
                    }
            )
        )
}