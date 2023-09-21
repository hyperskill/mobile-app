package org.hyperskill.app.android.notification_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.notification.permission.NotificationPermissionDelegate
import org.hyperskill.app.android.notification_onboarding.ui.NotificationsOnboardingScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action.ViewAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel

class NotificationsOnboardingFragment : Fragment() {

    companion object {
        const val NOTIFICATIONS_ONBOARDING_FINISHED = "NOTIFICATIONS_ONBOARDING_FINISHED"
        fun newInstance(): NotificationsOnboardingFragment =
            NotificationsOnboardingFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val notificationsOnboardingViewModel: NotificationsOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private val notificationPermissionDelegate: NotificationPermissionDelegate =
        NotificationPermissionDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        notificationsOnboardingViewModel.handleActions(this, block = ::onAction)
    }

    private fun injectComponent() {
        val notificationOnboardingComponent =
            HyperskillApp.graph().buildPlatformNotificationOnboardingComponent()
        viewModelFactory = notificationOnboardingComponent.reduxViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    NotificationsOnboardingScreen(notificationsOnboardingViewModel)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsOnboardingViewModel.onNewMessage(Message.ViewedEventMessage)
    }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.CompleteNotificationOnboarding -> {
                requireAppRouter().sendResult(NOTIFICATIONS_ONBOARDING_FINISHED, Any())
            }
            ViewAction.RequestNotificationPermission -> {
                notificationPermissionDelegate.requestNotificationPermission { result ->
                    val isPermissionGranted = when (result) {
                        NotificationPermissionDelegate.Result.GRANTED -> true
                        NotificationPermissionDelegate.Result.DENIED,
                        NotificationPermissionDelegate.Result.DONT_ASK -> false
                    }
                    notificationsOnboardingViewModel.onNewMessage(
                        Message.NotificationPermissionRequestResult(isPermissionGranted)
                    )
                }
            }
        }
    }
}