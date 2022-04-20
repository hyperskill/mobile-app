package org.hyperskill.app.android.auth.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.auth.view.ui.fragment.AuthFragment

object AuthScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        AuthFragment.newInstance()
}