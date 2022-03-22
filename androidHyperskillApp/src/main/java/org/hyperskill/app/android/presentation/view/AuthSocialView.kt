package org.hyperskill.app.android.presentation.view

interface AuthSocialView {
    sealed class State {
        // TODO: add data class with auth answer
        object Idle : State()
        object Loading : State()
        object NetworkError : State()
    }

    fun setState(state: State)
}