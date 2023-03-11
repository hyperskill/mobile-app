package org.hyperskill.app.android.step.view.delegate

import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.view.ui.navigation.requireMainRouter
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.step.view.dialog.TopicPracticeCompletedBottomSheet
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object StepDelegate  {
    fun init(errorBinding: ErrorNoConnectionWithButtonBinding, onNewMessage: (StepFeature.Message) -> Unit) {
        onNewMessage(StepFeature.Message.ViewedEventMessage)
        errorBinding.tryAgain.setOnClickListener {
            onNewMessage(StepFeature.Message.Initialize(forceUpdate = true))
        }
    }

    fun onAction(fragment: Fragment, action: StepFeature.Action.ViewAction) {
        when (action) {
            is StepFeature.Action.ViewAction.StepCompletionViewAction -> {
                when (val stepCompletionAction = action.viewAction) {
                    StepCompletionFeature.Action.ViewAction.NavigateTo.Back -> {
                        fragment.requireRouter().exit()
                    }

                    StepCompletionFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                        fragment.requireRouter().backTo(MainScreen)
                        fragment.parentFragmentManager.requireMainRouter().switch(HomeScreen)
                    }

                    is StepCompletionFeature.Action.ViewAction.ReloadStep -> {
                        fragment.requireRouter().replaceScreen(StepScreen(stepCompletionAction.stepRoute))
                    }

                    is StepCompletionFeature.Action.ViewAction.ShowStartPracticingError -> {
                        fragment.view?.snackbar(stepCompletionAction.message)
                    }

                    is StepCompletionFeature.Action.ViewAction.ShowTopicCompletedModal -> {
                        TopicPracticeCompletedBottomSheet
                            .newInstance(
                                stepCompletionAction.modalText,
                                stepCompletionAction.isNextStepAvailable
                            )
                            .showIfNotExists(
                                fragment.childFragmentManager,
                                TopicPracticeCompletedBottomSheet.Tag
                            )
                    }
                }
            }
        }
    }
}