package org.hyperskill.app.step.presentation

import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepViewModel(
    reduxViewContainer: ReduxViewContainer<StepFeature.ViewState, StepFeature.Message, StepFeature.Action.ViewAction>
) : ReduxViewModel<StepFeature.ViewState, StepFeature.Message, StepFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(StepFeature.Message.Initialize())
    }

    fun onShareStreakBottomSheetShown(streak: Int) {
        onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalShownEventMessage(streak)
            )
        )
    }

    fun onShareStreakBottomSheetDismissed(streak: Int) {
        onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalHiddenEventMessage(streak)
            )
        )
    }

    fun onShareClick(streak: Int) {
        onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalShareClicked(streak)
            )
        )
    }

    fun onRefuseStreakSharingClick(streak: Int) {
        onNewMessage(
            StepFeature.Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalNoThanksClickedEventMessage(streak)
            )
        )
    }
}