package org.hyperskill.app.android.core.view.ui.fragment

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

/**
 * Lifecycle observer that attaches and detaches [ReduxView] to [ReduxViewModel]
 * on ON_START and ON_STOP events respectively.
 * This observer should be used in cases when [reduxViewModel] can't be used.
 */
class ReduxViewLifecycleObserver<State, Message, Action>(
    private val reduxView: ReduxView<State, Action>,
    private val provideViewModel: () -> ReduxViewModel<State, Message, Action>
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        provideViewModel().attachView(reduxView)
    }

    override fun onStop(owner: LifecycleOwner) {
        provideViewModel().detachView(reduxView)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}