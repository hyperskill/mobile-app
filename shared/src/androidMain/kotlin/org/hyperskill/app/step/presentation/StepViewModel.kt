package org.hyperskill.app.step.presentation

import org.hyperskill.app.step.domain.model.StepMenuAction
import org.hyperskill.app.step.presentation.StepFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class StepViewModel(
    reduxViewContainer: ReduxViewContainer<StepFeature.ViewState, Message, StepFeature.Action.ViewAction>
) : ReduxViewModel<StepFeature.ViewState, Message, StepFeature.Action.ViewAction>(reduxViewContainer) {
    init {
        onNewMessage(Message.Initialize())
    }

    fun onShareStreakBottomSheetShown(streak: Int) {
        onNewMessage(
            Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalShownEventMessage(streak)
            )
        )
    }

    fun onShareStreakBottomSheetDismissed(streak: Int) {
        onNewMessage(
            Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalHiddenEventMessage(streak)
            )
        )
    }

    fun onShareStreakClick(streak: Int) {
        onNewMessage(
            Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalShareClicked(streak)
            )
        )
    }

    fun onRefuseStreakSharingClick(streak: Int) {
        onNewMessage(
            Message.StepCompletionMessage(
                StepCompletionFeature.Message.ShareStreakModalNoThanksClickedEventMessage(streak)
            )
        )
    }

    fun onActionClick(action: StepMenuAction) {
        onNewMessage(
            when (action) {
                StepMenuAction.SHARE -> Message.ShareClicked
                StepMenuAction.REPORT -> Message.ReportClicked
                StepMenuAction.SKIP -> Message.ShareClicked
                StepMenuAction.OPEN_IN_WEB -> Message.OpenInWebClicked
            }
        )
    }
}