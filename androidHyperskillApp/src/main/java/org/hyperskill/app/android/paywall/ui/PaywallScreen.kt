package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewStateContent
import org.hyperskill.app.paywall.presentation.PaywallViewModel

object PaywallDefaults {
    val ContentPadding = PaddingValues(
        start = 24.dp,
        end = 20.dp,
        top = 24.dp,
        bottom = 32.dp
    )

    val BackgroundColor: Color
        @Composable
        get() = colorResource(id = R.color.layer_1)
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
    val activity = LocalContext.current.findActivity()
    PaywallScreen(
        viewState = state,
        onBackClick = onBackClick,
        onBuySubscriptionClick = remember(activity) {
            {
                viewModel.onBuySubscriptionClick(activity)
            }
        },
        onContinueWithLimitsClick = viewModel::onContinueWithLimitsClick,
        onRetryLoadingClick = viewModel::onRetryLoadingClicked,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClick
    )
}

@Composable
fun PaywallScreen(
    viewState: ViewState,
    onBackClick: () -> Unit,
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    onRetryLoadingClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    Scaffold(
        topBar = {
            if (viewState.isToolbarVisible) {
                HyperskillTopAppBar(
                    title = stringResource(id = R.string.paywall_screen_title),
                    onNavigationIconClick = onBackClick,
                    backgroundColor = PaywallDefaults.BackgroundColor
                )
            }
        }
    ) { padding ->
        when (val contentState = viewState.contentState) {
            ViewStateContent.Idle -> {
                // no op
            }
            ViewStateContent.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PaywallDefaults.BackgroundColor)
                ) {
                    HyperskillProgressBar(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            ViewStateContent.Error ->
                ScreenDataLoadingError(
                    errorMessage = stringResource(id = R.string.paywall_placeholder_error_description),
                    modifier = Modifier.background(PaywallDefaults.BackgroundColor)
                ) {
                    onRetryLoadingClick()
                }
            is ViewStateContent.Content ->
                PaywallContent(
                    buyButtonText = contentState.buyButtonText,
                    isContinueWithLimitsButtonVisible = contentState.isContinueWithLimitsButtonVisible,
                    priceText = contentState.priceText,
                    onBuySubscriptionClick = onBuySubscriptionClick,
                    onContinueWithLimitsClick = onContinueWithLimitsClick,
                    onTermsOfServiceClick = onTermsOfServiceClick,
                    padding = padding
                )
            ViewStateContent.SubscriptionSyncLoading ->
                SubscriptionSyncLoading()
        }
    }
}

private class PaywallPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState(
                isToolbarVisible = true,
                contentState = ViewStateContent.Content(
                    buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
                    isContinueWithLimitsButtonVisible = false,
                    priceText = "$11.99 / month"
                )
            ),
            ViewState(
                isToolbarVisible = false,
                contentState = ViewStateContent.Content(
                    buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
                    isContinueWithLimitsButtonVisible = true,
                    priceText = PaywallPreviewDefaults.PRICE_TEXT
                )
            ),
            ViewState(
                isToolbarVisible = true,
                contentState = ViewStateContent.Error
            ),
            ViewState(
                isToolbarVisible = true,
                contentState = ViewStateContent.Loading
            ),
            ViewState(
                isToolbarVisible = true,
                contentState = ViewStateContent.SubscriptionSyncLoading
            )
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
            onRetryLoadingClick = {},
            onTermsOfServiceClick = {}
        )
    }
}