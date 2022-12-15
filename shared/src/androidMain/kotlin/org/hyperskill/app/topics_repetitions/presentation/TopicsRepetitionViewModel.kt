package org.hyperskill.app.topics_repetitions.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class TopicsRepetitionViewModel(
    reduxViewContainer: ReduxViewContainer<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Message, TopicsRepetitionsFeature.Action.ViewAction>
) : ReduxViewModel<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Message, TopicsRepetitionsFeature.Action.ViewAction>(reduxViewContainer) {

    init {
        onNewMessage(
            TopicsRepetitionsFeature.Message.Initialize(
                forceUpdate = false
            )
        )
    }
}