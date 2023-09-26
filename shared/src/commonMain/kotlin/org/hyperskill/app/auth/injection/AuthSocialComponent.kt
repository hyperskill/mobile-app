package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialFeature.Action
import org.hyperskill.app.auth.presentation.AuthSocialFeature.Message
import org.hyperskill.app.auth.presentation.AuthSocialFeature.State
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Action as WebViewAction
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.Message as WebViewMessage
import org.hyperskill.app.auth.presentation.AuthSocialWebViewFeature.State as WebViewState

interface AuthSocialComponent {
    val authSocialFeature: Feature<State, Message, Action>
    val authSocialErrorMapper: AuthSocialErrorMapper
    val authSocialWebViewFeature: Feature<WebViewState, WebViewMessage, WebViewAction>
}