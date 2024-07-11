package org.hyperskill.app.comments.injection

import org.hyperskill.app.comments.domain.repository.CommentsRepository

interface CommentsDataComponent {
    val commentsRepository: CommentsRepository
}