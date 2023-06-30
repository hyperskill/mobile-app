package org.hyperskill.app.android.track_progress.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R

@Composable
fun PercentStatistics(
    title: @Composable RowScope.() -> Unit,
    description: String,
    progressPercent: Float,
    isTrackCompleted: Boolean,
    modifier: Modifier = Modifier,
    icon: Painter? = null
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            title()
            if (icon != null) {
                Image(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
        PercentProgressIndicator(
            progressPercent = progressPercent,
            isTrackCompleted = isTrackCompleted,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = description,
            color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TopicsProgressPreview() {
    PercentStatistics(
        title = {
            Text(text = "0 / 149 • 0%")
        },
        isTrackCompleted = false,
        progressPercent = .35f,
        icon = painterResource(id = R.drawable.ic_track_progress_core_topics),
        description = "Applied core topics by completing project stages"
    )
}