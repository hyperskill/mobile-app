package org.hyperskill.app.android.step_quiz.view.delegate

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.notification.DailyStudyReminderNotificationDelegate
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.view.mapper.StepQuizUserPermissionRequestTextMapper
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class StepQuizViewActionsHandler(
    private val userPermissionRequestTextMapper: StepQuizUserPermissionRequestTextMapper,
    private val dailyStudyReminderNotificationDelegate: DailyStudyReminderNotificationDelegate,
    private val onNewMessage: (StepQuizFeature.Message) -> Unit
) {
    fun onNewAction(fragment: Fragment, action: StepQuizFeature.Action.ViewAction) {
        when (action) {
            is StepQuizFeature.Action.ViewAction.ShowNetworkError -> {
                fragment.view?.snackbar(messageRes = org.hyperskill.app.R.string.connection_error)
            }
            is StepQuizFeature.Action.ViewAction.NavigateTo.Back -> {
                fragment.requireRouter().exit()
            }
            is StepQuizFeature.Action.ViewAction.RequestUserPermission -> {
                when (action.userPermissionRequest) {
                    StepQuizUserPermissionRequest.RESET_CODE -> {
                        requestResetCodeActionPermission(fragment.requireContext(), action)
                    }
                    StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS -> {
                        requestSendDailyStudyRemindersPermission(fragment, action)
                    }
                }
            }
            is StepQuizFeature.Action.ViewAction.ShowProblemOfDaySolvedModal -> {
                CompletedStepOfTheDayDialogFragment
                    .newInstance(earnedGemsText = action.earnedGemsText)
                    .showIfNotExists(fragment.childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
            }
        }
    }

    private fun requestResetCodeActionPermission(context: Context, action: StepQuizFeature.Action.ViewAction.RequestUserPermission) {
        MaterialAlertDialogBuilder(context)
            .setTitle(userPermissionRequestTextMapper.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper.getMessage(action.userPermissionRequest))
            .setPositiveButton(org.hyperskill.app.R.string.yes) { _, _ ->
                onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = true
                    )
                )
            }
            .setNegativeButton(org.hyperskill.app.R.string.cancel) { _, _ ->
                onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = false
                    )
                )
            }
            .show()
    }

    private fun requestSendDailyStudyRemindersPermission(fragment: Fragment, action: StepQuizFeature.Action.ViewAction.RequestUserPermission) {
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(userPermissionRequestTextMapper.getTitle(action.userPermissionRequest))
            .setMessage(userPermissionRequestTextMapper.getMessage(action.userPermissionRequest))
            .setPositiveButton(org.hyperskill.app.R.string.ok) { dialog, _ ->
                onSendDailyStudyReminderAccepted(fragment, action.userPermissionRequest)
                dialog.dismiss()
            }
            .setNegativeButton(org.hyperskill.app.R.string.later) { dialog, _ ->
                onNewMessage(
                    StepQuizFeature.Message.RequestUserPermissionResult(
                        action.userPermissionRequest,
                        isGranted = false
                    )
                )
                dialog.dismiss()
            }
            .show()
    }

    private fun onSendDailyStudyReminderAccepted(
        fragment: Fragment,
        userPermissionRequest: StepQuizUserPermissionRequest
    ) {
        onNewMessage(
            StepQuizFeature.Message.RequestUserPermissionResult(
                userPermissionRequest,
                isGranted = true
            )
        )
        NotificationManagerCompat.from(fragment.requireContext()).checkNotificationChannelAvailability(
            fragment.requireContext(),
            HyperskillNotificationChannel.DailyReminder
        ) {
            if (fragment.isResumed) {
                fragment.view?.snackbar(org.hyperskill.app.R.string.common_error)
            }
        }
        dailyStudyReminderNotificationDelegate.scheduleDailyNotification()
    }
}