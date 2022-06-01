package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthSocialFeature
import org.hyperskill.app.auth.view.mapper.AuthSocialErrorMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface AuthSocialComponent {
    val authSocialFeature: Feature<AuthSocialFeature.State, AuthSocialFeature.Message, AuthSocialFeature.Action>
    val authSocialErrorMapper: AuthSocialErrorMapper
}