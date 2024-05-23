package org.hyperskill.app.android.topic_completion.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.topic_completion.fragment.TopicCompletionFragment

class TopicCompletionScreen : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        TopicCompletionFragment.newInstance()
}