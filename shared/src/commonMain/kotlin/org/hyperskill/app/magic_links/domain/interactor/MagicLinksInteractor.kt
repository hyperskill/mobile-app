package org.hyperskill.app.magic_links.domain.interactor

import org.hyperskill.app.magic_links.domain.model.MagicLink
import org.hyperskill.app.magic_links.domain.repository.MagicLinksRepository

class MagicLinksInteractor(
    private val magicLinksRepository: MagicLinksRepository
) {
    /**
     * Creates authorization link.
     *
     * Notes:
     * - Endpoint is available only to authorized users
     * - Link available within 2 minutes
     * - Link becomes invalid after user authorization (by any ways)
     *
     * @param nextUrl Redirect url on successful login.
     * @return On success returns a value of MagicLink type or a failure with an arbitrary Throwable exception.
     * @see MagicLink
     */
    suspend fun createMagicLink(nextUrl: String): Result<MagicLink> =
        magicLinksRepository.createMagicLink(nextUrl)
}