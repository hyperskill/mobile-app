package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.zIndex
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.comments.domain.model.CommentReaction
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.model.commentReactions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentReactions(
    reactions: List<CommentReaction>,
    onReactionClick: (ReactionType) -> Unit,
    onShowMoreReactionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(CommentDefaults.ReactionHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        reactions.forEach { reaction ->
            CommentReaction(
                reactionType = reaction.reactionType,
                reactionsCount = reaction.value,
                isSet = reaction.isSet,
                onClick = onReactionClick
            )
        }
        ShowMoreReactions(
            onClick = onShowMoreReactionsClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

private val CommentReactionsPopupPositionProvider = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        TODO("Not yet implemented")
    }
}

private val CommentReactions = ReactionType.commentReactions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentReactionsPopup(
    onReactionClick: (ReactionType) -> Unit,
    onDismissRequest: (() -> Unit)? = null
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = onDismissRequest
    ) {
        HyperskillCard(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.zIndex(10f)
        ) {
            FlowRow(
                maxItemsInEachRow = 4,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CommentReactions.forEach { reactionType ->
                    val currentOnReactionClick by rememberUpdatedState {
                        onReactionClick(reactionType)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(onClick = currentOnReactionClick)
                            .padding(4.dp)
                    ) {
                        Image(
                            painter = reactionType.commentReactionPainter,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun CommentReaction(
    reactionType: ReactionType,
    reactionsCount: Int,
    isSet: Boolean,
    onClick: (ReactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnClick by rememberUpdatedState { onClick(reactionType) }
    Box(
        modifier = modifier
            .clip(CommentDefaults.ReactionShape)
            .background(
                color = colorResource(
                    id = if (isSet) {
                        org.hyperskill.app.R.color.color_overlay_violet_alpha_38
                    } else {
                        org.hyperskill.app.R.color.color_on_surface_alpha_9
                    }
                )
            )
            .clickable(onClick = currentOnClick)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row {
            Image(
                painter = reactionType.commentReactionPainter,
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = reactionsCount.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

@Composable
private fun ShowMoreReactions(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(12.dp, 4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_reaction_show_more),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private val ReactionType.commentReactionPainter: Painter
    @Composable
    get() = painterResource(
        id = when (this) {
            ReactionType.SMILE -> R.drawable.ic_reaction_smile
            ReactionType.PLUS -> R.drawable.ic_reaction_upvote
            ReactionType.MINUS -> R.drawable.ic_reaction_downvote
            ReactionType.CONFUSED -> R.drawable.ic_reaction_confused
            ReactionType.THINKING -> R.drawable.ic_reaction_thinking
            ReactionType.FIRE -> R.drawable.ic_reaction_fire
            ReactionType.CLAP -> R.drawable.ic_reaction_clapping

            ReactionType.HELPFUL,
            ReactionType.UNHELPFUL,
            ReactionType.UNKNOWN -> error("$this reaction doesn't have an associated icon for comment reactions")
        }
    )