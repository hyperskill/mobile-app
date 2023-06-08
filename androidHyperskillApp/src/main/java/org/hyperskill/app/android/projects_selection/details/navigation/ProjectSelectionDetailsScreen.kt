package org.hyperskill.app.android.projects_selection.details.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.projects_selection.details.fragment.ProjectSelectionDetailsFragment
import org.hyperskill.app.project_selection.details.injection.ProjectSelectionDetailsParams

class ProjectSelectionDetailsScreen(
    private val params: ProjectSelectionDetailsParams
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        ProjectSelectionDetailsFragment.newInstance(params)
}