package org.hyperskill.app.android.stage_implementation.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.step.domain.model.StepMenuAction

object StageStepMenuDelegate {

    fun setup(
        menuHost: MenuHost,
        viewLifecycleOwner: LifecycleOwner,
        onActionClick: (StepMenuAction) -> Unit
    ) {
        menuHost.addMenuProvider(
            StageMenuProvider(onActionClick),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
}

private class StageMenuProvider(
    private val onActionClick: (StepMenuAction) -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.stage_menu, menu)

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.stageShare -> {
                onActionClick(StepMenuAction.SHARE)
                true
            }
            R.id.stageFeedback -> {
                onActionClick(StepMenuAction.REPORT)
                true
            }
            R.id.stageOpenInWeb -> {
                onActionClick(StepMenuAction.OPEN_IN_WEB)
                true
            }
            else -> false
        }
}