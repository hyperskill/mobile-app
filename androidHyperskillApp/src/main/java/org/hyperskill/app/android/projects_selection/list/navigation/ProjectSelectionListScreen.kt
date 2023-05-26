package org.hyperskill.app.android.projects_selection.list.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.projects_selection.list.fragment.ProjectSelectionListFragment

class ProjectSelectionListScreen(
    private val trackId: Long
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        ProjectSelectionListFragment.newInstance(trackId)
}