package org.hyperskill.app.likes.injection

import org.hyperskill.app.likes.domain.interactor.LikesInteractor

interface LikesDataComponent {
    val likesInteractor: LikesInteractor
}