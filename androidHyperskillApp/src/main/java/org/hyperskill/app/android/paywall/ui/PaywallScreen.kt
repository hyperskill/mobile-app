package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillProgressBar
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
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
fun PaywallScreen(
    viewModel: PaywallViewModel,
    onBackClick: () -> Unit
) {
    OnComposableShownFirstTime(viewModel) {
        viewModel.onNewMessage(PaywallFeature.Message.ViewedEventMessage)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    PaywallScreen(
        viewState = state,
        onBackClick = onBackClick,
        onBuySubscriptionClick = viewModel::onBuySubscriptionClick,
        onContinueWithLimitsClick = viewModel::onContinueWithLimitsClick,
        onRetryLoadingClick = viewModel::onRetryLoadingClicked
    )
}

@Composable
fun PaywallScreen(
    viewState: ViewState,
    onBackClick: () -> Unit,
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    onRetryLoadingClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (viewState is ViewState.Content && viewState.isToolbarVisible) {
                HyperskillTopAppBar(
                    title = stringResource(id = R.string.paywall_screen_title),
                    onNavigationIconClick = onBackClick,
                    backgroundColor = colorResource(id = R.color.layer_1)
                )
            }
        }
    ) { padding ->
        when (viewState) {
            ViewState.Idle -> {
                // no op
            }
            ViewState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                    buyButtonText = viewState.buyButtonText,
                    isContinueWithLimitsButtonVisible = viewState.isContinueWithLimitsButtonVisible,
                    onBuySubscriptionClick = onBuySubscriptionClick,
                    onContinueWithLimitsClick = onContinueWithLimitsClick,
                    padding = padding
                )
        }
    }
}

private class PaywallPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState.Content(
                buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
                isToolbarVisible = true,
                isContinueWithLimitsButtonVisible = false
            ),
            ViewState.Content(
                buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
                isToolbarVisible = false,
                isContinueWithLimitsButtonVisible = true
            ),
            ViewState.Error,
            ViewState.Loading
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
            onBackClick = {},
            onBuySubscriptionClick = {},
            onContinueWithLimitsClick = {},
            onRetryLoadingClick = {}
        )
    }
}