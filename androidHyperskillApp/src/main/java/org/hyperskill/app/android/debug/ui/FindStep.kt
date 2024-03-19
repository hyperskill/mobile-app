package org.hyperskill.app.android.debug.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton

@Composable
fun FindStep(
    modifier: Modifier = Modifier,
    onStepIdChanged: (String) -> Unit,
    onApplyClick: () -> Unit
) {
    var stepId: String by remember { mutableStateOf("") }
    Column(
        modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            )
            .padding(Dp(16f)),
        verticalArrangement = Arrangement.spacedBy(Dp(20f))
    ) {
        OutlinedTextField(
            value = stepId,
            onValueChange = { value ->
                onStepIdChanged(value)
                stepId = value
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(
                        id = org.hyperskill.app.R.string.debug_menu_section_step_navigation_text_input_title
                    )
                )
            }
        )
        HyperskillButton(
            onClick = onApplyClick,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = org.hyperskill.app.R.string.debug_menu_section_step_navigation_button_text))
        }
    }
}