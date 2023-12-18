package org.hyperskill.app.welcome.presentation

import org.hyperskill.app.welcome.presentation.WelcomeFeature.Action.ViewAction
import org.hyperskill.app.welcome.presentation.WelcomeFeature.Message
import org.hyperskill.app.welcome.presentation.WelcomeFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class WelcomeViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, ViewAction>
) : ReduxViewModel<State, Message, ViewAction>(reduxViewContainer)