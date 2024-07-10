package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTextButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState.DiscussionReplies
import org.hyperskill.app.comments.screen.view.model.CommentsScreenViewState.DiscussionsViewState
import org.hyperskill.app.reactions.domain.model.ReactionType

private const val LoadingStateItemsCount = 3

@Composable
fun CommentList(
    discussionsState: DiscussionsViewState,
    onShowRepliesClick: (Long) -> Unit,
    onReactionClick: (Long, ReactionType) -> Unit,
    onShowMoreDiscussionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val safeDrawingInsetsPadding = WindowInsets.safeDrawing.asPaddingValues()
    LazyColumn(
        modifier = modifier,
        contentPadding = safeDrawingInsetsPadding
    ) {
        when (discussionsState) {
            is DiscussionsViewState.Content -> {
                discussions(
                    discussionsState = discussionsState,
                    onShowRepliesClick = onShowRepliesClick,
                    onReactionClick = onReactionClick,
                    onShowMoreDiscussionsClick = onShowMoreDiscussionsClick
                )
            }
            DiscussionsViewState.Loading -> {
                loadingItems()
            }

            DiscussionsViewState.Idle,
            DiscussionsViewState.Error -> {
                error("$discussionsState can not be rendered with CommentList composable")
            }
        }
    }
}

private fun LazyListScope.discussions(
    discussionsState: DiscussionsViewState.Content,
    onShowRepliesClick: (Long) -> Unit,
    onReactionClick: (Long, ReactionType) -> Unit,
    onShowMoreDiscussionsClick: () -> Unit
) {
    discussionsState.discussions.forEachIndexed { index, discussionItem ->
        item(
            key = discussionItem.id,
            contentType = CommentListContentType.COMMENT
        ) {
            Comment(
                comment = discussionItem.comment,
                isShowRepliesBtnVisible = discussionItem.replies is DiscussionReplies.ShowRepliesButton,
                onShowRepliesClick = onShowRepliesClick,
                onReactionClick = onReactionClick,
                modifier = Modifier.padding(CommentDefaults.RootCommentPadding)
            )
        }
        replies(
            repliesState = discussionItem.replies,
            onShowRepliesClick = onShowRepliesClick,
            onReactionClick = onReactionClick
        )
        when {
            index < discussionsState.discussions.lastIndex -> {
                item(contentType = CommentListContentType.SEPARATOR) {
                    Separator(modifier = Modifier.fillParentMaxWidth())
                }
            }
            discussionsState.isLoadingNextPage -> {
                item(contentType = CommentListContentType.SEPARATOR) {
                    Separator(modifier = Modifier.fillParentMaxWidth())
                }
                item(contentType = CommentListContentType.COMMENT_SKELETON) {
                    CommentSkeleton(modifier = Modifier.padding(CommentDefaults.RootCommentPadding))
                }
            }
            discussionsState.hasNextPage -> {
                item(contentType = CommentListContentType.SHOW_MORE_BUTTON) {
                    ShowMoreDiscussionButton(
                        onShowMoreDiscussionsClick = onShowMoreDiscussionsClick,
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }
    }
}

private fun LazyListScope.replies(
    repliesState: DiscussionReplies,
    onShowRepliesClick: (Long) -> Unit,
    onReactionClick: (Long, ReactionType) -> Unit
) {
    when (repliesState) {
        DiscussionReplies.EmptyReplies,
        DiscussionReplies.ShowRepliesButton -> {
            // no op
        }
        DiscussionReplies.LoadingReplies -> {
            item(key = CommentListContentType.COMMENT_SKELETON) {
                CommentSkeleton(
                    modifier = Modifier.padding(CommentDefaults.ReplyCommentPadding)
                )
            }
        }
        is DiscussionReplies.Content -> {
            repliesState.replies.forEach { reply ->
                item(
                    key = reply.id,
                    contentType = CommentListContentType.COMMENT
                ) {
                    Comment(
                        comment = reply,
                        isShowRepliesBtnVisible = false,
                        onShowRepliesClick = onShowRepliesClick,
                        onReactionClick = onReactionClick,
                        modifier = Modifier.padding(CommentDefaults.ReplyCommentPadding)
                    )
                }
            }
        }
    }
}

private fun LazyListScope.loadingItems() {
    repeat(LoadingStateItemsCount) { i ->
        item(contentType = CommentListContentType.COMMENT_SKELETON) {
            CommentSkeleton(
                modifier = Modifier.padding(CommentDefaults.RootCommentPadding)
            )
        }
        if (i != LoadingStateItemsCount - 1) {
            item(contentType = CommentListContentType.SEPARATOR) {
                Separator(modifier = Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
private fun Separator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(CommentDefaults.SeparatorPadding)
            .height(1.dp)
            .background(color = colorResource(id = R.color.color_on_surface_alpha_12))
    )
}

@Composable
private fun ShowMoreDiscussionButton(
    onShowMoreDiscussionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HyperskillTextButton(
        onClick = onShowMoreDiscussionsClick,
        modifier = modifier.padding(CommentDefaults.ShowMoreButtonPadding)
    ) {
        Text(stringResource(id = R.string.comments_show_more_btn))
    }
}

class CommentsContentPreviewParameterProvider : CollectionPreviewParameterProvider<DiscussionsViewState>(
    listOf(
        DiscussionsViewState.Loading,
        CommentPreviewDataProvider.loadingNextPageDiscussions,
        CommentPreviewDataProvider.loadingNextPageAndRepliesDiscussions,
        CommentPreviewDataProvider.mixedDiscussions
    )
)

@Preview(showSystemUi = true)
@Composable
private fun CommentsContentPreview(
    @PreviewParameter(CommentsContentPreviewParameterProvider::class) discussionsState: DiscussionsViewState
) {
    HyperskillTheme {
        CommentList(
            discussionsState = discussionsState,
            onShowRepliesClick = {},
            onReactionClick = { _, _ -> },
            onShowMoreDiscussionsClick = {},
            modifier = Modifier.background(colorResource(id = R.color.color_surface)),
        )
    }
}