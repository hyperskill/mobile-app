package org.hyperskill.app.magic_links.injection

import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor

interface MagicLinksDataComponent {
    val urlPathProcessor: UrlPathProcessor
}