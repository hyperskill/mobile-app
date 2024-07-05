package org.hyperskill.app.reactions.injection

import org.hyperskill.app.reactions.domain.repository.ReactionsRepository

interface ReactionsDataComponent {
    val reactionsRepository: ReactionsRepository
}