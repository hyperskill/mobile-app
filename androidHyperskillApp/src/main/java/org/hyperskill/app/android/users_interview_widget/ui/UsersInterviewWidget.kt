package org.hyperskill.app.android.users_interview_widget.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.android.core.view.ui.widget.compose.linearGradient
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
    Box(
        modifier = modifier
            .clip(UsersInterviewWidgetDefaults.shape)
            .border(
                width = 1.dp,
                color = colorResource(id = SharedR.color.color_on_surface_alpha_12),
                shape = UsersInterviewWidgetDefaults.shape
            )
            .background(UsersInterviewWidgetDefaults.BackgroundBrush)
            .clickable {
                onNewMessage(UsersInterviewWidgetFeature.Message.WidgetClicked)
            }
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        ) {
            UsersInterviewText(
                modifier = Modifier
                    .weight(weight = 1f)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(id = R.drawable.img_user_interview_inv),
                contentDescription = null
            )
        }
        UsersInterviewCloseButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            onNewMessage(UsersInterviewWidgetFeature.Message.CloseClicked)
        }
    }
}

@Composable
private fun UsersInterviewText(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = SharedR.string.users_interview_widget_title),
            color = colorResource(id = SharedR.color.text_on_color),
            style = MaterialTheme.typography.caption,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = SharedR.string.users_interview_widget_subtitle),
            color = colorResource(id = SharedR.color.text_on_color),
            style = MaterialTheme.typography.subtitle1,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun UsersInterviewCloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .requiredSize(24.dp)
            .clickable {
                onClick()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_user_interview_close),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private object UsersInterviewWidgetDefaults {

    val shape: Shape
        @Composable
        get() = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))

    val BackgroundBrush: Brush =
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF000000),
                Color(0xFF6C63FF)
            ),
            angleInDegrees = 300f
        )
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
            modifier = Modifier.requiredWidth(360.dp)
        )
    }
}