package org.hyperskill.app.android.manage_subscription.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.manage_subscription.fragment.ManageSubscriptionFragment

object ManageSubscriptionScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        ManageSubscriptionFragment.newInstance()
}