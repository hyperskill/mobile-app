package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState
import org.hyperskill.app.reactions.domain.model.ReactionType

@Composable
fun Comment(
    comment: CommentsScreenViewState.CommentItem,
    isShowRepliesBtnVisible: Boolean,
    onShowRepliesClick: (Long) -> Unit,
    onReactionClick: (Long, ReactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnReactionClick by rememberUpdatedState { reactionType: ReactionType ->
        onReactionClick(comment.id, reactionType)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(CommentDefaults.CommentContentVerticalPadding)
    ) {
        CommentHeader(
            authorAvatar = comment.authorAvatar,
            authorFullName = comment.authorFullName,
            formattedTime = comment.formattedTime
        )
        SelectionContainer {
            Text(
                text = comment.text,
                style = MaterialTheme.typography.body1,
                color = colorResource(id = R.color.text_primary),
                lineHeight = 16.sp,
                modifier = Modifier.padding(start = CommentDefaults.CommentContentStartPadding)
            )
        }
        CommentReactions(
            reactions = comment.reactions,
            onReactionClick = currentOnReactionClick,
            modifier = Modifier.padding(start = CommentDefaults.CommentContentStartPadding)
        )
        if (isShowRepliesBtnVisible) {
            ShowRepliesButton {
                onShowRepliesClick(comment.id)
            }
        }
    }
}

@Composable
private fun ShowRepliesButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(CommentDefaults.CommentContentStartPadding)
    ) {
        Text(
            text = stringResource(id = R.string.comments_show_replies_btn),
            fontSize = 14.sp,
            color = colorResource(id = R.color.color_primary_alpha_60)
        )
    }
}

@Preview
@Composable
private fun CommentPreview() {
    HyperskillTheme {
        Comment(
            comment = CommentPreviewDataProvider.getSingleComment(),
            isShowRepliesBtnVisible = true,
            onReactionClick = { _, _ -> },
            onShowRepliesClick = {}
        )
    }
}