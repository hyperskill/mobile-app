package org.hyperskill.app.auth.injection

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.auth.domain.model.UserDeauthorized

object AuthDataBuilder {
    fun provideAuthorizationFlow(): MutableSharedFlow<UserDeauthorized> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
}