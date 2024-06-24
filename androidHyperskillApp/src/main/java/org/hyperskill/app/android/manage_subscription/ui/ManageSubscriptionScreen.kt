package org.hyperskill.app.android.manage_subscription.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillProgressBar
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.ViewState
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionViewModel

@Composable
fun ManageSubscriptionScreen(
    viewModel: ManageSubscriptionViewModel,
    onBackClick: () -> Unit
) {
    OnComposableShownFirstTime(viewModel) {
        viewModel.onNewMessage(Message.ViewedEventMessage)
    }
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    ManageSubscriptionScreen(
        viewState = viewState,
        onBackClick = onBackClick,
        onRetryLoadingClick = viewModel::onRetryClick,
        onActionButtonClick = viewModel::onActionButtonClick
    )
}

@Composable
fun ManageSubscriptionScreen(
    viewState: ViewState,
    onBackClick: () -> Unit,
    onRetryLoadingClick: () -> Unit,
    onActionButtonClick: () -> Unit
) {
    Scaffold(
        topBar = {
            HyperskillTopAppBar(
                title = stringResource(id = R.string.manage_subscription_screen_title),
                onNavigationIconClick = onBackClick,
                backgroundColor = colorResource(id = R.color.layer_1)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(WindowInsets.statusBars)
        ) {
            val insets = WindowInsets.safeDrawing
            when (viewState) {
                ViewState.Idle -> {
                    // no op
                }
                ViewState.Loading -> {
                    HyperskillProgressBar(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                ViewState.Error -> {
                    ScreenDataLoadingError(
                        errorMessage = stringResource(id = R.string.paywall_placeholder_error_description),
                        modifier = Modifier.windowInsetsPadding(insets)
                    ) {
                        onRetryLoadingClick()
                    }
                }
                is ViewState.Content -> {
                    ManageSubscriptionContent(
                        state = viewState,
                        onActionButtonClick = onActionButtonClick,
                        padding = padding
                    )
                }
            }
        }
    }
}

private class ManageSubscriptionPreviewProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState>
        get() = sequenceOf(
            ManageSubscriptionPreviewDefaults.ActiveSubscriptionContent,
            ManageSubscriptionPreviewDefaults.ExpiredSubscriptionContent,
            ViewState.Loading,
            ViewState.Error
        )
}

@Preview(showBackground = true)
@Composable
fun ManageSubscriptionScreenPreview(
    @PreviewParameter(ManageSubscriptionPreviewProvider::class) viewState: ViewState
) {
    HyperskillTheme {
        ManageSubscriptionScreen(
            viewState = viewState,
            onBackClick = {},
            onActionButtonClick = {},
            onRetryLoadingClick = {}
        )
    }
}