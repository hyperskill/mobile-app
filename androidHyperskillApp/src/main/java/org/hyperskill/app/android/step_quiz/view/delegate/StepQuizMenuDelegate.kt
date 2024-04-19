package org.hyperskill.app.android.step_quiz.view.delegate

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver

class StepQuizMenuDelegate(
    menuHost: MenuHost,
    viewLifecycleOwner: LifecycleOwner,
    private val onTheoryClick: () -> Unit
) : MenuProvider {

    private var theoryButtonState: TheoryButtonState? = null

    init {
        menuHost.addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.step_quiz_appbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.theory -> {
                onTheoryClick.invoke()
                true
            }
            else -> false
        }

    fun render(menuHost: MenuHost, state: StepQuizFeature.StepQuizState) {
        val newTheoryButtonState = TheoryButtonState(
            isVisible = StepQuizResolver.isTheoryToolbarItemAvailable(state),
            isEnabled = !StepQuizResolver.isQuizLoading(state)
        )
        if (newTheoryButtonState != this.theoryButtonState) {
            this.theoryButtonState = newTheoryButtonState
            menuHost.invalidateMenu()
        }
    }

    data class TheoryButtonState(
        val isVisible: Boolean,
        val isEnabled: Boolean
    )
}