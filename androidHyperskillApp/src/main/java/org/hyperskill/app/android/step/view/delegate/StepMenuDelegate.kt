package org.hyperskill.app.android.step.view.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step.view.model.StepMenuState

class StepMenuDelegate(
    viewLifecycleOwner: LifecycleOwner,
    private val menuHost: MenuHost,
    private val onTheoryClick: () -> Unit,
    private val onTheoryFeedbackClick: () -> Unit
) : MenuProvider {

    private var menuState: StepMenuState? = null

    init {
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    fun renderMenu(menuState: StepMenuState) {
        if (this.menuState != menuState) {
            this.menuState = menuState
            menuHost.invalidateMenu()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        when (menuState) {
            is StepMenuState.OpenTheory,
            StepMenuState.TheoryFeedback -> {
                menuInflater.inflate(R.menu.step_menu, menu)
            }
            null -> {
                // no op
            }
        }
    }

    override fun onPrepareMenu(menu: Menu) {
        val theoryItem: MenuItem? = menu.findItem(R.id.theory)
        val theoryFeedbackItem: MenuItem? = menu.findItem(R.id.theoryFeedback)

        val state = menuState

        theoryFeedbackItem?.isVisible = state is StepMenuState.TheoryFeedback

        val isTheoryItemVisible = state is StepMenuState.OpenTheory && state.isVisible
        theoryItem?.isVisible = isTheoryItemVisible
        if (isTheoryItemVisible) {
            val isTheoryItemEnabled = state is StepMenuState.OpenTheory && state.isEnabled
            theoryItem?.isEnabled = isTheoryItemEnabled
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.theoryFeedback -> {
                onTheoryFeedbackClick.invoke()
                true
            }
            R.id.theory -> {
                onTheoryClick.invoke()
                true
            }
            else -> {
                false
            }
        }
}