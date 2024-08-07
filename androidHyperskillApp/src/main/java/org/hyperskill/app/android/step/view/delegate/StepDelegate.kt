package org.hyperskill.app.android.step.view.delegate

import android.content.ActivityNotFoundException
import android.content.Context
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import co.touchlab.kermit.Logger
import org.hyperskill.app.R
import org.hyperskill.app.android.comments.fragment.CommentsDialogFragment
import org.hyperskill.app.android.core.extensions.ShareUtils
import org.hyperskill.app.android.core.extensions.launchUrlInCustomTabs
import org.hyperskill.app.android.core.extensions.performConfirmHapticFeedback
import org.hyperskill.app.android.core.view.ui.fragment.parentOfType
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.ErrorNoConnectionWithButtonBinding
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.android.request_review.dialog.RequestReviewDialogFragment
import org.hyperskill.app.android.share_streak.fragment.ShareStreakDialogFragment
import org.hyperskill.app.android.step.view.model.StepHost
import org.hyperskill.app.android.step.view.navigation.requireStepRouter
import org.hyperskill.app.android.step_feedback.dialog.StepFeedbackDialogFragment
import org.hyperskill.app.android.step_quiz.view.dialog.CompletedStepOfTheDayDialogFragment
import org.hyperskill.app.android.topic_completion.fragment.TopicCompletedDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object StepDelegate {

    fun init(
        errorBinding: ErrorNoConnectionWithButtonBinding,
        lifecycle: Lifecycle,
        onNewMessage: (StepFeature.Message) -> Unit
    ) {
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_RESUME -> onNewMessage(StepFeature.Message.ScreenShowed)
                    Lifecycle.Event.ON_PAUSE -> onNewMessage(StepFeature.Message.ScreenHidden)
                    else -> {
                        // no op
                    }
                }
            }
        )
        errorBinding.tryAgain.setOnClickListener {
            onNewMessage(StepFeature.Message.Initialize(forceUpdate = true))
        }
    }

    fun <TFragment> onAction(
        fragment: TFragment,
        mainScreenRouter: MainScreenRouter,
        action: StepFeature.Action.ViewAction,
        logger: Logger
    ) where TFragment : Fragment,
            TFragment : ShareStreakDialogFragment.Callback,
            TFragment : TopicCompletedDialogFragment.Callback {
        when (action) {
            is StepFeature.Action.ViewAction.StepCompletionViewAction ->
                handleStepCompletionViewAction(fragment, mainScreenRouter, logger, action)
            is StepFeature.Action.ViewAction.ShareStepLink ->
                shareLink(fragment, action.link, logger)
            is StepFeature.Action.ViewAction.ShowFeedbackModal -> {
                StepFeedbackDialogFragment
                    .newInstance(action.stepRoute)
                    .showIfNotExists(
                        manager = fragment.childFragmentManager,
                        tag = StepFeedbackDialogFragment.TAG
                    )
            }
            is StepFeature.Action.ViewAction.OpenUrl -> {
                fragment.launchUrlInCustomTabs(action.url, logger)
            }
            StepFeature.Action.ViewAction.ShowLoadingError -> {
                showToast(fragment.requireContext(), R.string.common_error)
            }
            is StepFeature.Action.ViewAction.ReloadStep -> {
                reloadStep(fragment, action.stepRoute)
            }
            StepFeature.Action.ViewAction.ShowCantSkipError -> {
                showToast(fragment.requireContext(), R.string.step_skip_failed_message)
            }
            is StepFeature.Action.ViewAction.StepToolbarViewAction -> {
                // no op
            }
            is StepFeature.Action.ViewAction.NavigateTo.CommentsScreen -> {
                CommentsDialogFragment
                    .newInstance(action.params)
                    .showIfNotExists(
                        fragment.childFragmentManager,
                        CommentsDialogFragment.TAG
                    )
            }
        }
    }

    private fun handleStepCompletionViewAction(
        fragment: Fragment,
        mainScreenRouter: MainScreenRouter,
        logger: Logger,
        viewAction: StepFeature.Action.ViewAction.StepCompletionViewAction
    ) {
        when (val stepCompletionAction = viewAction.viewAction) {
            StepCompletionFeature.Action.ViewAction.NavigateTo.Back -> {
                fragment.requireStepRouter().exit()
            }

            StepCompletionFeature.Action.ViewAction.NavigateTo.StudyPlan -> {
                fragment.requireRouter().backTo(MainScreen(Tabs.STUDY_PLAN))
                mainScreenRouter.switch(Tabs.STUDY_PLAN)
            }

            is StepCompletionFeature.Action.ViewAction.NavigateTo.Paywall ->
                fragment.requireRouter().navigateTo(PaywallScreen(stepCompletionAction.paywallTransitionSource))

            is StepCompletionFeature.Action.ViewAction.ReloadStep ->
                reloadStep(fragment, stepCompletionAction.stepRoute)

            is StepCompletionFeature.Action.ViewAction.ShowStartPracticingError -> {
                fragment.view?.snackbar(stepCompletionAction.message)
            }

            is StepCompletionFeature.Action.ViewAction.ShowTopicCompletedModal -> {
                TopicCompletedDialogFragment
                    .newInstance(stepCompletionAction.params)
                    .showIfNotExists(
                        fragment.childFragmentManager,
                        TopicCompletedDialogFragment.TAG
                    )
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
                shareStreak(fragment, stepCompletionAction.streak, logger)
            }
            is StepCompletionFeature.Action.ViewAction.ShowRequestUserReviewModal ->
                RequestReviewDialogFragment
                    .newInstance(stepCompletionAction.stepRoute)
                    .showIfNotExists(
                        manager = fragment.childFragmentManager,
                        tag = RequestReviewDialogFragment.TAG
                    )
            StepCompletionFeature.Action.ViewAction.HapticFeedback.TopicCompleted -> {
                fragment.requireView().performConfirmHapticFeedback(HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING)
            }
        }
    }

    private fun reloadStep(fragment: Fragment, stepRoute: StepRoute) {
        fragment.parentOfType(StepHost::class.java)?.reloadStep(stepRoute)
    }

    private fun showToast(context: Context, @StringRes stringRes: Int) {
        Toast
            .makeText(context, stringRes, Toast.LENGTH_SHORT)
            .show()
    }

    @DrawableRes
    private fun getShareStreakDrawableRes(streak: Int): Int =
        when (streak) {
            5 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_5
            10 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_10
            25 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_25
            50 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_50
            100 -> org.hyperskill.app.android.R.drawable.img_share_streak_day_100
            else -> org.hyperskill.app.android.R.drawable.img_share_streak_day_5
        }

    private fun getShareStreakText(context: Context): String {
        val title = context.getString(R.string.share_streak_sharing_text)
        val link = context.getString(R.string.share_streak_sharing_url)
        return "$title\n$link"
    }

    private fun shareStreak(
        fragment: Fragment,
        streak: Int,
        logger: Logger
    ) {
        val shareIntent = ShareUtils.getShareDrawableIntent(
            fragment.requireContext(),
            getShareStreakDrawableRes(streak),
            text = getShareStreakText(fragment.requireContext()),
            title = fragment.requireContext().getString(R.string.share_streak_modal_title)
        )
        try {
            fragment.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            logger.e { "Unable to share streak. Activity not found!" }
        }
    }

    private fun shareLink(
        fragment: Fragment,
        link: String,
        logger: Logger
    ) {
        val shareIntent = ShareUtils.getShareTextIntent(link)
        try {
            fragment.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            logger.e { "Unable to share step link. Activity not found!" }
        }
    }
}