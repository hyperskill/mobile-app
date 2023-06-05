package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.profile.domain.model.Profile

interface CurrentProfileStateRepository : StateRepository<Profile>