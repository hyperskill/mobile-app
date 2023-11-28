package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import org.hyperskill.app.android.R

@Composable
fun HyperskillCard(
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = dimensionResource(id = R.dimen.corner_radius),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colors.surface)
            .apply {
                if (onClick != null) {
                    clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(),
                        onClick = onClick
                    )
                }
            }
            .padding(contentPadding),
        content = content
    )
}