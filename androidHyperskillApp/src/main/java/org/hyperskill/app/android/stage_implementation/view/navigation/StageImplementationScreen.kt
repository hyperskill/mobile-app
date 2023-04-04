package org.hyperskill.app.android.stage_implementation.view.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen
import org.hyperskill.app.android.stage_implementation.view.fragment.StageImplementationFragment

class StageImplementationScreen(
    private val projectId: Long,
    private val stageId: Long
) : FragmentScreen {
    override fun createFragment(factory: FragmentFactory): Fragment =
        StageImplementationFragment.newInstance(projectId = projectId, stageId = stageId)
}