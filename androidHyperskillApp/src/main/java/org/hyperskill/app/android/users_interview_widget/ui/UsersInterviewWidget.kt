package org.hyperskill.app.android.users_interview_widget.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature
import org.hyperskill.app.R as SharedR

@Composable
fun UsersInterviewWidget(
    viewState: UsersInterviewWidgetFeature.State,
    onNewMessage: (UsersInterviewWidgetFeature.Message) -> Unit,
    modifier: Modifier = Modifier
) {
    when (viewState) {
        UsersInterviewWidgetFeature.State.Idle,
        UsersInterviewWidgetFeature.State.Hidden -> {
            // no op
        }
        UsersInterviewWidgetFeature.State.Loading -> {
            ShimmerLoading(
                modifier = modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
        }
        UsersInterviewWidgetFeature.State.Visible -> {
            UsersInterviewWidgetContent(onNewMessage, modifier)
        }
    }
}

@Composable
private fun UsersInterviewWidgetContent(
    onNewMessage: (UsersInterviewWidgetFeature.Message) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .clip(UsersInterviewWidgetDefaults.shape)
            .border(
                width = 1.dp,
                color = colorResource(id = SharedR.color.color_on_surface_alpha_9),
                shape = UsersInterviewWidgetDefaults.shape
            )
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF7AB7FE),
                        Color(0xFF6C63FF)
                    )
                )
            )
            .clickable {
                onNewMessage(UsersInterviewWidgetFeature.Message.WidgetClicked)
            }
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {
        Text(
            text = stringResource(id = SharedR.string.users_interview_widget_title),
            color = colorResource(id = SharedR.color.text_on_color),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Box(
            modifier = Modifier
                .requiredSize(24.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    onNewMessage(UsersInterviewWidgetFeature.Message.CloseClicked)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user_questionnaire_close),
                contentDescription = null,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private object UsersInterviewWidgetDefaults {

    val shape: Shape
        @Composable
        get() = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
}

private class UsersInterviewPreviewParameterProvider :
    PreviewParameterProvider<UsersInterviewWidgetFeature.State> {
    override val values: Sequence<UsersInterviewWidgetFeature.State>
        get() = sequenceOf(
            UsersInterviewWidgetFeature.State.Loading,
            UsersInterviewWidgetFeature.State.Visible
        )
}

@Preview
@Composable
private fun UsersInterviewWidgetPreview(
    @PreviewParameter(UsersInterviewPreviewParameterProvider::class)
    state: UsersInterviewWidgetFeature.State
) {
    HyperskillTheme {
        UsersInterviewWidget(
            viewState = state,
            onNewMessage = {},
            modifier = Modifier.requiredWidth(320.dp)
        )
    }
}