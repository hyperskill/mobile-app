package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.leaderboard.model.LeaderboardTab

@Composable
fun LeaderboardTabs(
    selectedTab: LeaderboardTab,
    onTabClick: (LeaderboardTab) -> Unit
) {
    val tabs = remember {
        LeaderboardTab.values()
    }
    TabRow(
        selectedTabIndex = selectedTab.index,
        backgroundColor = colorResource(id = R.color.color_background),
        contentColor = MaterialTheme.colors.primary
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab.index == index,
                text = {
                    Text(
                        text = getTabTitle(tab),
                        color = if (selectedTab.index == index) {
                            MaterialTheme.colors.primary
                        } else {
                            colorResource(id = R.color.color_on_surface_alpha_60)
                        },
                        fontWeight = FontWeight.Medium
                    )
                },
                onClick = {
                    onTabClick(
                        when (index) {
                            LeaderboardTab.DAY.index -> LeaderboardTab.DAY
                            LeaderboardTab.WEEK.index -> LeaderboardTab.WEEK
                            else -> error("There is no Tab with index = $index")
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun getTabTitle(tab: LeaderboardTab): String =
    stringResource(
        id = when (tab) {
            LeaderboardTab.DAY -> R.string.leaderboard_tab_day_title
            LeaderboardTab.WEEK -> R.string.leaderboard_tab_week_title
        }
    )

@Preview
@Composable
private fun LeaderboardTabsPreview() {
    HyperskillTheme {
        LeaderboardTabs(LeaderboardTab.DAY) {}
    }
}