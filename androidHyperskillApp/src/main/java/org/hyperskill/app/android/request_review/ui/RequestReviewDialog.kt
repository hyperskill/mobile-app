package org.hyperskill.app.android.request_review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillOutlinedButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.request_review.modal.presentation.RequestReviewModalFeature.ViewState
import org.hyperskill.app.request_review.presentation.RequestReviewModalViewModel

@Composable
fun RequestReviewDialog(viewModel: RequestReviewModalViewModel) {
    val viewState: ViewState by viewModel.state.collectAsStateWithLifecycle()
    RequestReviewDialog(
        viewState = viewState,
        onPositiveButtonClick = viewModel::onPositiveButtonClick,
        onNegativeButtonClick = viewModel::onNegativeButtonClick
    )
}

@Composable
fun RequestReviewDialog(
    viewState: ViewState,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit
) {
    Column {
        BottomSheetDragIndicator(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Text(
                text = viewState.title,
                style = MaterialTheme.typography.h4
            )
            val description = viewState.description
            if (description != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.body2
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            when (viewState.state) {
                ViewState.State.AWAITING, ViewState.State.POSITIVE -> AwaitingButtons(
                    positiveButtonText = viewState.positiveButtonText,
                    negativeButtonText = viewState.negativeButtonText,
                    onPositiveButtonClick = onPositiveButtonClick,
                    onNegativeButtonClick = onNegativeButtonClick
                )
                ViewState.State.NEGATIVE -> NegativeButtons(
                    positiveButtonText = viewState.positiveButtonText,
                    negativeButtonText = viewState.negativeButtonText,
                    onPositiveButtonClick = onPositiveButtonClick,
                    onNegativeButtonClick = onNegativeButtonClick
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AwaitingButtons(
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HyperskillOutlinedButton(
            onClick = onPositiveButtonClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = positiveButtonText)
        }
        HyperskillOutlinedButton(
            onClick = onNegativeButtonClick,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = negativeButtonText
            )
        }
    }
}

@Composable
private fun NegativeButtons(
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveButtonClick: () -> Unit,
    onNegativeButtonClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HyperskillButton(
            onClick = onPositiveButtonClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = positiveButtonText)
        }
        HyperskillOutlinedButton(
            onClick = onNegativeButtonClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = negativeButtonText)
        }
    }
}

@Composable
fun BottomSheetDragIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredSize(width = 60.dp, height = 4.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(colorResource(id = R.color.layer_active_2))
    )
}

private class RequestReviewModalPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            RequestReviewPreviewDefaults.AwaitingViewState,
            RequestReviewPreviewDefaults.NegativeViewState
        )
}

@Preview
@Composable
private fun RequestReviewDialogPreview(
    @PreviewParameter(RequestReviewModalPreviewProvider::class) viewState: ViewState
) {
    HyperskillTheme {
        RequestReviewDialog(
            viewState = viewState,
            onPositiveButtonClick = {},
            onNegativeButtonClick = {}
        )
    }
}