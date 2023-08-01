package org.hyperskill.app.android.badges.view.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.view.BadgesViewState

@Composable
fun ProfileBadges(
    viewState: BadgesViewState,
    windowWidthSizeClass: WindowWidthSizeClass,
    onBadgeClick: (BadgeKind) -> Unit,
    onExpandButtonClick: (ProfileFeature.Message.BadgesVisibilityButton) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnBadgeClick by rememberUpdatedState(newValue = onBadgeClick)
    val currentOnExpandButtonClick = remember(viewState.isExpanded) {
        {
            onExpandButtonClick(
                if (viewState.isExpanded) {
                    ProfileFeature.Message.BadgesVisibilityButton.SHOW_LESS
                } else {
                    ProfileFeature.Message.BadgesVisibilityButton.SHOW_ALL
                }
            )
        }
    }
    Box(modifier.fillMaxWidth()) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Achievements",
                    fontSize = BadgeDefaults.titleTextSize,
                    fontWeight = BadgeDefaults.titleFontWeight,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Box(
                    Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = rememberRipple(),
                            onClick = currentOnExpandButtonClick
                        )
                ) {
                    Text(
                        text = if (viewState.isExpanded) "Show less" else "Show all",
                        fontSize = BadgeDefaults.visibilityButtonFontSize,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BadgeGrid(
                badges = viewState.badges,
                windowWidthSizeClass = windowWidthSizeClass,
                onBadgeClick = currentOnBadgeClick,
                modifier = Modifier.animateContentSize()
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "id:Nexus 6"
)
@Composable
fun ProfileBadgesPreview() {
    ProfileBadges(
        viewState = BadgesViewState(
            badges = BadgeCardStatePreview.getAllBadgesPreview(),
            isExpanded = true
        ),
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        onBadgeClick = {},
        onExpandButtonClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    )
}