package org.hyperskill.app.magic_links.data.repository

import org.hyperskill.app.magic_links.data.source.MagicLinksRemoteDataSource
import org.hyperskill.app.magic_links.domain.model.MagicLink
import org.hyperskill.app.magic_links.domain.repository.MagicLinksRepository

class MagicLinksRepositoryImpl(
    private val magicLinksRemoteDataSource: MagicLinksRemoteDataSource
) : MagicLinksRepository {
    override suspend fun createMagicLink(nextUrl: String): Result<MagicLink> =
        magicLinksRemoteDataSource.createMagicLink(nextUrl)
}