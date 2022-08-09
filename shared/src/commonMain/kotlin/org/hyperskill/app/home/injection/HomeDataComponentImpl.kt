package org.hyperskill.app.home.injection

import org.hyperskill.app.home.data.HomeRepositoryImpl
import org.hyperskill.app.home.domain.repository.HomeRepository

class HomeDataComponentImpl : HomeDataComponent {
    override val homeRepository: HomeRepository = HomeRepositoryImpl()
}