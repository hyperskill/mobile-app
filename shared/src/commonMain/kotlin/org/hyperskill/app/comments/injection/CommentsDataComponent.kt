package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.domain.interactor.CommentsDataInteractor

interface CommentsDataComponent {
    val commentsDataInteractor: CommentsDataInteractor
}