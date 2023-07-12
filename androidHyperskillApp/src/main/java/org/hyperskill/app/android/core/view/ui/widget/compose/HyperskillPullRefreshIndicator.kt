package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HyperskillPullRefreshIndicator(
    refreshing: Boolean,
    state: PullRefreshState,
    modifier: Modifier = Modifier
) {
    PullRefreshIndicator(
        refreshing = refreshing,
        state = state,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier
    )
}