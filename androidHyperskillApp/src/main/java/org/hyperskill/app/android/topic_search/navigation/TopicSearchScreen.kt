package org.hyperskill.app.android.topic_search.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.topic_search.fragment.TopicSearchFragment

object TopicSearchScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TopicSearchFragment.newInstance()
}