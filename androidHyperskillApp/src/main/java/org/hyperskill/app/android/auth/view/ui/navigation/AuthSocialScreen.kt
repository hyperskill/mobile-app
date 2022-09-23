package org.hyperskill.app.android.auth.view.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.auth.view.ui.fragment.AuthSocialFragment

object AuthSocialScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        AuthSocialFragment.newInstance()
}