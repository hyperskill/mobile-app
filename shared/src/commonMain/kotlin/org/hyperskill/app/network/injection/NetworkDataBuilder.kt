package org.hyperskill.app.network.injection

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.auth.domain.model.UserDeauthorized

object NetworkDataBuilder {
    fun provideAuthorizationFlow(): MutableSharedFlow<UserDeauthorized> =
        MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
}