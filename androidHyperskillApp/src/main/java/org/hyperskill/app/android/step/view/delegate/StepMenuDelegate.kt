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
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step.view.model.StepMenuPrimaryAction
import org.hyperskill.app.android.step.view.model.StepMenuPrimaryActionParams
import org.hyperskill.app.core.utils.mutate
import org.hyperskill.app.step.domain.model.StepMenuSecondaryAction

@OptIn(FlowPreview::class)
class StepMenuDelegate(
    menuHost: MenuHost,
    viewLifecycleOwner: LifecycleOwner,
    private val onPrimaryActionClick: (StepMenuPrimaryAction) -> Unit,
    private val onSecondaryActionClick: (StepMenuSecondaryAction) -> Unit,
    private val onBackClick: () -> Unit
) : MenuProvider {

    private val menuActionsStateFlow: MutableStateFlow<MenuActionsState> = MutableStateFlow(
        MenuActionsState(
            primaryActions = emptyMap(),
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

    fun renderPrimaryMenuAction(
        action: StepMenuPrimaryAction,
        params: StepMenuPrimaryActionParams
    ) {
        menuActionsStateFlow.update {
            it.copy(
                primaryActions = it.primaryActions.mutate {
                    set(action, params)
                }
            )
        }
    }

    fun renderSecondaryMenuActions(actions: Set<StepMenuSecondaryAction>) {
        menuActionsStateFlow.update {
            it.copy(secondaryActions = actions)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.step_menu, menu)
    }

    override fun onPrepareMenu(menu: Menu) {
        val state = menuActionsStateFlow.value
        renderMainAction(menu, state.primaryActions)
        renderSecondaryActions(menu, state.secondaryActions)
    }

    private fun renderMainAction(
        menu: Menu,
        primaryActions: Map<StepMenuPrimaryAction, StepMenuPrimaryActionParams>
    ) {
        StepMenuPrimaryAction.entries.forEach { action ->
            val params: StepMenuPrimaryActionParams? = primaryActions[action]
            val menuItem: MenuItem? = menu.findItem(
                when (action) {
                    StepMenuPrimaryAction.THEORY -> R.id.theory
                }
            )
            val isVisible = params?.isVisible == true
            menuItem?.isVisible = isVisible
            if (isVisible) {
                val isEnabled = params?.isEnabled == true
                menuItem?.isEnabled = isEnabled
            }
        }
    }

    private fun renderSecondaryActions(menu: Menu, actions: Set<StepMenuSecondaryAction>) {
        StepMenuSecondaryAction.entries.forEach { action ->
            val menuItemId = when (action) {
                StepMenuSecondaryAction.SHARE -> R.id.share
                StepMenuSecondaryAction.REPORT -> R.id.practiceFeedback
                StepMenuSecondaryAction.SKIP -> R.id.skip
                StepMenuSecondaryAction.OPEN_IN_WEB -> R.id.open_in_web
                StepMenuSecondaryAction.СOMMENTS -> R.id.comments
            }
            menu.findItem(menuItemId)?.isVisible = actions.contains(action)
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.theory -> {
                onPrimaryActionClick.invoke(StepMenuPrimaryAction.THEORY)
                true
            }
            R.id.comments -> {
                onSecondaryActionClick.invoke(StepMenuSecondaryAction.СOMMENTS)
                true
            }
            R.id.practiceFeedback -> {
                onSecondaryActionClick.invoke(StepMenuSecondaryAction.REPORT)
                true
            }
            R.id.share -> {
                onSecondaryActionClick.invoke(StepMenuSecondaryAction.SHARE)
                true
            }
            R.id.skip -> {
                onSecondaryActionClick.invoke(StepMenuSecondaryAction.SKIP)
                true
            }
            R.id.open_in_web -> {
                onSecondaryActionClick.invoke(StepMenuSecondaryAction.OPEN_IN_WEB)
                true
            }
            android.R.id.home -> {
                onBackClick()
                true
            }
            else -> false
        }

    private data class MenuActionsState(
        val primaryActions: Map<StepMenuPrimaryAction, StepMenuPrimaryActionParams>,
        val secondaryActions: Set<StepMenuSecondaryAction>
    )
}