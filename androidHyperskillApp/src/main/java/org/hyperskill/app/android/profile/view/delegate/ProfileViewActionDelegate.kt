package org.hyperskill.app.android.profile.view.delegate

import androidx.fragment.app.Fragment
import co.touchlab.kermit.Logger
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.main.view.ui.navigation.switch
import org.hyperskill.app.android.profile.view.dialog.BadgeDetailsDialogFragment
import org.hyperskill.app.android.profile.view.dialog.StreakFreezeDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.profile.presentation.ProfileFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object ProfileViewActionDelegate {
    fun onAction(
        fragment: Fragment,
        mainScreenRouter: MainScreenRouter,
        logger: Logger,
        action: ProfileFeature.Action.ViewAction
    ) {
        when (action) {
            is ProfileFeature.Action.ViewAction.OpenUrl ->
                fragment.requireContext().openUrl(action.url, logger)

            is ProfileFeature.Action.ViewAction.ShowError ->
                fragment.requireView().snackbar(org.hyperskill.app.R.string.common_error)

            is ProfileFeature.Action.ViewAction.ShowStreakFreezeModal -> {
                StreakFreezeDialogFragment.newInstance(action.streakFreezeState)
                    .showIfNotExists(fragment.childFragmentManager, StreakFreezeDialogFragment.Tag)
            }
            ProfileFeature.Action.ViewAction.HideStreakFreezeModal -> {
                fragment.childFragmentManager
                    .dismissDialogFragmentIfExists(StreakFreezeDialogFragment.Tag)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Loading -> {
                fragment.childFragmentManager
                    .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Success -> {
                fragment.requireView().snackbar(org.hyperskill.app.R.string.streak_freeze_bought_success)
                fragment.childFragmentManager
                    .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Error ->
                fragment.requireView().snackbar(org.hyperskill.app.R.string.streak_freeze_bought_error)

            ProfileFeature.Action.ViewAction.NavigateTo.StudyPlan -> {
                mainScreenRouter.switch(Tabs.STUDY_PLAN)
            }
            is ProfileFeature.Action.ViewAction.ShowBadgeDetailsModal -> {
                BadgeDetailsDialogFragment.newInstance(action.details)
                    .showIfNotExists(fragment.childFragmentManager, BadgeDetailsDialogFragment.TAG)
            }
        }
    }
}