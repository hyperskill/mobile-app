package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
        onCloseClick = viewModel::onCloseClick,
        onRetryLoadingClick = viewModel::onRetryLoadingClicked,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClick
    )
}

@Composable
fun PaywallScreen(
    viewState: ViewState,
    onBackClick: () -> Unit,
    onBuySubscriptionClick: () -> Unit,
    onCloseClick: () -> Unit,
    onRetryLoadingClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    PaywallScaffold(
        isToolbarVisible = viewState.isToolbarVisible,
        onBackClick = onBackClick,
        onCloseClick = onCloseClick
    ) { padding ->
        val insets = WindowInsets.safeDrawing
        when (val contentState = viewState.contentState) {
            ViewStateContent.Idle -> {
                // no op
            }
            ViewStateContent.Loading -> {
                HyperskillProgressBar(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
            ViewStateContent.Error ->
                ScreenDataLoadingError(
                    errorMessage = stringResource(id = R.string.paywall_placeholder_error_description),
                    modifier = Modifier
                        .background(PaywallDefaults.BackgroundColor)
                        .windowInsetsPadding(insets)
                ) {
                    onRetryLoadingClick()
                }
            is ViewStateContent.Content ->
                PaywallContent(
                    buyButtonText = contentState.buyButtonText,
                    priceText = contentState.priceText,
                    onBuySubscriptionClick = onBuySubscriptionClick,
                    onTermsOfServiceClick = onTermsOfServiceClick,
                    padding = padding
                )
            ViewStateContent.SubscriptionSyncLoading ->
                SubscriptionSyncLoading(
                    modifier = Modifier.windowInsetsPadding(insets)
                )
        }
    }
}

@Composable
private fun PaywallScaffold(
    isToolbarVisible: Boolean,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (isToolbarVisible) {
                HyperskillTopAppBar(
                    title = stringResource(id = R.string.paywall_screen_title),
                    onNavigationIconClick = onBackClick,
                    backgroundColor = PaywallDefaults.BackgroundColor,
                )
            }
        },
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PaywallDefaults.BackgroundColor)
        ) {
            if (!isToolbarVisible) {
                CloseButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(12.dp)
                        .windowInsetsPadding(WindowInsets.statusBars)
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .consumeStatusBarInsets(isToolbarVisible),
                content = {
                    content(padding)
                }
            )
        }
    }
}

@Composable
private fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .requiredSize(32.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = org.hyperskill.app.android.R.drawable.ic_close_topic_completed),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private class PaywallPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ViewState(
                isToolbarVisible = true,
                contentState = ViewStateContent.Content(
                    buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
                    priceText = "$11.99 / month"
                )
            ),
            ViewState(
                isToolbarVisible = false,
                contentState = ViewStateContent.Content(
                    buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
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

private fun Modifier.consumeStatusBarInsets(isToolbarVisible: Boolean): Modifier =
    composed {
        if (isToolbarVisible) {
            consumeWindowInsets(WindowInsets.statusBars)
        } else {
            this
        }
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
            onCloseClick = {},
            onRetryLoadingClick = {},
            onTermsOfServiceClick = {}
        )
    }
}