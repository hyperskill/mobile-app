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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.compose.plus
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButtonDefaults
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun PaywallContent(
    buyButtonText: String,
    isContinueWithLimitsButtonVisible: Boolean,
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.layer_1))
            .padding(PaywallDefaults.ContentPadding + padding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_paywall),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.paywall_mobile_only_title),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )
            SubscriptionDetails()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HyperskillButton(
                onClick = onBuySubscriptionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buyButtonText)
            }
            if (isContinueWithLimitsButtonVisible) {
                HyperskillButton(
                    onClick = onContinueWithLimitsClick,
                    colors = HyperskillButtonDefaults.buttonColors(colorResource(id = R.color.layer_1)),
                    border = BorderStroke(1.dp, colorResource(id = R.color.button_tertiary)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.paywall_mobile_only_continue_btn),
                        color = colorResource(id = R.color.button_tertiary)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PaywallContentPreview() {
    HyperskillTheme {
        PaywallContent(
            buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
            isContinueWithLimitsButtonVisible = true,
            onBuySubscriptionClick = {},
            onContinueWithLimitsClick = {}
        )
    }
}