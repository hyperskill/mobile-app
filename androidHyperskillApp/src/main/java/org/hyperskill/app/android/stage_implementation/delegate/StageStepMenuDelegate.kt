package org.hyperskill.app.android.stage_implementation.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction

object StageStepMenuDelegate {

    fun setup(
        menuHost: MenuHost,
        viewLifecycleOwner: LifecycleOwner,
        onActionClick: (StepMenuSecondaryAction) -> Unit,
        onBackClick: () -> Unit
    ) {
        menuHost.addMenuProvider(
            StageMenuProvider(onActionClick, onBackClick),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
}

private class StageMenuProvider(
    private val onActionClick: (StepMenuSecondaryAction) -> Unit,
    private val onBackClick: () -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.stage_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.stageShare -> {
                onActionClick(StepMenuSecondaryAction.SHARE)
                true
            }
            R.id.stageFeedback -> {
                onActionClick(StepMenuSecondaryAction.REPORT)
                true
            }
            R.id.stageOpenInWeb -> {
                onActionClick(StepMenuSecondaryAction.OPEN_IN_WEB)
                true
            }
            android.R.id.home -> {
                onBackClick()
                true
            }
            else -> false
        }
}