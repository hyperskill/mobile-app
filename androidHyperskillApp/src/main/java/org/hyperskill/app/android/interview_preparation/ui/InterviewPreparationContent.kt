package org.hyperskill.app.android.interview_preparation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.interview_preparation.view.model.InterviewPreparationWidgetViewState

private fun Modifier.squareModifier(): Modifier =
    this.size(32.dp).clip(RoundedCornerShape(8.dp))

@Composable
fun InterviewPreparationContent(
    content: InterviewPreparationWidgetViewState.Content,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HyperskillCard(
        contentPadding = PaddingValues(20.dp),
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.interview_preparation_widget_title),
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.color_on_surface_alpha_87),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
                Box(
                    modifier = Modifier
                        .squareModifier()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    colorResource(id = R.color.color_blue_200),
                                    colorResource(id = R.color.color_blue_400)
                                )
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(
                            id = org.hyperskill.app.android.R.drawable.ic_home_screen_right_arrow_light
                        ),
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .squareModifier()
                        .background(colorResource(id = R.color.color_overlay_blue_alpha_12))
                ) {
                    Text(
                        text = content.formattedStepsCount,
                        style = MaterialTheme.typography.body1,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.color_on_surface_alpha_87),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Text(
                    text = content.description,
                    style = MaterialTheme.typography.body1,
                    color = colorResource(id = R.color.color_on_surface_alpha_60),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview
@Composable
private fun InterviewPreparationWidgetViewState() {
    HyperskillTheme {
        InterviewPreparationContent(
            content = InterviewPreparationWidgetViewState.Content(
                formattedStepsCount = "50",
                description = "problems at the hard level to solve"
            ),
            onClick = {}
        )
    }
}