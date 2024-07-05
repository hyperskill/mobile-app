package org.hyperskill.app.reactions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.reactions.data.repository.ReactionsRepositoryImpl
import org.hyperskill.app.reactions.data.source.ReactionsRemoteDataSource
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository
import org.hyperskill.app.reactions.remote.ReactionsRemoteDataSourceImpl

internal class ReactionsDataComponentImpl(
    appGraph: AppGraph
) : ReactionsDataComponent {
    private val reactionsRemoteDataComponent: ReactionsRemoteDataSource =
        ReactionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val reactionsRepository: ReactionsRepository
        get() = ReactionsRepositoryImpl(reactionsRemoteDataComponent)
}