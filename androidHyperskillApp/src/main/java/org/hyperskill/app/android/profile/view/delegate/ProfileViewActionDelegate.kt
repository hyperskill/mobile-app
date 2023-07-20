package org.hyperskill.app.android.profile.view.delegate

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.home.view.ui.screen.HomeScreen
import org.hyperskill.app.android.main.view.ui.navigation.MainScreenRouter
import org.hyperskill.app.android.profile.view.dialog.StreakFreezeDialogFragment
import org.hyperskill.app.android.view.base.ui.extension.snackbar
import org.hyperskill.app.profile.presentation.ProfileFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object ProfileViewActionDelegate {
    fun onAction(
        context: Context,
        view: View,
        fragmentManager: FragmentManager,
        mainScreenRouter: MainScreenRouter,
        action: ProfileFeature.Action.ViewAction
    ) {
        when (action) {
            is ProfileFeature.Action.ViewAction.OpenUrl ->
                context.openUrl(action.url)

            is ProfileFeature.Action.ViewAction.ShowGetMagicLinkError ->
                view.snackbar(org.hyperskill.app.R.string.common_error)

            is ProfileFeature.Action.ViewAction.ShowStreakFreezeModal -> {
                StreakFreezeDialogFragment.newInstance(action.streakFreezeState)
                    .showIfNotExists(fragmentManager, StreakFreezeDialogFragment.Tag)
            }
            ProfileFeature.Action.ViewAction.HideStreakFreezeModal -> {
                fragmentManager
                    .dismissDialogFragmentIfExists(StreakFreezeDialogFragment.Tag)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Loading -> {
                fragmentManager
                    .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Success -> {
                view.snackbar(org.hyperskill.app.R.string.streak_freeze_bought_success)
                fragmentManager
                    .dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
            }
            ProfileFeature.Action.ViewAction.ShowStreakFreezeBuyingStatus.Error ->
                view.snackbar(org.hyperskill.app.R.string.streak_freeze_bought_error)

            ProfileFeature.Action.ViewAction.NavigateTo.HomeScreen -> {
                mainScreenRouter.switch(HomeScreen)
            }
        }

    }
}