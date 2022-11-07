package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.domain.interactor.CommentsInteractor

interface CommentsDataComponent {
    val commentsInteractor: CommentsInteractor
}