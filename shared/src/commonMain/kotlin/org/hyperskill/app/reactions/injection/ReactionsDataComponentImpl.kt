package org.hyperskill.app.reactions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.reactions.data.repository.ReactionsRepositoryImpl
import org.hyperskill.app.reactions.data.source.ReactionsRemoteDataSource
import org.hyperskill.app.reactions.domain.interactor.ReactionsInteractor
import org.hyperskill.app.reactions.domain.repository.ReactionsRepository
import org.hyperskill.app.reactions.remote.ReactionsRemoteDataSourceImpl

class ReactionsDataComponentImpl(
    appGraph: AppGraph
) : ReactionsDataComponent {
    private val reactionsRemoteDataComponent: ReactionsRemoteDataSource =
        ReactionsRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)
    private val reactionsRepository: ReactionsRepository =
        ReactionsRepositoryImpl(reactionsRemoteDataComponent)

    override val reactionsInteractor: ReactionsInteractor
        get() = ReactionsInteractor(reactionsRepository)
}