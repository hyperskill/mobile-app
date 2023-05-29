package org.hyperskill.app.android.track_selection.list.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.track_selection.list.fragment.TrackSelectionListFragment
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams

class TrackSelectionListScreen(
    private val params: TrackSelectionListParams
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TrackSelectionListFragment.newInstance(params)
}