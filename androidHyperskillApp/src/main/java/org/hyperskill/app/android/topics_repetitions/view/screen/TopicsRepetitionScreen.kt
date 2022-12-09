package org.hyperskill.app.android.topics_repetitions.view.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.topics_repetitions.view.fragment.TopicsRepetitionFragment

class TopicsRepetitionScreen(
    private val recommendedRepetitionsCount: Int
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TopicsRepetitionFragment.newInstance(recommendedRepetitionsCount)
}