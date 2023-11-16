package org.hyperskill.app.android.step.view.delegate

import android.content.ActivityNotFoundException
import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.ShareUtils
import org.hyperskill.app.android.core.extensions.checkNotificationChannelAvailability
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.notification.model.HyperskillNotificationChannel
import org.hyperskill.app.android.notification.permission.NotificationPermissionDelegate
import org.hyperskill.app.android.share_streak.fragment.ShareStreakDialogFragment
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
          TFragment : RequestDailyStudyReminderDialogFragment.Callback,
          TFragment : ShareStreakDialogFragment.Callback {

    private val notificationPermissionDelegate: NotificationPermissionDelegate =
        NotificationPermissionDelegate(fragment, ::onNotificationPermissionResult)

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

                    StepCompletionFeature.Action.ViewAction.NavigateTo.StudyPlan -> {
                        fragment.requireRouter().backTo(MainScreen(Tabs.STUDY_PLAN))
                        mainScreenRouter.switch(Tabs.STUDY_PLAN)
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
                            .newInstance(
                                earnedGemsText = stepCompletionAction.earnedGemsText,
                                shareStreakData = stepCompletionAction.shareStreakData
                            )
                            .showIfNotExists(fragment.childFragmentManager, CompletedStepOfTheDayDialogFragment.TAG)
                    }
                    is StepCompletionFeature.Action.ViewAction.ShowShareStreakModal -> {
                        ShareStreakDialogFragment
                            .newInstance(
                                streak = stepCompletionAction.streak,
                                imageRes = getShareStreakDrawableRes(stepCompletionAction.streak)
                            )
                            .showIfNotExists(fragment.childFragmentManager, ShareStreakDialogFragment.TAG)
                    }
                    is StepCompletionFeature.Action.ViewAction.ShowShareStreakSystemModal -> {
                        shareStreak(stepCompletionAction.streak)
                    }
                }
            }
        }
    }

    override fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            notificationPermissionDelegate.requestNotificationPermission()
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

    @DrawableRes
    private fun getShareStreakDrawableRes(streak: Int): Int =
        when (streak) {
            1 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_1
            5 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_5
            10 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_10
            25 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_25
            50 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_50
            100 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_100
            else -> org.hyperskill.app.android.R.drawable.img_share_streak_day_1
        }

    private fun getShareStreakText(context: Context): String {
        val title = context.getString(R.string.share_streak_sharing_text)
        val link = context.getString(R.string.share_streak_sharing_url)
        return "$title\n$link"
    }

    private fun shareStreak(streak: Int) {
        val shareIntent = ShareUtils.getShareDrawableIntent(
            fragment.requireContext(),
            getShareStreakDrawableRes(streak),
            text = getShareStreakText(fragment.requireContext()),
            title = fragment.requireContext().getString(R.string.share_streak_modal_title)
        )
        try {
            fragment.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e("StepDelegate", "Unable to share streak. Activity not found!")
        }
    }
}