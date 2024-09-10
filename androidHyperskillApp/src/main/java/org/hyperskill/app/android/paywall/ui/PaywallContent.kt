package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.paywall.presentation.PaywallFeature

@Composable
fun PaywallContent(
    buyButtonText: String,
    products: List<PaywallFeature.ViewStateContent.SubscriptionProduct>,
    onProductClick: (String) -> Unit,
    onTermsOfServiceClick: () -> Unit,
    onBuySubscriptionClick: () -> Unit,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaywallDefaults.BackgroundColor)
            .padding(PaywallDefaults.ContentPadding + padding)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_paywall),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.paywall_mobile_only_title),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(24.dp))
            SubscriptionDetails()
            Spacer(modifier = Modifier.height(24.dp))
            SubscriptionProducts(
                products = products,
                onProdutsClick = onProductClick
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        Column {
            HyperskillButton(
                onClick = onBuySubscriptionClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = buyButtonText)
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
private fun SubscriptionProducts(
    products: List<PaywallFeature.ViewStateContent.SubscriptionProduct>,
    onProdutsClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        products.forEach { option ->
            key(option.productId) {
                SubscriptionProduct(
                    product = option,
                    onClick = remember {
                        {
                            onProdutsClick(option.productId)
                        }
                    }
                )
            }
        }
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

@Preview(showSystemUi = true)
@Composable
fun PaywallContentPreview() {
    HyperskillTheme {
        val options by remember {
            mutableStateOf(
                PaywallPreviewDefaults.subscriptionProducts
            )
        }
        PaywallContent(
            buyButtonText = PaywallPreviewDefaults.BUY_BUTTON_TEXT,
            products = options,
            onProductClick = { },
            onTermsOfServiceClick = {},
            onBuySubscriptionClick = {}
        )
    }
}