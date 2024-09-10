package org.hyperskill.app.android.paywall.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.R as SharedR

@Composable
fun SubscriptionProduct(
    product: PaywallFeature.ViewStateContent.SubscriptionProduct,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnClick by rememberUpdatedState(newValue = onClick)
    if (product.isBestValue) {
        BestValueSubscriptionProduct(
            title = product.title,
            subtitle = product.subtitle,
            isSelected = product.isSelected,
            modifier = modifier,
            onClick = currentOnClick
        )
    } else {
        SubscriptionProduct(
            title = product.title,
            subtitle = product.subtitle,
            isSelected = product.isSelected,
            modifier = modifier,
            onClick = currentOnClick
        )
    }
}

@Composable
fun SubscriptionProduct(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = colorResource(
            id = if (isSelected) {
                SharedR.color.color_overlay_blue
            } else SharedR.color.color_on_surface_alpha_12
        ),
        label = "Border color"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "Border width"
    )
    val view = LocalView.current
    val verticalPadding by animateDpAsState(
        targetValue = if (isSelected) 34.dp else 18.dp,
        label = "Vertical padding",
        finishedListener = {
            view.performHapticFeedback(HapticFeedbackConstantsCompat.TOGGLE_ON)
        }
    )
    val textColor by animateColorAsState(
        targetValue = colorResource(
            if (isSelected) {
                SharedR.color.color_on_surface
            } else {
                SharedR.color.color_on_surface_alpha_60
            }
        ),
        label = "Text color"
    )
    SubscriptionProduct(
        title = title,
        subtitle = subtitle,
        borderColor = borderColor,
        borderWidth = borderWidth,
        verticalPadding = verticalPadding,
        textColor = textColor,
        modifier = modifier,
        onClick = onClick
    )
}

@Composable
fun SubscriptionProduct(
    title: String,
    subtitle: String,
    borderColor: Color,
    borderWidth: Dp,
    verticalPadding: Dp,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius)))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = subtitle,
                color = textColor
            )
        }
    }
}

private const val BestValueLayoutId = "Best value tag"

@Suppress("MagicNumber")
@Composable
fun BestValueSubscriptionProduct(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Layout(
        modifier = modifier,
        content = {
            SubscriptionProduct(
                title = title,
                subtitle = subtitle,
                isSelected = isSelected,
                onClick = onClick
            )
            Text(
                text = stringResource(id = SharedR.string.paywall_best_value_label),
                color = colorResource(id = SharedR.color.color_on_error),
                modifier = Modifier
                    .layoutId(BestValueLayoutId)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(id = SharedR.color.color_overlay_blue))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    ) { measurables, constraints ->
        val tagMeasurable = measurables.first {
            it.layoutId == BestValueLayoutId
        }
        val optionMeasurable = (measurables - tagMeasurable).first()

        val tagPlaceable = tagMeasurable.measure(constraints)
        val optionPlaceable = optionMeasurable.measure(constraints)

        val optionTopPadding = tagPlaceable.height / 2
        val optionEndPadding = optionTopPadding / 2

        layout(
            width = optionPlaceable.width,
            height = optionPlaceable.height + optionTopPadding
        ) {
            optionPlaceable.place(x = 0, y = optionTopPadding)
            tagPlaceable.place(
                x = optionPlaceable.width - tagPlaceable.width + optionEndPadding,
                y = 0
            )
        }
    }
}

private class SubscriptionProductPreviewProvider :
    PreviewParameterProvider<PaywallFeature.ViewStateContent.SubscriptionProduct> {
    override val values: Sequence<PaywallFeature.ViewStateContent.SubscriptionProduct>
        get() = sequenceOf(
            PaywallFeature.ViewStateContent.SubscriptionProduct(
                productId = "1",
                title = "Annual 100$",
                subtitle = "$8.33 / month",
                isBestValue = true,
                isSelected = true
            ),
            PaywallFeature.ViewStateContent.SubscriptionProduct(
                productId = "2",
                title = "Monthly",
                subtitle = "$12 / month",
                isBestValue = false,
                isSelected = false
            )
        )
}

@Preview(showBackground = true)
@Composable
private fun SubscriptionProductPreview(
    @PreviewParameter(provider = SubscriptionProductPreviewProvider::class)
    option: PaywallFeature.ViewStateContent.SubscriptionProduct
) {
    HyperskillTheme {
        var mutableOption by remember {
            mutableStateOf(option)
        }
        SubscriptionProduct(
            product = mutableOption,
            onClick = {
                mutableOption = mutableOption.copy(isSelected = !mutableOption.isSelected)
            },
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}