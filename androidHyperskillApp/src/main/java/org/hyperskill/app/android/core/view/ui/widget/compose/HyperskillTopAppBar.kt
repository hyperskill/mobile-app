package org.hyperskill.app.android.core.view.ui.widget.compose

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R

@Composable
fun HyperskillTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        navigationIcon = onNavigationIconClick?.let {
            {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_toolbar_back),
                        contentDescription = stringResource(id = org.hyperskill.app.R.string.back),
                        tint = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60)
                    )
                }
            }
        },
        title = {
            Text(
                text = title,
                modifier = TopAppBarTitleModifier
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        actions = actions,
        modifier = modifier
    )
}

// -16 dp is used to fix spacing between navigation icon and title
private val TopAppBarTitleModifier = Modifier.offset(x = (-16).dp)