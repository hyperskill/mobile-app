package org.hyperskill.app.android.progress.ui.track

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import org.hyperskill.app.R
import org.hyperskill.app.android.progress.ui.PercentStatistics

@Composable
fun TrackTopicsStatistics(
    countString: String,
    percentageString: String,
    progressPercent: Float,
    isTrackCompleted: Boolean,
    icon: Painter,
    description: String,
    modifier: Modifier = Modifier
) {
    PercentStatistics(
        title = {
            Text(
                text = getProgressAnnotatedTitle(
                    countString = countString,
                    percentageString = percentageString
                )
            )
        },
        description = description,
        progressPercent = progressPercent,
        isTrackCompleted = isTrackCompleted,
        icon = icon,
        modifier = modifier
    )
}

@Composable
private fun getProgressAnnotatedTitle(
    countString: String,
    percentageString: String
) =
    buildAnnotatedString {
        withStyle(SpanStyle(colorResource(id = R.color.color_on_surface))) {
            append(countString)
        }
        append(' ')
        withStyle(SpanStyle(colorResource(id = R.color.color_on_surface_alpha_38))) {
            append(percentageString)
        }
    }

@Composable
@Preview(showBackground = true)
fun CompletedTopicsProgressPreview() {
    TrackTopicsStatistics(
        countString = "0 / 149",
        percentageString = "• 0%",
        isTrackCompleted = true,
        progressPercent = .5f,
        icon = painterResource(id = org.hyperskill.app.android.R.drawable.ic_track_progress_topics),
        description = "Completed topics"
    )
}