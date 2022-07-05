package org.hyperskill.app.android.home.view.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.home.view.ui.fragment.PlaceholderNewUserFragment

object PlaceholderNewUserScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        PlaceholderNewUserFragment.newInstance()
}