package org.hyperskill.app.discussions.injection

import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor
import org.hyperskill.app.discussions.domain.repository.DiscussionsRepository

interface DiscussionsDataComponent {
    val discussionsRepository: DiscussionsRepository
    val discussionsInteractor: DiscussionsInteractor
}