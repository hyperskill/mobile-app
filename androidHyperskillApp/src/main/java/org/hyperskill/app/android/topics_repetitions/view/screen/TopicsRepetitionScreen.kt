package org.hyperskill.app.android.topics_repetitions.view.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.topics_repetitions.view.fragment.TopicsRepetitionFragment

object TopicsRepetitionScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TopicsRepetitionFragment.newInstance()
}