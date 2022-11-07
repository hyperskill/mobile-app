package org.hyperskill.app.discussions.injection

import org.hyperskill.app.discussions.domain.interactor.DiscussionsInteractor

interface DiscussionsDataComponent {
    val discussionsInteractor: DiscussionsInteractor
}