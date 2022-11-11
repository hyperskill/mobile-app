package org.hyperskill.app.magic_links.domain.interactor

import org.hyperskill.app.magic_links.domain.model.MagicLink

suspend fun MagicLinksInteractor.createMagicLink(nextUrl: String): MagicLink? =
    createMagicLink(nextUrl).getOrNull()