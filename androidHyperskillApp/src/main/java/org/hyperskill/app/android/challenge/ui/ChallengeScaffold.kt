package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard

@Composable
fun ChallengeScaffold(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    HyperskillCard(
        contentPadding = ChallengeCardDefaults.paddingValues,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            content = content,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
    }
}