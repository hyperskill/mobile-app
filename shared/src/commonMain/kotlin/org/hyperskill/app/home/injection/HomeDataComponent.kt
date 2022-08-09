package org.hyperskill.app.home.injection

import org.hyperskill.app.home.domain.repository.HomeRepository

interface HomeDataComponent {
    val homeRepository: HomeRepository
}