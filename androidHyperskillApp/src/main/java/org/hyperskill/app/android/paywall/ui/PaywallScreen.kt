package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButtonDefaults
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallViewModel
import org.hyperskill.app.R as SharedR

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
    PaywallScreen(
        onBuySubscriptionClick = viewModel::onBuySubscriptionClick,
        onContinueWithLimitsClick = viewModel::onContinueWithLimitsClick
    )
}

@Composable
fun PaywallScreen(
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = SharedR.color.layer_1))
            .padding(PaywallDefaults.ContentPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_paywall),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            PaywallSubscriptionDetails()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HyperskillButton(
                onClick = onBuySubscriptionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = SharedR.string.paywall_mobile_only_buy_btn))
            }
            HyperskillButton(
                onClick = onContinueWithLimitsClick,
                colors = HyperskillButtonDefaults.buttonColors(colorResource(id = SharedR.color.layer_1)),
                border = BorderStroke(1.dp, colorResource(id = SharedR.color.button_tertiary)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = SharedR.string.paywall_mobile_only_continue_btn),
                    color = colorResource(id = SharedR.color.button_tertiary)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaywallScreenPreview() {
    HyperskillTheme {
        PaywallScreen(
            onBuySubscriptionClick = {},
            onContinueWithLimitsClick = {}
        )
    }
}