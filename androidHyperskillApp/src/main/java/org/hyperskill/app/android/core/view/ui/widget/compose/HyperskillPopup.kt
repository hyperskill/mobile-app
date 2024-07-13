package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties

/**
 * Composable for displaying a popup with anchor and appearing/disappearing animation.
 *
 * @param popupState the [PopupState] used to track whether the popup is visible
 * @param onDismissRequest callback to be invoked when the popup is dismissed
 * @param modifier [Modifier] used to modify the composable's layout and behavior
 * @param offset the [DpOffset] to apply to the popup's position
 * @param properties the [PopupProperties] used to configure the popup
 * @param content the [BoxScope] composable that defines the popup's content
 */
@Composable
fun HyperskillPopup(
    popupState: PopupState,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable BoxScope.() -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }
    LaunchedEffect(key1 = popupState.isVisible) {
        expandedState.targetState = popupState.isVisible
    }

    // Only show the popup if it's visible.
    if (expandedState.currentState || expandedState.targetState) {
        HyperskillPopup(
            popupState = popupState,
            expandedState = expandedState,
            onDismissRequest = onDismissRequest,
            offset = offset,
            properties = properties,
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun HyperskillPopup(
    popupState: PopupState,
    expandedState: MutableTransitionState<Boolean>,
    onDismissRequest: (() -> Unit)?,
    offset: DpOffset,
    properties: PopupProperties,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val popupPositionProvider = remember(density, offset) {
        HyperskillPopupPositionProvider(
            contentOffset = offset,
            density = density
        ) { popupPositionResult ->
            // Update the PopupState's alignment and direction.
            popupState.horizontalAlignment = popupPositionResult.horizontalAlignment
            popupState.isTop = !popupPositionResult.fitTop
        }
    }

    Popup(
        onDismissRequest = onDismissRequest,
        popupPositionProvider = popupPositionProvider,
        properties = properties
    ) {
        HyperskillPopupContent(
            expandedState = expandedState,
            transformOrigin = TransformOrigin(
                pivotFractionX = when (popupState.horizontalAlignment) {
                    Alignment.Start -> 0f
                    Alignment.CenterHorizontally -> 0.5f
                    else -> 1f
                },
                pivotFractionY = if (popupState.isTop) 1f else 0f
            ),
            modifier = modifier,
            content = content
        )
    }
}

@Stable
class PopupState(
    isVisible: Boolean = false
) {
    /**
     * Horizontal alignment from which the popup will expand from and shrink to.
     */
    var horizontalAlignment: Alignment.Horizontal by mutableStateOf(Alignment.CenterHorizontally)

    /**
     * Boolean that defines whether the popup is displayed above or below the anchor.
     */
    var isTop: Boolean by mutableStateOf(false)

    /**
     * Boolean that defines whether the popup is currently visible or not.
     */
    var isVisible: Boolean by mutableStateOf(isVisible)
}

private data class PopupPositionResult(
    val horizontalAlignment: Alignment.Horizontal,
    val fitTop: Boolean
)

private data class HorizontalAlignmentCalculationResult(
    val horizontalAlignment: Alignment.Horizontal,
    val fallbackHorizontalOffset: Int
)

/**
 * A custom implementation of PopupPositionProvider.
 * This calculates the position of the popup relative to the anchor
 *
 * @property contentOffset The offset of the popup content from the anchor position.
 * @property density The density of the display.
 * @property onPopupPositionFound A callback that is called once the popup position is found.
 */
@Suppress("MagicNumber")
@Immutable
private data class HyperskillPopupPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val onPopupPositionFound: (PopupPositionResult) -> Unit
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }

        val topCoordinatesY = anchorBounds.top - popupContentSize.height
        val fitTop = topCoordinatesY > 0 || anchorBounds.top > windowSize.height

        val yOffset = computeYOffset(
            anchorBounds = anchorBounds,
            contentOffsetY = contentOffsetY,
            popupContentSize = popupContentSize,
            windowSize = windowSize,
            fitTop = fitTop
        )

        val fitEnd = (anchorBounds.left + contentOffsetX + popupContentSize.width) < windowSize.width
        val fitStart = (anchorBounds.left - contentOffsetX - popupContentSize.width) > 0

        val popupHalfWidth = popupContentSize.width / 2

        val endPlacementOffset = anchorBounds.left - contentOffsetX
        val centerPlacementOffset = anchorBounds.left - popupHalfWidth + contentOffsetX
        val startPlacementOffset = anchorBounds.right + contentOffsetX - popupContentSize.width

        val horizontalAndOffset = getHorizontalAlignment(
            fitsStart = fitStart,
            fitsEnd = fitEnd,
            endPlacementOffset = endPlacementOffset,
            startPlacementOffset = startPlacementOffset,
            centerPlacementOffset = centerPlacementOffset
        )

        onPopupPositionFound(PopupPositionResult(horizontalAndOffset.horizontalAlignment, fitTop))
        return IntOffset(horizontalAndOffset.fallbackHorizontalOffset, yOffset)
    }

    private fun computeYOffset(
        anchorBounds: IntRect,
        contentOffsetY: Int,
        popupContentSize: IntSize,
        windowSize: IntSize,
        fitTop: Boolean
    ): Int {
        val toBottom = anchorBounds.bottom + contentOffsetY
        val toTop = anchorBounds.top - contentOffsetY - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height
        val yOffset = sequenceOf(
            if (fitTop) toBottom else toTop,
            toCenter,
            toDisplayBottom
        ).firstOrNull {
            it + popupContentSize.height <= windowSize.height
        } ?: toTop
        return yOffset
    }
}

/**
 * Composable that defines custom animations for a popup.
 *
 * @param expandedState [MutableTransitionState] that determines whether the popup is expanding or shrinking.
 * @param transformOrigin [TransformOrigin] determining from which position the popup (dis)appears from.
 * @param modifier [Modifier] for this composable.
 * @param content content that will be displayed within the popup.
 */
@Composable
private fun HyperskillPopupContent(
    expandedState: MutableTransitionState<Boolean>,
    transformOrigin: TransformOrigin,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val transition = updateTransition(expandedState, "Popup")

    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Popup Scale"
    ) { isExpanded -> if (isExpanded) 1f else 0f }

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "Popup Alpha"
    ) { isExpanded -> if (isExpanded) 1f else 0f }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                this.transformOrigin = transformOrigin
            },
        content = content
    )
}

/**
 * Get the horizontal offset of the popup based on fitting and placement.
 *
 * @param fitsStart    Whether the popup fits when placed at the start.
 * @param fitsEnd      Whether the popup fits when placed at the end.
 * @param endPlacementOffset     The offset when the popup is placed at the end.
 * @param startPlacementOffset   The offset when the popup is placed at the start.
 * @param centerPlacementOffset  The offset when the popup is placed at the center.
 * @return A pair consisting of the horizontal alignment and the horizontal offset.
 */
private fun getHorizontalAlignment(
    fitsStart: Boolean,
    fitsEnd: Boolean,
    endPlacementOffset: Int,
    startPlacementOffset: Int,
    centerPlacementOffset: Int
): HorizontalAlignmentCalculationResult =
    when {
        fitsEnd -> HorizontalAlignmentCalculationResult(Alignment.Start, endPlacementOffset)
        fitsStart -> HorizontalAlignmentCalculationResult(Alignment.End, startPlacementOffset)
        else -> HorizontalAlignmentCalculationResult(Alignment.CenterHorizontally, centerPlacementOffset)
    }