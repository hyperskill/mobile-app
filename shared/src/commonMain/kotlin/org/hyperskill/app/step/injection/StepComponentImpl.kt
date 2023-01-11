package org.hyperskill.app.step.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.step.data.repository.StepRepositoryImpl
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.repository.StepRepository
import org.hyperskill.app.step.presentation.StepFeature
import org.hyperskill.app.step.remote.StepRemoteDataSourceImpl
import org.hyperskill.app.step.view.mapper.CommentThreadTitleMapper
import ru.nobird.app.presentation.redux.feature.Feature

class StepComponentImpl(private val appGraph: AppGraph) : StepComponent {
    private val stepRemoteDataSource: StepRemoteDataSource = StepRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val stepRepository: StepRepository = StepRepositoryImpl(stepRemoteDataSource)
    private val stepInteractor: StepInteractor = StepInteractor(stepRepository)

    override val commentThreadTitleMapper: CommentThreadTitleMapper
        get() = CommentThreadTitleMapper(appGraph.commonComponent.resourceProvider)

    override val stepFeature: Feature<StepFeature.State, StepFeature.Message, StepFeature.Action>
        get() = StepFeatureBuilder.build(
            stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )
}