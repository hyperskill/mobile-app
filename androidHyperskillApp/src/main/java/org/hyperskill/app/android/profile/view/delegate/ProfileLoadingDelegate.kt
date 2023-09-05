package org.hyperskill.app.android.profile.view.delegate

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.hyperskill.app.android.core.view.ui.dialog.LoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class ProfileLoadingDelegate(
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle
) {
    // Is used to avoid multiple LoadingProgressDialogFragment showing,
    // in case of fast same value handling
    private val isLoadingShownFlow = MutableStateFlow(false)

    init {
        isLoadingShownFlow
            .onEach { isLoadingShowed ->
                if (isLoadingShowed) {
                    LoadingProgressDialogFragment.newInstance()
                        .showIfNotExists(fragmentManager, LoadingProgressDialogFragment.TAG)
                } else {
                    fragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
                }
            }
            .launchIn(lifecycle.coroutineScope)
    }

    fun render(isLoadingShown: Boolean) {
        lifecycle.coroutineScope.launch {
            isLoadingShownFlow.emit(isLoadingShown)
        }
    }
}