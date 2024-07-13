package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillPopup
import org.hyperskill.app.android.core.view.ui.widget.compose.PopupState
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.reactions.domain.model.commentReactions

private val CommentReactions = ReactionType.commentReactions
private const val MaxReactionsInPopupRow = 4

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommentReactionsPopup(
    popupState: PopupState,
    onReactionClick: (ReactionType) -> Unit,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null
) {
    HyperskillPopup(
        popupState = popupState,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = 0.dp, y = 8.dp),
        modifier = modifier
    ) {
        val shadowColor = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
        HyperskillCard(
            contentPadding = PaddingValues(16.dp),
            background = colorResource(id = org.hyperskill.app.R.color.color_surface),
            modifier = Modifier.shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius)),
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
        ) {
            FlowRow(
                maxItemsInEachRow = MaxReactionsInPopupRow,
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