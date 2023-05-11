package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import ru.nobird.app.presentation.redux.feature.Feature

interface PlatformAuthSocialWebViewComponent {
    val feature: Feature<
        AuthSocialWebViewFeature.State, AuthSocialWebViewFeature.Message, AuthSocialWebViewFeature.Action>
    val reduxViewModelFactory: ReduxViewModelFactory
}