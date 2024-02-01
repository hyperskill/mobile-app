package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.findActivity
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillProgressBar
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.paywall.presentation.PaywallViewModel

object PaywallDefaults {
    val ContentPadding = PaddingValues(
        start = 24.dp,
        end = 20.dp,
        top = 24.dp,
        bottom = 32.dp
    )
}

@Composable
fun PaywallScreen(viewModel: PaywallViewModel) {
    OnComposableShownFirstTime(viewModel) {
        viewModel.onNewMessage(PaywallFeature.Message.ViewedEventMessage)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val activity = LocalContext.current.findActivity()
    PaywallScreen(
        viewState = state,
        onBuySubscriptionClick = remember(activity) {
            {
                viewModel.onBuySubscriptionClick(activity)
            }
        },
        onContinueWithLimitsClick = viewModel::onContinueWithLimitsClick,
        onRetryLoadingClick = viewModel::onRetryLoadingClicked
    )
}

@Composable
fun PaywallScreen(
    viewState: ViewState,
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    onRetryLoadingClick: () -> Unit
) {
    when (viewState) {
        ViewState.Idle -> {
            // no op
        }
        ViewState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                HyperskillProgressBar(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        ViewState.Error ->
            ScreenDataLoadingError(
                errorMessage = stringResource(id = R.string.paywall_placeholder_error_description)
            ) {
                onRetryLoadingClick()
            }
        is ViewState.Content ->
            PaywallContent(
                viewState.buyButtonText,
                onBuySubscriptionClick,
                onContinueWithLimitsClick
            )
    }
}

private class PaywallPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState.Loading,
            ViewState.Error,
            ViewState.Content("Subscribe for $12.00/month")
        )
}

@Preview(showBackground = true)
@Composable
fun PaywallScreenPreview(
    @PreviewParameter(provider = PaywallPreviewProvider::class) viewState: ViewState
) {
    HyperskillTheme {
        PaywallScreen(
            viewState = viewState,
            onBuySubscriptionClick = {},
            onContinueWithLimitsClick = {},
            onRetryLoadingClick = {}
        )
    }
}