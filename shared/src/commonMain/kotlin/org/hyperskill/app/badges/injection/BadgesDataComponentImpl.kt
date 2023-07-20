package org.hyperskill.app.badges.injection

import org.hyperskill.app.badges.data.repository.BadgesRepositoryImpl
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

internal class BadgesDataComponentImpl(
    appGraph: AppGraph
) : BadgesDataComponent {

    private val badgesRemoteDataSource: BadgesRemoteDataSourceImpl =
        BadgesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val badgesRepository: BadgesRepository
        get() = BadgesRepositoryImpl(badgesRemoteDataSource)
}