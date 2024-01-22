package org.hyperskill.app.paywall.presentation

import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class PaywallViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, ViewAction>
) : ReduxViewModel<State, Message, ViewAction>(reduxViewContainer)