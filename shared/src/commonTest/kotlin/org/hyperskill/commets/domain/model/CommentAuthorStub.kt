package org.hyperskill.commets.domain.model

import org.hyperskill.app.comments.domain.model.CommentAuthor

fun CommentAuthor.Companion.stub(
    id: Long = 0,
    avatar: String = "",
    fullName: String = ""
): CommentAuthor =
    CommentAuthor(
        id = id,
        avatar = avatar,
        fullName = fullName
    )