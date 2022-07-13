package org.hyperskill.app.android.track.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.track.view.fragment.TrackFragment

class TrackScreen(private val trackId: Long) : FragmentScreen {
    override val screenKey: String = Tabs.TRACK.name

    override fun createFragment(factory: FragmentFactory): Fragment =
        TrackFragment.newInstance(trackId)
}