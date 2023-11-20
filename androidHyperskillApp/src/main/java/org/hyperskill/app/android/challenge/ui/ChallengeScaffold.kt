package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
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
        modifier = modifier.border(
            width = 1.dp,
            color = colorResource(id = R.color.color_on_surface_alpha_9),
            shape = RoundedCornerShape(
                dimensionResource(id = org.hyperskill.app.android.R.dimen.corner_radius)
            )
        )
    ) {
        Column(
            content = content,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
    }
}