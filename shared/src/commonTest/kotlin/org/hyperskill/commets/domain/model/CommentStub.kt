package org.hyperskill.commets.domain.model

import kotlinx.datetime.Instant
import org.hyperskill.app.comments.domain.model.Comment
import org.hyperskill.app.comments.domain.model.CommentAuthor
import org.hyperskill.app.comments.domain.model.CommentReaction
import org.hyperskill.app.content_type.domain.model.ContentType

fun Comment.Companion.stub(
    id: Long = 0,
    targetId: Long = 0,
    targetType: ContentType = ContentType.UNKNOWN,
    text: String = "",
    localizedText: String = "",
    user: CommentAuthor = CommentAuthor.stub(),
    time: Instant? = null,
    reactions: List<CommentReaction> = emptyList()
): Comment =
    Comment(
        id = id,
        targetId = targetId,
        targetType = targetType,
        text = text,
        localizedText = localizedText,
        user = user,
        time = time,
        reactions = reactions
    )