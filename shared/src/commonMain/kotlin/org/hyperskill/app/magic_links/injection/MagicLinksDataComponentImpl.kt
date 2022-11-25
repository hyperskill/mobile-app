package org.hyperskill.app.magic_links.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.magic_links.data.repository.MagicLinksRepositoryImpl
import org.hyperskill.app.magic_links.data.source.MagicLinksRemoteDataSource
import org.hyperskill.app.magic_links.domain.interactor.MagicLinksInteractor
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.magic_links.domain.repository.MagicLinksRepository
import org.hyperskill.app.magic_links.remote.MagicLinksRemoteDataSourceImpl

class MagicLinksDataComponentImpl(appGraph: AppGraph) : MagicLinksDataComponent {
    private val magicLinksRemoteDataSource: MagicLinksRemoteDataSource = MagicLinksRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val magicLinksRepository: MagicLinksRepository = MagicLinksRepositoryImpl(
        magicLinksRemoteDataSource
    )

    private val magicLinksInteractor: MagicLinksInteractor =
        MagicLinksInteractor(magicLinksRepository)

    override val urlPathProcessor: UrlPathProcessor
        get() = UrlPathProcessor(magicLinksInteractor)
}