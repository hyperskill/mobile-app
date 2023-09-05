package org.hyperskill.app.android.step.view.delegate

import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.permission.NotificationPermissionDelegate
import org.hyperskill.app.android.step.view.dialog.TopicPracticeCompletedBottomSheet
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.step_quiz.view.dialog.RequestDailyStudyReminderDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class StepDelegate<TFragment>(
    private val fragment: TFragment,
    private val onRequestDailyStudyRemindersPermissionResult: (Boolean) -> Unit
) : RequestDailyStudyReminderDialogFragment.Callback
    where TFragment : Fragment,
          TFragment : RequestDailyStudyReminderDialogFragment.Callback {

    private val notificationPermissionDelegate: NotificationPermissionDelegate =
        NotificationPermissionDelegate(fragment)

    fun init(errorBinding: ErrorNoConnectionWithButtonBinding, onNewMessage: (StepFeature.Message) -> Unit) {
        onNewMessage(StepFeature.Message.ViewedEventMessage)
        errorBinding.tryAgain.setOnClickListener {
            onNewMessage(StepFeature.Message.Initialize(forceUpdate = true))
        }
    }

    fun onAction(
        mainScreenRouter: MainScreenRouter,
        action: StepFeature.Action.ViewAction
    ) {
        when (action) {
            is StepFeature.Action.ViewAction.StepCompletionViewAction -> {
                when (val stepCompletionAction = action.viewAction) {
                    StepCompletionFeature.Action.ViewAction.NavigateTo.Back -> {
                        fragment.requireRouter().exit()
                    }

                    StepCompletionFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                        fragment.requireRouter().backTo(MainScreen())
                        mainScreenRouter.switch(HomeScreen)
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
                    StepCompletionFeature.Action.ViewAction.RequestDailyStudyRemindersPermission -> {
                        RequestDailyStudyReminderDialogFragment.newInstance()
                            .showIfNotExists(fragment.childFragmentManager, RequestDailyStudyReminderDialogFragment.TAG)
                    }
                    is StepCompletionFeature.Action.ViewAction.ShowProblemOfDaySolvedModal -> {
                        CompletedStepOfTheDayDialogFragment
                            .newInstance(earnedGemsText = stepCompletionAction.earnedGemsText)
                            .showIfNotExists(fragment.childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
                    }
                }
            }
        }
    }

    override fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            notificationPermissionDelegate.requestNotificationPermission { result ->
                onNotificationPermissionResult(result)
            }
        } else {
            onRequestDailyStudyRemindersPermissionResult(false)
        }
    }

    private fun onNotificationPermissionResult(result: NotificationPermissionDelegate.Result) {
        when (result) {
            NotificationPermissionDelegate.Result.GRANTED -> {
                onNotificationPermissionGranted()
            }
            NotificationPermissionDelegate.Result.DENIED,
            NotificationPermissionDelegate.Result.DONT_ASK -> {
                onRequestDailyStudyRemindersPermissionResult(false)
            }
        }
    }

    private fun onNotificationPermissionGranted() {
        onRequestDailyStudyRemindersPermissionResult(true)
        val context = fragment.context
        if (context != null) {
            NotificationManagerCompat.from(context)
                .checkNotificationChannelAvailability(context, HyperskillNotificationChannel.DailyReminder)
        }
    }
}