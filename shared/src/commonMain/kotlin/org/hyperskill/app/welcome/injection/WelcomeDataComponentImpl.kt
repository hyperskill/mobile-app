package org.hyperskill.app.welcome.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome.cache.WelcomeCacheDataSourceImpl
import org.hyperskill.app.welcome.data.repository.WelcomeRepositoryImpl
import org.hyperskill.app.welcome.data.source.WelcomeCacheDataSource
import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor
import org.hyperskill.app.welcome.domain.repository.WelcomeRepository

internal class WelcomeDataComponentImpl(appGraph: AppGraph) : WelcomeDataComponent {
    private val welcomeCacheDataSource: WelcomeCacheDataSource =
        WelcomeCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val welcomeRepository: WelcomeRepository =
        WelcomeRepositoryImpl(welcomeCacheDataSource)

    override val welcomeInteractor: WelcomeInteractor
        get() = WelcomeInteractor(welcomeRepository)
}