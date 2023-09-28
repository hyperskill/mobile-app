package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action as AuthSocialAction
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message as AuthSocialMessage
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State as AuthSocialState
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action as AuthSocialWebViewAction
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message as AuthSocialWebViewMessage
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State as AuthSocialWebViewState

interface AuthSocialComponent {
    val authSocialFeature: Feature<AuthSocialState, AuthSocialMessage, AuthSocialAction>
    val authSocialErrorMapper: AuthSocialErrorMapper
    val authSocialWebViewFeature: Feature<AuthSocialWebViewState, AuthSocialWebViewMessage, AuthSocialWebViewAction>
}