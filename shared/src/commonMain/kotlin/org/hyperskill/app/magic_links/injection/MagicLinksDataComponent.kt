package org.hyperskill.app.magic_links.injection

import org.hyperskill.app.magic_links.domain.interactor.MagicLinksInteractor

interface MagicLinksDataComponent {
    val magicLinksInteractor: MagicLinksInteractor
}