package org.hyperskill.app.welcome.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.welcome.cache.WelcomeCacheDataSourceImpl
import org.hyperskill.app.welcome.data.repository.WelcomeRepositoryImpl
import org.hyperskill.app.welcome.data.source.WelcomeCacheDataSource
import org.hyperskill.app.welcome.domain.interactor.WelcomeInteractor
import org.hyperskill.app.welcome.domain.repository.WelcomeRepository
import org.hyperskill.app.welcome.presentation.WelcomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

class WelcomeComponentImpl(private val appGraph: AppGraph) : WelcomeComponent {
    private val welcomeCacheDataSource: WelcomeCacheDataSource = WelcomeCacheDataSourceImpl(
        appGraph.commonComponent.settings
    )

    private val welcomeRepository: WelcomeRepository = WelcomeRepositoryImpl(welcomeCacheDataSource)

    override val welcomeInteractor: WelcomeInteractor
        get() = WelcomeInteractor(welcomeRepository)

    override val welcomeFeature: Feature<WelcomeFeature.State, WelcomeFeature.Message, WelcomeFeature.Action>
        get() = WelcomeFeatureBuilder.build(
            welcomeInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}