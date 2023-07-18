package org.hyperskill.app.badges.injection

import org.hyperskill.app.badges.domain.data.repository.BadgesRepositoryImpl
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSource
import org.hyperskill.app.core.injection.AppGraph

class BadgesDataComponentImpl(
    appGraph: AppGraph
) : BadgesDataComponent {

    private val badgesRemoteDataSource: BadgesRemoteDataSource =
        BadgesRemoteDataSource(appGraph.networkComponent.authorizedHttpClient)

    override val badgesRepository: BadgesRepository
        get() = BadgesRepositoryImpl(badgesRemoteDataSource)
}