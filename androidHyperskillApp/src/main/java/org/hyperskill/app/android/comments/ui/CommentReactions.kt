package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.PopupState
import org.hyperskill.app.comments.domain.model.CommentReaction
import org.hyperskill.app.reactions.domain.model.ReactionType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentReactions(
    reactions: List<CommentReaction>,
    onReactionClick: (ReactionType) -> Unit,
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
            onReactionClick = onReactionClick,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
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
    onReactionClick: (ReactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val reactionPopupState = remember { PopupState() }
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.Center)
                .clickable {
                    reactionPopupState.isVisible =
                        !reactionPopupState.isVisible
                }
                .padding(12.dp, 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_reaction_show_more),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        CommentReactionsPopup(
            popupState = reactionPopupState,
            onReactionClick = { reactionType ->
                reactionPopupState.isVisible = false
                onReactionClick(reactionType)
            },
            onDismissRequest = { reactionPopupState.isVisible = false }
        )
    }
}

val ReactionType.commentReactionPainter: Painter
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