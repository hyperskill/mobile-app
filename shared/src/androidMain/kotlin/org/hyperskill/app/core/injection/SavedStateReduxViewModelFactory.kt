package org.hyperskill.app.core.injection

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Special [ViewModel] factory that allows you to create a [ViewModel] with a [SavedStateHandle].
 * */
class SavedStateReduxViewModelFactory(
    private val viewModelMap: Map<Class<out ViewModel>, (SavedStateHandle) -> ViewModel>
) : AbstractSavedStateViewModelFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T =
        viewModelMap[modelClass]?.invoke(handle) as T
}