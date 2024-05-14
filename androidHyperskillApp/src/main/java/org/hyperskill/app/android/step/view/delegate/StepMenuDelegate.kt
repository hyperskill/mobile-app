package org.hyperskill.app.android.step.view.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step.view.model.StepMenuState

class StepMenuDelegate(
    menuHost: MenuHost,
    private val viewLifecycleOwner: LifecycleOwner,
    private val onTheoryClick: () -> Unit,
    private val onTheoryFeedbackClick: () -> Unit
) : MenuProvider {

    private var menuStateFlow: MutableStateFlow<StepMenuState?> = MutableStateFlow(null)

    init {
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        menuStateFlow
            .onEach {
                menuHost.invalidateMenu()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    fun renderMenu(menuState: StepMenuState) {
        val newState = if (menuStateFlow.value is StepMenuState.OpenTheory && menuState is StepMenuState.OpenTheory) {
            StepMenuState.OpenTheory(
                isVisible = true,
                isEnabled = menuState.isEnabled
            )
        } else {
            menuState
        }
        viewLifecycleOwner.lifecycleScope.launch {
            menuStateFlow.emit(newState)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        when (menuStateFlow.value) {
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

        val state = menuStateFlow.value

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