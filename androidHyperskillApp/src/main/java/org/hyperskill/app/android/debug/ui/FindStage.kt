package org.hyperskill.app.android.debug.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton

data class FindStageInput(val projectId: String, val stageId: String)

@Composable
fun FindStage(
    modifier: Modifier = Modifier,
    onInputChanged: (FindStageInput) -> Unit,
    onApplyClick: () -> Unit
) {
    var stageId: String by remember { mutableStateOf("") }
    var projectId: String by remember { mutableStateOf("") }
    Column(
        modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius))
            )
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = projectId,
            onValueChange = { value ->
                onInputChanged(FindStageInput(projectId = value, stageId = stageId))
                projectId = value
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(
                        id = org.hyperskill.app.R.string.debug_menu_section_stage_implement_project_id_text_input_title
                    )
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        OutlinedTextField(
            value = stageId,
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8f.dp),
            onValueChange = { value ->
                onInputChanged(FindStageInput(projectId = projectId, stageId = value))
                stageId = value
            },
            label = {
                Text(
                    text = stringResource(
                        id = org.hyperskill.app.R.string.debug_menu_section_stage_implement_stage_id_text_input_title
                    )
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        HyperskillButton(
            onClick = onApplyClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20f.dp)
        ) {
            Text(text = stringResource(id = org.hyperskill.app.R.string.debug_menu_section_stage_implement_button_text))
        }
    }
}