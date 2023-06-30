package org.hyperskill.app.android.track_progress.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.track_progress.fragment.ProgressFragment

object ProgressScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        ProgressFragment.newInstance()
}