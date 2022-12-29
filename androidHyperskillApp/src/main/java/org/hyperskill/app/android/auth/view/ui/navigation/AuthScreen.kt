package org.hyperskill.app.android.auth.view.ui.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.auth.view.ui.fragment.AuthFragment

class AuthScreen(private val isInSignUpMode: Boolean = false) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        AuthFragment.newInstance(isInSignUpMode)
}