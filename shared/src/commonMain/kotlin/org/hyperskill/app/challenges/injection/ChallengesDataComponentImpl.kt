package org.hyperskill.app.challenges.injection

import org.hyperskill.app.challenges.data.repository.ChallengesRepositoryImpl
import org.hyperskill.app.challenges.data.source.ChallengesRemoteDataSource
import org.hyperskill.app.challenges.domain.repository.ChallengesRepository
import org.hyperskill.app.challenges.remote.ChallengesRemoteDataSourceImpl
import org.hyperskill.app.core.injection.AppGraph

internal class ChallengesDataComponentImpl(appGraph: AppGraph) : ChallengesDataComponent {
    private val challengesRemoteDataSource: ChallengesRemoteDataSource =
        ChallengesRemoteDataSourceImpl(appGraph.networkComponent.authorizedHttpClient)

    override val challengesRepository: ChallengesRepository
        get() = ChallengesRepositoryImpl(challengesRemoteDataSource)
}