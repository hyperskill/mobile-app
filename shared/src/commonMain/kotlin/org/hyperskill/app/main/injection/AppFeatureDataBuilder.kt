package org.hyperskill.app.main.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.main.presentation.AppFeature

object AppFeatureDataBuilder {
    fun provideAuthorizationFlow(): MutableSharedFlow<AppFeature.Message> =
        MutableSharedFlow()
}