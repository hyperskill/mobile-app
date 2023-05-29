package org.hyperskill.app.android.placeholder_new_user.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.placeholder_new_user.fragment.PlaceholderNewUserFragment

object PlaceholderNewUserScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        PlaceholderNewUserFragment.newInstance()
}