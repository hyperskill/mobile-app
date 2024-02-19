package org.hyperskill.app.profile.injection

import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository

interface ProfileDataComponent {
    val profileRepository: ProfileRepository
    val currentProfileStateRepository: CurrentProfileStateRepository
}