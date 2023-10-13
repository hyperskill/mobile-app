package org.hyperskill.app.android.notification.click_handling.delegate

import androidx.fragment.app.FragmentManager
import com.github.terrakok.cicerone.Router
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.profile.view.dialog.BadgeEarnedDialogFragment
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.android.topics_repetitions.view.screen.TopicsRepetitionScreen
import org.hyperskill.app.notification.click_handling.presentation.NotificationClickHandlingFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

object NotificationClickHandlingDelegate {
    fun onNavigationViewAction(
        router: Router,
        navigateTo: NotificationClickHandlingFeature.Action.ViewAction.NavigateTo
    ) {
        when (navigateTo) {
            NotificationClickHandlingFeature.Action.ViewAction.NavigateTo.Profile ->
                router.newRootScreen(MainScreen(initialTab = Tabs.PROFILE))
            NotificationClickHandlingFeature.Action.ViewAction.NavigateTo.StudyPlan ->
                router.newRootScreen(MainScreen(initialTab = Tabs.STUDY_PLAN))
            is NotificationClickHandlingFeature.Action.ViewAction.NavigateTo.StepScreen ->
                router.newRootChain(
                    MainScreen(),
                    StepScreen(navigateTo.stepRoute)
                )
            NotificationClickHandlingFeature.Action.ViewAction.NavigateTo.TopicRepetition -> {
                router.newRootChain(
                    MainScreen(),
                    TopicsRepetitionScreen()
                )
            }
        }
    }

    fun onShowEarnedBadgeModalViewAction(
        showEarnedBadgeModal: NotificationClickHandlingFeature.Action.ViewAction.ShowEarnedBadgeModal,
        fragmentManager: FragmentManager
    ) {
        BadgeEarnedDialogFragment.newInstance(showEarnedBadgeModal.badge)
            .showIfNotExists(fragmentManager, BadgeEarnedDialogFragment.TAG)
    }
}