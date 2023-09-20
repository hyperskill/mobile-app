package org.hyperskill.app.android.notification_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.android.notification_onboarding.ui.NotificationsOnboardingScreen

class NotificationsOnboardingFragment : Fragment() {

    companion object {
        fun newInstance(): NotificationsOnboardingFragment =
            NotificationsOnboardingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    NotificationsOnboardingScreen()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: send ViewedEventMessage
    }
}