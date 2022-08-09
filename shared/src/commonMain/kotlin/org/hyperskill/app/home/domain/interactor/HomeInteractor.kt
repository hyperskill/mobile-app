package org.hyperskill.app.home.domain.interactor

import kotlinx.coroutines.flow.SharedFlow
import org.hyperskill.app.home.domain.repository.HomeRepository

class HomeInteractor(
    private val homeRepository: HomeRepository
) {
    val solvedProblemsSharedFlow: SharedFlow<Long> = homeRepository.solvedProblemsSharedFlow
}