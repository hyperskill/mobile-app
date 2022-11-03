package org.hyperskill.app.magic_links.data.source

import org.hyperskill.app.magic_links.domain.model.MagicLink

interface MagicLinksRemoteDataSource {
    suspend fun createMagicLink(nextUrl: String): Result<MagicLink>
}