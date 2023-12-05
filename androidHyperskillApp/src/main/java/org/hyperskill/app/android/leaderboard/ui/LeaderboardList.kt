package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillPullRefreshIndicator
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.leaderboard.widget.view.model.LeaderboardWidgetListItem

private enum class LeaderboardWidgetListItemType {
    ITEM,
    SEPARATOR
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshableLeaderboardList(
    items: List<LeaderboardWidgetListItem>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )
    Box(
        modifier = modifier.pullRefresh(pullRefreshState),
        contentAlignment = Alignment.Center
    ) {
        LeaderboardList(
            items = items,
            onClick = onItemClick,
            modifier = Modifier.wrapContentSize()
        )
        HyperskillPullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LeaderboardList(
    items: List<LeaderboardWidgetListItem>,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items,
            key = { item ->
                when (item) {
                    LeaderboardWidgetListItem.Separator -> LeaderboardWidgetListItemType.SEPARATOR
                    is LeaderboardWidgetListItem.UserInfo -> item.userId
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
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable {
                                onClick(item.userId)
                            }
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
            items = LeaderboardPreviewData.listData,
            onClick = {}
        )
    }
}