package org.hyperskill.app.android.leaderboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.leaderboard.model.LeaderboardTab

@Composable
fun LeaderboardScreen() {
    var selectedTab by remember {
        mutableStateOf(LeaderboardTab.DAY)
    }
    Column {
        LeaderboardTabs(selectedTab) { newSelectedTab ->
            selectedTab = newSelectedTab
        }
    }
}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    HyperskillTheme {
        LeaderboardScreen()
    }
}