package org.hyperskill.app.auth.injection

import org.hyperskill.app.auth.presentation.AuthCredentialsFeature
import org.hyperskill.app.auth.view.mapper.AuthCredentialsErrorMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface AuthCredentialsComponent {
    val authCredentialsFeature: Feature<AuthCredentialsFeature.State, AuthCredentialsFeature.Message, AuthCredentialsFeature.Action>
    val authCredentialsErrorMapper: AuthCredentialsErrorMapper
}