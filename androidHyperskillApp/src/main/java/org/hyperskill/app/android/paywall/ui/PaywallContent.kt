package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.compose.plus
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButtonDefaults
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun PaywallContent(
    buyButtonText: String,
    priceText: String?,
    isContinueWithLimitsButtonVisible: Boolean,
    onTermsOfServiceClick: () -> Unit,
    onBuySubscriptionClick: () -> Unit,
    onContinueWithLimitsClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaywallDefaults.BackgroundColor)
            .padding(PaywallDefaults.ContentPadding + padding)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_paywall),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.paywall_mobile_only_title),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubscriptionDetails()
            Spacer(modifier = Modifier.height(32.dp))
            if (priceText != null) {
                SubscriptionPrice(priceText)
            }
        }
        Column {
            HyperskillButton(
                onClick = onBuySubscriptionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buyButtonText)
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (isContinueWithLimitsButtonVisible) {
                ContinueButton(onClick = onContinueWithLimitsClick)
            }
            Spacer(modifier = Modifier.height(20.dp))
            TermsOfService(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(onClick = onTermsOfServiceClick)
            )
        }
    }
}

@Composable
private fun SubscriptionPrice(
    priceText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.paywall_android_subscription_duration),
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = priceText,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HyperskillButton(
        onClick = onClick,
        colors = HyperskillButtonDefaults.buttonColors(colorResource(id = R.color.layer_1)),
        border = BorderStroke(1.dp, colorResource(id = R.color.button_tertiary)),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.paywall_mobile_only_continue_btn),
            color = colorResource(id = R.color.button_tertiary)
        )
    }
}

@Composable
private fun TermsOfService(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.paywall_tos_and_privacy_bth),
        fontSize = 12.sp,
        color = colorResource(id = R.color.color_on_surface_alpha_60),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview
@Composable
fun PaywallContentPreview() {
    HyperskillTheme {
        PaywallContent(
            buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
            isContinueWithLimitsButtonVisible = true,
            priceText = PaywallPreviewDefaults.PRICE_TEXT,
            onTermsOfServiceClick = {},
            onBuySubscriptionClick = {},
            onContinueWithLimitsClick = {}
        )
    }
}