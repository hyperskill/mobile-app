package org.hyperskill.app.profile.presentation

import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.streak.domain.model.Streak

interface ProfileFeature {
    sealed interface State {
        object Idle : State
        object Loading : State
        object Error : State
        data class Content(
            val profile: Profile,
            val streak: Streak?
        ) : State
    }

    sealed interface Message {
        data class Init(
            val isInitCurrent: Boolean = true,
            val profileId: Long? = null,
            val forceUpdate: Boolean = false
        ) : Message

        sealed interface ProfileLoaded : Message {
            data class Success(val profile: Profile, val streak: Streak?) : ProfileLoaded
            data class Error(val errorMsg: String) : ProfileLoaded
        }
    }

    sealed interface Action {
        data class FetchProfile(val profileId: Long) : Action
        object FetchCurrentProfile : Action
        sealed class ViewAction : Action
    }
}