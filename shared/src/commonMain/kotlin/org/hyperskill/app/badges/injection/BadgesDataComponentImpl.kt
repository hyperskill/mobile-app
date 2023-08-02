package org.hyperskill.app.badges.injection

import org.hyperskill.app.badges.data.repository.BadgesRepositoryImpl
import org.hyperskill.app.badges.data.source.BadgesRemoteDataSource
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.badges.remote.BadgesRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.debug.domain.model.EndpointConfigType

internal class BadgesDataComponentImpl(
    appGraph: AppGraph
) : BadgesDataComponent {

    private val badgesRemoteDataSource: BadgesRemoteDataSource =
        BadgesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    private val isBadgesFeatureEnabled: Boolean =
        appGraph.commonComponent.buildKonfig.endpointConfigType != EndpointConfigType.PRODUCTION

    override val badgesRepository: BadgesRepository
        get() = BadgesRepositoryImpl(
            badgesRemoteDataSource,
            isBadgesFeatureEnabled = isBadgesFeatureEnabled
        )
}