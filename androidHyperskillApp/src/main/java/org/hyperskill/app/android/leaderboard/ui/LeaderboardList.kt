@file:OptIn(ExperimentalFoundationApi::class)

package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem

private enum class LeaderboardWidgetListItemType {
    ITEM,
    SEPARATOR
}

@Composable
fun LeaderboardList(
    items: List<LeaderboardWidgetListItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items,
            key = { item ->
                when (item) {
                    LeaderboardWidgetListItem.Separator -> LeaderboardWidgetListItemType.SEPARATOR
                    is LeaderboardWidgetListItem.UserInfo -> item.position
                }
            },
            contentType = { item ->
                when (item) {
                    LeaderboardWidgetListItem.Separator -> LeaderboardWidgetListItemType.SEPARATOR
                    is LeaderboardWidgetListItem.UserInfo -> LeaderboardWidgetListItemType.ITEM
                }
            }
        ) { item ->
            when (item) {
                LeaderboardWidgetListItem.Separator -> {
                    LeaderboardListDivider(
                        modifier = Modifier.animateItemPlacement()
                    )
                }
                is LeaderboardWidgetListItem.UserInfo -> {
                    LeaderboardItem(
                        placeNumber = item.position,
                        participantAvatarUrl = item.userAvatar,
                        participantName = item.userName,
                        solvedProblemsAmount = item.passedProblems,
                        problemsSubtitle = item.passedProblemsSubtitle,
                        isHighlighted = item.isCurrentUser,
                        modifier = Modifier.animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LeaderboardListPreview() {
    HyperskillTheme {
        LeaderboardList(
            items = LeaderboardPreviewData.listData
        )
    }
}