package org.hyperskill.app.android.paywall.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.paywall.fragment.PaywallFragment
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource

class PaywallScreen(
    private val paywallTransitionSource: PaywallTransitionSource
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        PaywallFragment.newInstance(paywallTransitionSource)
}