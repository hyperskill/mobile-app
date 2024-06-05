package org.hyperskill.app.android.step.view.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step.view.model.OpenTheoryMenuAction
import org.hyperskill.app.step.domain.model.StepMenuAction

@OptIn(FlowPreview::class)
class StepMenuDelegate(
    menuHost: MenuHost,
    private val viewLifecycleOwner: LifecycleOwner,
    private val onTheoryClick: () -> Unit,
    private val onSecondaryActionClick: (StepMenuAction) -> Unit
) : MenuProvider {

    private val menuActionsStateFlow: MutableStateFlow<MenuActionsState> = MutableStateFlow(
        MenuActionsState(
            openTheoryMenuAction = null,
            secondaryActions = emptySet()
        )
    )

    init {
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
        menuActionsStateFlow
            .debounce(100.milliseconds)
            .onEach {
                menuHost.invalidateMenu()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    fun renderMainMenuAction(action: OpenTheoryMenuAction) {
        viewLifecycleOwner.lifecycleScope.launch {
            menuActionsStateFlow.update {
                it.copy(openTheoryMenuAction = action)
            }
        }
    }

    fun renderSecondaryMenuActions(actions: Set<StepMenuAction>) {
        viewLifecycleOwner.lifecycleScope.launch {
            menuActionsStateFlow.update {
                it.copy(secondaryActions = actions)
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.step_menu, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        val state = menuActionsStateFlow.value
        renderMainAction(menu, state.openTheoryMenuAction)
        renderSecondaryActions(menu, state.secondaryActions)
    }

    private fun renderMainAction(menu: Menu, openTheoryAction: OpenTheoryMenuAction?) {
        val theoryItem: MenuItem? = menu.findItem(R.id.theory)
        val isTheoryItemVisible = openTheoryAction?.isVisible == true
        theoryItem?.isVisible = isTheoryItemVisible
        if (isTheoryItemVisible) {
            val isTheoryItemEnabled = openTheoryAction?.isEnabled == true
            theoryItem?.isEnabled = isTheoryItemEnabled
        }
    }

    private fun renderSecondaryActions(menu: Menu, actions: Set<StepMenuAction>) {
        menu.findItem(R.id.share)?.isVisible = actions.contains(StepMenuAction.SHARE)
        menu.findItem(R.id.practiceFeedback)?.isVisible = actions.contains(StepMenuAction.REPORT)
        menu.findItem(R.id.skip)?.isVisible = actions.contains(StepMenuAction.SKIP)
        menu.findItem(R.id.open_in_web)?.isVisible = actions.contains(StepMenuAction.OPEN_IN_WEB)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.theory -> {
                onTheoryClick.invoke()
                true
            }
            R.id.practiceFeedback -> {
                onSecondaryActionClick.invoke(StepMenuAction.REPORT)
                true
            }
            R.id.share -> {
                onSecondaryActionClick.invoke(StepMenuAction.SHARE)
                true
            }
            R.id.skip -> {
                onSecondaryActionClick.invoke(StepMenuAction.SKIP)
                true
            }
            R.id.open_in_web -> {
                onSecondaryActionClick.invoke(StepMenuAction.OPEN_IN_WEB)
                true
            }
            else -> {
                false
            }
        }

    private data class MenuActionsState(
        val openTheoryMenuAction: OpenTheoryMenuAction?,
        val secondaryActions: Set<StepMenuAction>
    )
}