package org.hyperskill.app.android.step.view.delegate

import android.util.Log
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
import kotlinx.coroutines.launch
import org.hyperskill.app.android.R
import org.hyperskill.app.android.step.view.model.StepMenuState

@OptIn(FlowPreview::class)
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
            // Hack to avoid menu items invisibility
            // when toolbar content transition is playing.
            .debounce(200.milliseconds)
            .onEach {
                menuHost.invalidateMenu()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    fun renderMenu(menuState: StepMenuState) {
        viewLifecycleOwner.lifecycleScope.launch {
            menuStateFlow.emit(menuState)
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
        Log.d("StepMenuDelegate", "onPrepareMenu(state=${menuStateFlow.value})")
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