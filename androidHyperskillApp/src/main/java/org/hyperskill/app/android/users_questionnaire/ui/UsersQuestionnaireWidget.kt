package org.hyperskill.app.android.users_questionnaire.ui

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
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature
import org.hyperskill.app.R as SharedR

object UsersQuestionnaireWidgetDefaults {

    val shape: Shape
        @Composable
        get() = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
}

@Composable
fun UsersQuestionnaireWidget(
    viewState: UsersQuestionnaireWidgetFeature.State,
    onNewMessage: (UsersQuestionnaireWidgetFeature.Message) -> Unit,
    modifier: Modifier = Modifier
) {
    when (viewState) {
        UsersQuestionnaireWidgetFeature.State.Idle,
        UsersQuestionnaireWidgetFeature.State.Hidden -> {
            // no op
        }
        UsersQuestionnaireWidgetFeature.State.Loading -> {
            ShimmerLoading(
                modifier = modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )
        }
        UsersQuestionnaireWidgetFeature.State.Visible -> {
            UsersQuestionnaireWidgetContent(onNewMessage, modifier)
        }
    }
}

@Composable
private fun UsersQuestionnaireWidgetContent(
    onNewMessage: (UsersQuestionnaireWidgetFeature.Message) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .clip(UsersQuestionnaireWidgetDefaults.shape)
            .border(
                width = 1.dp,
                color = colorResource(id = SharedR.color.color_on_surface_alpha_9),
                shape = UsersQuestionnaireWidgetDefaults.shape
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
                onNewMessage(UsersQuestionnaireWidgetFeature.Message.WidgetClicked)
            }
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {
        Text(
            text = stringResource(id = SharedR.string.users_questionnaire_widget_title),
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
                    onNewMessage(UsersQuestionnaireWidgetFeature.Message.CloseClicked)
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

private class UsersQuestionnairePreviewParameterProvider :
    PreviewParameterProvider<UsersQuestionnaireWidgetFeature.State> {
    override val values: Sequence<UsersQuestionnaireWidgetFeature.State>
        get() = sequenceOf(
            UsersQuestionnaireWidgetFeature.State.Loading,
            UsersQuestionnaireWidgetFeature.State.Visible
        )
}

@Preview
@Composable
private fun UsersQuestionnaireWidgetPreview(
    @PreviewParameter(UsersQuestionnairePreviewParameterProvider::class)
    state: UsersQuestionnaireWidgetFeature.State
) {
    HyperskillTheme {
        UsersQuestionnaireWidget(
            viewState = state,
            onNewMessage = {},
            modifier = Modifier.requiredWidth(320.dp)
        )
    }
}