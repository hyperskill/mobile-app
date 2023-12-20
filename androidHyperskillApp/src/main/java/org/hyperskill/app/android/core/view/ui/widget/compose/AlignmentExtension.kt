package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment

/**
 * An [Alignment] specified by vertical bias: for example, a bias of -1 represents alignment to the
 * top, a bias of 0 will represent centering, and a bias of 1 will represent bottom.
 * Any value can be specified to obtain an alignment.
 *
 * @see BiasAlignment
 */
@Stable
fun Alignment.Companion.centerWithVerticalBias(verticalBias: Float): Alignment =
    BiasAlignment(0f, verticalBias)