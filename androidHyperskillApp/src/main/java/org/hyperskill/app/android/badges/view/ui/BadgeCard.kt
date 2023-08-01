package org.hyperskill.app.android.badges.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.profile.view.BadgesViewState

@Composable
fun BadgeCard(
    badge: BadgesViewState.Badge,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius)))
            .background(MaterialTheme.colors.surface)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(),
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = badge.title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            BadgeImage(
                badge = badge,
                modifier = Modifier.fillMaxWidth()
            )
            BadgeLevelWithProgress(
                level = badge.formattedCurrentLevel,
                nextLevel = badge.nextLevel,
                progress = badge.progress
            )
        }
    }
}

@Preview(widthDp = 160, heightDp = 220)
@Composable
fun BadgeCardPreview() {
    BadgeCard(
        badge = BadgeCardStatePreview.getLongTitlePreview(),
        onClick = {},
        modifier = Modifier.wrapContentHeight()
    )
}

@Preview(widthDp = 160, heightDp = 220)
@Composable
fun LastLevelBadgeCardPreview() {
    BadgeCard(
        badge = BadgeCardStatePreview.getShortTitlePreview(),
        onClick = {}
    )
}