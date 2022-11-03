package org.hyperskill.app.magic_links.domain.interactor

import org.hyperskill.app.magic_links.domain.model.MagicLink
import org.hyperskill.app.magic_links.domain.repository.MagicLinksRepository

class MagicLinksInteractor(
    private val magicLinksRepository: MagicLinksRepository
) {
    suspend fun createMagicLink(nextUrl: String): Result<MagicLink> =
        magicLinksRepository.createMagicLink(nextUrl)
}