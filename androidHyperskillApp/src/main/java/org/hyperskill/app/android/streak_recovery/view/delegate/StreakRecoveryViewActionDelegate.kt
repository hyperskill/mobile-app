package org.hyperskill.app.android.streak_recovery.view.delegate

import android.view.View
import androidx.fragment.app.FragmentManager
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.streak_recovery.view.fragment.StreakRecoveryDialogFragment
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists
import ru.nobird.android.view.base.ui.extension.snackbar

object StreakRecoveryViewActionDelegate {
    fun handleViewAction(
        fragmentManager: FragmentManager,
        rootView: View,
        viewAction: StreakRecoveryFeature.Action.ViewAction
    ) {
        when (viewAction) {
            is StreakRecoveryFeature.Action.ViewAction.ShowRecoveryStreakModal -> {
                StreakRecoveryDialogFragment
                    .newInstance(
                        StreakRecoveryDialogFragment.Params(
                            recoveryPriceAmountLabel = viewAction.recoveryPriceAmountLabel,
                            recoveryPriceGemsLabel = viewAction.recoveryPriceGemsLabel,
                            modalText = viewAction.modalText
                        )
                    ).showIfNotExists(fragmentManager, StreakRecoveryDialogFragment.TAG)
            }
            StreakRecoveryFeature.Action.ViewAction.HideStreakRecoveryModal -> {
                fragmentManager
                    .dismissDialogFragmentIfExists(StreakRecoveryDialogFragment.TAG)
            }
            StreakRecoveryFeature.Action.ViewAction.ShowNetworkRequestStatus.Loading -> {
                LoadingProgressDialogFragment.newInstance()
                    .showIfNotExists(fragmentManager, LoadingProgressDialogFragment.TAG)
            }
            StreakRecoveryFeature.Action.ViewAction.ShowNetworkRequestStatus.Success -> {
                fragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
                rootView.snackbar(org.hyperskill.app.R.string.streak_recovery_modal_success)
            }
            StreakRecoveryFeature.Action.ViewAction.ShowNetworkRequestStatus.Error -> {
                fragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
                rootView.snackbar(org.hyperskill.app.R.string.streak_recovery_modal_error)
            }
        }
    }
}