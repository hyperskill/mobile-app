package org.hyperskill.app.main.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.main.presentation.AppFeature
import org.hyperskill.app.onboarding.cache.OnboardingCacheDataSourceImpl
import org.hyperskill.app.onboarding.data.repository.OnboardingRepositoryImpl
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository
import ru.nobird.app.presentation.redux.feature.Feature

class MainComponentImpl(private val appGraph: AppGraph) : MainComponent {

    private val onboardingCacheDataSource: OnboardingCacheDataSource =
        OnboardingCacheDataSourceImpl(appGraph.commonComponent.settings)

    private val onboardingRepository: OnboardingRepository =
        OnboardingRepositoryImpl(onboardingCacheDataSource)

    private val onboardingInteractor: OnboardingInteractor =
        OnboardingInteractor(onboardingRepository)

    override val appFeature: Feature<AppFeature.State, AppFeature.Message, AppFeature.Action>
        get() = AppFeatureBuilder.build(appGraph.authComponent.authInteractor, appGraph.buildProfileDataComponent().profileInteractor, onboardingInteractor)
}