package org.hyperskill.app.android.progress.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.progress.fragment.ProgressFragment

object ProgressScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        ProgressFragment.newInstance()
}