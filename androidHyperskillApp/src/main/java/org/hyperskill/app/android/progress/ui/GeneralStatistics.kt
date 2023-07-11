package org.hyperskill.app.android.progress.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun GeneralStatistics(
    title: String,
    icon: Painter,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            )
            .padding(ProgressDefaults.CardInnerPadding),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title)
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Text(
            text = description,
            color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TrackStatisticsPreview() {
    GeneralStatistics(
        title = "~ 56 h",
        icon = painterResource(id = R.drawable.ic_track_progress_time),
        description = "Time to complete the track",
        modifier = Modifier.width(150.dp)
    )
}