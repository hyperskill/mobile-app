package org.hyperskill.app.profile.injection

import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository

interface ProfileDataComponent {
    val profileRepository: ProfileRepository
    val profileInteractor: ProfileInteractor
}