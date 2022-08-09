package org.hyperskill.app.home.domain.repository

import kotlinx.coroutines.flow.MutableSharedFlow

interface HomeRepository {
    val solvedProblemsSharedFlow: MutableSharedFlow<Long>
}