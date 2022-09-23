package org.hyperskill.app.profile.injection

import org.hyperskill.app.profile.domain.interactor.ProfileInteractor

interface ProfileDataComponent {
    val profileInteractor: ProfileInteractor
}