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
import org.hyperskill.app.android.step.view.model.StepMainMenuAction
import org.hyperskill.app.step.domain.model.StepToolbarAction

class StepMenuDelegate(
    menuHost: MenuHost,
    private val viewLifecycleOwner: LifecycleOwner,
    private val onTheoryClick: () -> Unit,
    private val onTheoryFeedbackClick: () -> Unit,
    private val onSecondaryActionClick: (StepToolbarAction) -> Unit
) : MenuProvider {

    private var mainMenuActionStateFlow: MutableStateFlow<StepMainMenuAction?> = MutableStateFlow(null)
    private var secondaryMenuActionsFlow: MutableStateFlow<Set<StepToolbarAction>> = MutableStateFlow(emptySet())

    init {
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        mainMenuActionStateFlow
            .onEach {
                menuHost.invalidateMenu()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        secondaryMenuActionsFlow
            .onEach {
                menuHost.invalidateMenu()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    fun renderMainMenuAction(menuState: StepMainMenuAction) {
        val newState = if (mainMenuActionStateFlow.value is StepMainMenuAction.OpenTheory && menuState is StepMainMenuAction.OpenTheory) {
            StepMainMenuAction.OpenTheory(
                isVisible = true,
                isEnabled = menuState.isEnabled
            )
        } else {
            menuState
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainMenuActionStateFlow.emit(newState)
        }
    }

    fun renderSecondaryMenuActions(actions: Set<StepToolbarAction>) {
        viewLifecycleOwner.lifecycleScope.launch {
            secondaryMenuActionsFlow.emit(actions)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.step_menu, menu)

    override fun onPrepareMenu(menu: Menu) {
        renderMainAction(menu, mainMenuActionStateFlow.value)
        renderSecondaryActions(menu, secondaryMenuActionsFlow.value)
    }

    private fun renderMainAction(menu: Menu, mainMenuAction: StepMainMenuAction?) {
        val theoryItem: MenuItem? = menu.findItem(R.id.theory)
        val theoryFeedbackItem: MenuItem? = menu.findItem(R.id.theoryFeedback)

        theoryFeedbackItem?.isVisible = mainMenuAction is StepMainMenuAction.Theory

        val isTheoryItemVisible = mainMenuAction is StepMainMenuAction.OpenTheory && mainMenuAction.isVisible
        theoryItem?.isVisible = isTheoryItemVisible
        if (isTheoryItemVisible) {
            val isTheoryItemEnabled = mainMenuAction is StepMainMenuAction.OpenTheory && mainMenuAction.isEnabled
            theoryItem?.isEnabled = isTheoryItemEnabled
        }
    }

    private fun renderSecondaryActions(menu: Menu, actions: Set<StepToolbarAction>) {
        menu.findItem(R.id.share)?.isVisible = actions.contains(StepToolbarAction.SHARE)
        menu.findItem(R.id.practiceFeedback)?.isVisible = actions.contains(StepToolbarAction.REPORT)
        menu.findItem(R.id.skip)?.isVisible = actions.contains(StepToolbarAction.SKIP)
        menu.findItem(R.id.open_in_web)?.isVisible = actions.contains(StepToolbarAction.OPEN_IN_WEB)
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
            R.id.practiceFeedback -> {
                onSecondaryActionClick.invoke(StepToolbarAction.REPORT)
                true
            }
            R.id.share -> {
                onSecondaryActionClick.invoke(StepToolbarAction.SHARE)
                true
            }
            R.id.skip -> {
                onSecondaryActionClick.invoke(StepToolbarAction.SKIP)
                true
            }
            R.id.open_in_web -> {
                onSecondaryActionClick.invoke(StepToolbarAction.OPEN_IN_WEB)
                true
            }
            else -> {
                false
            }
        }
}