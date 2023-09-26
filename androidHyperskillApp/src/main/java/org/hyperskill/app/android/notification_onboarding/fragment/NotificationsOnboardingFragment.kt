package org.hyperskill.app.android.notification_onboarding.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.startAppNotificationSettingsIntent
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.notification.permission.NotificationPermissionDelegate
import org.hyperskill.app.android.notification_onboarding.ui.NotificationsOnboardingScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action.ViewAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel
import ru.nobird.android.view.base.ui.extension.argument

class NotificationsOnboardingFragment : Fragment() {

    companion object {
        const val NOTIFICATIONS_ONBOARDING_FINISHED = "NOTIFICATIONS_ONBOARDING_FINISHED"
        fun newInstance(): NotificationsOnboardingFragment =
            NotificationsOnboardingFragment().apply {
                waitForNotificationPermissionResult = false
            }
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val notificationsOnboardingViewModel: NotificationsOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private var notificationPermissionDelegate: NotificationPermissionDelegate? = null

    private var waitForNotificationPermissionResult: Boolean by argument()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        notificationsOnboardingViewModel.handleActions(this, block = ::onAction)
        notificationPermissionDelegate =
            NotificationPermissionDelegate(this, ::onNotificationPermissionRequestResult)
    }

    private fun injectComponent() {
        val notificationOnboardingComponent =
            HyperskillApp.graph().buildPlatformNotificationOnboardingComponent()
        viewModelFactory = notificationOnboardingComponent.reduxViewModelFactory
    }

    @SuppressLint("InlinedApi")
    override fun onResume() {
        super.onResume()
        if (waitForNotificationPermissionResult) {
            waitForNotificationPermissionResult = false
            notificationsOnboardingViewModel.onNewMessage(
                Message.NotificationPermissionRequestResult(
                    isPermissionGranted = ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme(
                    setTextColors = true,
                    setDefaultFontFamily = true
                ) {
                    NotificationsOnboardingScreen(notificationsOnboardingViewModel)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationsOnboardingViewModel.onNewMessage(Message.ViewedEventMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationPermissionDelegate = null
    }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.CompleteNotificationOnboarding -> {
                requireAppRouter().sendResult(NOTIFICATIONS_ONBOARDING_FINISHED, Any())
            }
            ViewAction.RequestNotificationPermission -> {
                notificationPermissionDelegate?.requestNotificationPermission()
            }
        }
    }

    private fun onNotificationPermissionRequestResult(result: NotificationPermissionDelegate.Result) {
        when (result) {
            NotificationPermissionDelegate.Result.GRANTED ->
                notificationsOnboardingViewModel.onNewMessage(
                    Message.NotificationPermissionRequestResult(true)
                )
            NotificationPermissionDelegate.Result.DENIED ->
                notificationsOnboardingViewModel.onNewMessage(
                    Message.NotificationPermissionRequestResult(false)
                )
            NotificationPermissionDelegate.Result.DONT_ASK -> {
                this.waitForNotificationPermissionResult = true
                requireContext().startAppNotificationSettingsIntent {
                    this.waitForNotificationPermissionResult = false
                    if (isResumed) {
                        Toast.makeText(
                            requireContext(),
                            org.hyperskill.app.R.string.common_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}