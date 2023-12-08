package org.hyperskill.app.android.leaderboard.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.leaderboard.fragment.LeaderboardFragment
import org.hyperskill.app.android.main.view.ui.navigation.Tabs

object LeaderboardScreen : FragmentScreen {

    override val screenKey: String
        get() = Tabs.LEADERBOARD.name
    override fun createFragment(factory: FragmentFactory): Fragment =
        LeaderboardFragment.newInstance()
}