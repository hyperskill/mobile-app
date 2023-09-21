package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle

val Typography.textButton: TextStyle
    @Composable
    get() = button.copy(
        color = colorResource(id = org.hyperskill.app.R.color.button_ghost)
    )