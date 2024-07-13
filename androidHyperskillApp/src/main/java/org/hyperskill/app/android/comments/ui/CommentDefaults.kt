package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object CommentDefaults {
    val CommentImageSize = 40.dp
    val CommentImagePadding = 8.dp

    val CommentContentVerticalPadding = 16.dp
    val CommentContentStartPadding = 4.dp

    val ReactionShape = RoundedCornerShape(8.dp)
    val ReactionHorizontalPadding = 8.dp

    private val CommentVerticalPadding = 24.dp
    private val CommentHorizontalPadding = 20.dp

    val RootCommentPadding = PaddingValues(
        start = CommentHorizontalPadding,
        end = CommentHorizontalPadding,
        top = CommentVerticalPadding,
    )

    val ReplyCommentPadding = PaddingValues(
        start = CommentHorizontalPadding + CommentImageSize + CommentImagePadding,
        end = CommentHorizontalPadding,
        top = CommentVerticalPadding
    )

    val SeparatorPadding = RootCommentPadding

    val ShowMoreButtonPadding = PaddingValues(
        horizontal = CommentHorizontalPadding,
        vertical = CommentVerticalPadding
    )
}