package org.hyperskill.app.android.track_selection.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.track_selection.fragment.TrackSelectionListFragment

object TrackSelectionListScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TrackSelectionListFragment.newInstance()
}