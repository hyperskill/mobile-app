package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.runtime.Composable
import com.google.accompanist.themeadapter.material.MdcTheme

@Composable
fun HyperskillTheme(content: @Composable () -> Unit) {
    MdcTheme(
        setTextColors = true,
        setDefaultFontFamily = true,
        content = content
    )
}