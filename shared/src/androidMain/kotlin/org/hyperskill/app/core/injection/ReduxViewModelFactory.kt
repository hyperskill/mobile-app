package org.hyperskill.app.core.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ReduxViewModelFactory(
    private val viewModelMap: Map<Class<out ViewModel>, () -> ViewModel>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModelMap[modelClass]?.invoke() as T
}