package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R

object HyperskillButtonDefaults {
    private val ButtonVerticalPadding = 14.dp
    private val ButtonHorizontalPadding = 16.dp

    private val TextButtonVerticalPadding = 8.dp

    val ContentPadding = PaddingValues(
        vertical = ButtonVerticalPadding,
        horizontal = ButtonHorizontalPadding
    )

    val TextButtonContentPadding = PaddingValues(
        vertical = TextButtonVerticalPadding,
        horizontal = ButtonHorizontalPadding
    )

    @Composable
    fun buttonColors(
        backgroundColor: Color = colorResource(id = R.color.button_primary)
    ): ButtonColors =
        ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        )

    @Composable
    fun textButtonColors(): ButtonColors =
        ButtonDefaults.textButtonColors()
}

@Composable
fun HyperskillButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = HyperskillButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = HyperskillButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun HyperskillTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = HyperskillButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = HyperskillButtonDefaults.TextButtonContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        content = {
            ProvideTextStyle(value = MaterialTheme.typography.textButton) {
                content()
            }
        }
    )
}