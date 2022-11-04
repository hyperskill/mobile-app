package org.hyperskill.app.magic_links.domain.repository

import org.hyperskill.app.magic_links.domain.model.MagicLink

interface MagicLinksRepository {
    suspend fun createMagicLink(nextUrl: String): Result<MagicLink>
}