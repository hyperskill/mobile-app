package org.hyperskill.app.debug.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.debug.cache.DebugCacheDataSourceImpl
import org.hyperskill.app.debug.data.repository.DebugRepositoryImpl
import org.hyperskill.app.debug.data.source.DebugCacheDataSource
import org.hyperskill.app.debug.domain.interactor.DebugInteractor
import org.hyperskill.app.debug.domain.repository.DebugRepository
import org.hyperskill.app.debug.presentation.DebugFeature.Action
import org.hyperskill.app.debug.presentation.DebugFeature.Message
import org.hyperskill.app.debug.presentation.DebugFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

class DebugComponentImpl(appGraph: AppGraph) : DebugComponent {
    private val debugCacheDataSource: DebugCacheDataSource =
        DebugCacheDataSourceImpl(appGraph.commonComponent.settings)
    private val debugRepository: DebugRepository =
        DebugRepositoryImpl(debugCacheDataSource)
    private val debugInteractor: DebugInteractor =
        DebugInteractor(debugRepository)

    override val debugFeature: Feature<State, Message, Action>
        get() = DebugFeatureBuilder.build(debugInteractor)
}