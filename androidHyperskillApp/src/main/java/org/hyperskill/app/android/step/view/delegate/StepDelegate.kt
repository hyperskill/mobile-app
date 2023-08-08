package org.hyperskill.app.android.step.view.delegate

import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.HyperskillApp
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
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class StepDelegate(
    private val fragment: Fragment,
    private val rootView: View,
    private val onRequestDailyStudyRemindersPermissionResult: (Boolean) -> Unit
) {
    private val notificationPermissionDelegate: NotificationPermissionDelegate =
        NotificationPermissionDelegate(fragment)

    private val platformNotificationComponent =
        HyperskillApp.graph().platformLocalNotificationComponent

    fun init(errorBinding: ErrorNoConnectionWithButtonBinding, onNewMessage: (StepFeature.Message) -> Unit) {
        onNewMessage(StepFeature.Message.ViewedEventMessage)
        errorBinding.tryAgain.setOnClickListener {
            onNewMessage(StepFeature.Message.Initialize(forceUpdate = true))
        }
    }

    fun onAction(
        mainScreenRouter: MainScreenRouter,
        action: StepFeature.Action.ViewAction,
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
                        requestSendDailyStudyRemindersPermission()
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

    private fun requestSendDailyStudyRemindersPermission() {
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(org.hyperskill.app.R.string.after_daily_step_completed_dialog_title)
            .setMessage(org.hyperskill.app.R.string.after_daily_step_completed_dialog_text)
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                notificationPermissionDelegate.requestNotificationPermission { result ->
                    dialog.dismiss()
                    if (result == NotificationPermissionDelegate.Result.GRANTED) {
                        onNotificationPermissionGranted()
                    }
                }
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                onRequestDailyStudyRemindersPermissionResult(false)
                dialog.dismiss()
            }
            .show()
    }

    private fun onNotificationPermissionGranted() {
        onRequestDailyStudyRemindersPermissionResult(true)
        NotificationManagerCompat.from(fragment.requireContext()).checkNotificationChannelAvailability(
            fragment.requireContext(),
            HyperskillNotificationChannel.DailyReminder
        ) {
            if (fragment.isResumed) {
                rootView.snackbar(org.hyperskill.app.R.string.common_error)
            }
        }
        platformNotificationComponent.dailyStudyReminderNotificationDelegate.scheduleDailyNotification()
    }
}