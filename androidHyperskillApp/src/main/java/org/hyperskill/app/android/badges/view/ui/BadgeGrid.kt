package org.hyperskill.app.android.badges.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.view.BadgesViewState

@Composable
fun BadgeGrid(
    badges: List<BadgesViewState.Badge>,
    windowWidthSizeClass: WindowWidthSizeClass,
    onBadgeClick: (BadgeKind) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowedBadges = remember(windowWidthSizeClass, badges) {
        val columnsCount = if (windowWidthSizeClass >= WindowWidthSizeClass.Medium) 4 else 2
        badges.windowed(size = columnsCount, step = columnsCount)
    }

    /**
     * Column of Rows is used instead of LazyVerticalGrid because
     * to align elements in Row by height.
     */
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        windowedBadges.forEach { badgesInRow ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                badgesInRow.forEach { badge ->
                    key(badge) {
                        val currentOnBadgeClick = remember {
                            {
                                onBadgeClick(badge.kind)
                            }
                        }
                        BadgeCard(
                            badge = badge,
                            onClick = currentOnBadgeClick,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3WindowSizeClassApi
@Composable
@Preview(
    device = "id:Nexus 6",
    showBackground = true
)
fun PhoneBadgeGridPreview() {
    BadgeGrid(
        badges = BadgeCardStatePreview.getAllBadgesPreview(),
        windowWidthSizeClass = WindowWidthSizeClass.Compact,
        onBadgeClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    )
}

@ExperimentalMaterial3WindowSizeClassApi
@Composable
@Preview(
    device = "spec:parent=Nexus 10,orientation=portrait",
    showBackground = true
)
fun TabletBadgeGridPreview() {
    BadgeGrid(
        badges = BadgeCardStatePreview.getAllBadgesPreview(),
        windowWidthSizeClass = WindowWidthSizeClass.Medium,
        onBadgeClick = {},
        modifier = Modifier.fillMaxWidth()
    )
}