package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.onboarding.cache.OnboardingCacheDataSourceImpl
import org.hyperskill.app.onboarding.data.repository.OnboardingRepositoryImpl
import org.hyperskill.app.onboarding.data.source.OnboardingCacheDataSource
import org.hyperskill.app.onboarding.domain.interactor.OnboardingInteractor
import org.hyperskill.app.onboarding.domain.repository.OnboardingRepository

class OnboardingComponentImpl(appGraph: AppGraph) : OnboardingComponent {
    private val onboardingCacheDataSource: OnboardingCacheDataSource = OnboardingCacheDataSourceImpl(
        appGraph.commonComponent.settings
    )

    private val onboardingRepository: OnboardingRepository = OnboardingRepositoryImpl(onboardingCacheDataSource)

    override val onboardingInteractor: OnboardingInteractor
        get() = OnboardingInteractor(onboardingRepository)
}