package org.hyperskill.app.android.track_selection.details.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.track_selection.details.fragment.TrackSelectionDetailsFragment
import org.hyperskill.app.track_selection.details.injection.TrackSelectionDetailsParams

class TrackSelectionDetailsScreen(private val params: TrackSelectionDetailsParams) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TrackSelectionDetailsFragment.newInstance(params)
}