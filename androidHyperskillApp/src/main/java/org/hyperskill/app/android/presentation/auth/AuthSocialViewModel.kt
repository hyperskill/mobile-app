package org.hyperskill.app.android.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthSocialViewModel : ViewModel() {
    sealed class ViewState {
        // TODO: add data class with auth answer
        object Idle : ViewState()
        object Loading : ViewState()
        object Error : ViewState()
    }

    init {
        viewModelScope.launch {
            _viewState.emit(ViewState.Idle)
        }
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()
}