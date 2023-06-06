package org.hyperskill.app.profile.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.hyperskill.app.core.domain.repository.StateRepository
import org.hyperskill.app.profile.domain.model.Profile

interface CurrentProfileStateRepository : StateRepository<Profile>

fun CurrentProfileStateRepository.observeHypercoinsBalance(): Flow<Int> =
    changes
        .distinctUntilChanged()
        .map { it.gamification.hypercoinsBalance }