package org.hyperskill.app.android.step.presentation

import org.hyperskill.app.step.presentation.StepFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepViewModel(
    reduxViewContainer: ReduxViewContainer<StepFeature.State, StepFeature.Message, StepFeature.Action.ViewAction>
) : ReduxViewModel<StepFeature.State, StepFeature.Message, StepFeature.Action.ViewAction>(reduxViewContainer)