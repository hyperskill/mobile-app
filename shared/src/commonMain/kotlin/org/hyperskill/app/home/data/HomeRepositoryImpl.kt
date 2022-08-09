package org.hyperskill.app.home.data

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.home.domain.repository.HomeRepository

class HomeRepositoryImpl : HomeRepository {
    override val solvedProblemsSharedFlow: MutableSharedFlow<Long> =
        MutableSharedFlow()
}