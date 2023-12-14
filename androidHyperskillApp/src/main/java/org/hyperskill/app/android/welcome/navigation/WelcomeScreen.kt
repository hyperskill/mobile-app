package org.hyperskill.app.android.welcome.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.welcome.fragment.WelcomeFragment

object WelcomeScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        WelcomeFragment.newInstance()
}