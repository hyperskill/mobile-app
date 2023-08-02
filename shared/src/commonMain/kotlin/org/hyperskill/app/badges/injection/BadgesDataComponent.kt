package org.hyperskill.app.badges.injection

import org.hyperskill.app.badges.domain.repository.BadgesRepository

interface BadgesDataComponent {
    val badgesRepository: BadgesRepository
}