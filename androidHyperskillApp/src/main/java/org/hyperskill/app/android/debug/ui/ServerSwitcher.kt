package org.hyperskill.app.android.debug.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import org.hyperskill.app.R
import org.hyperskill.app.debug.domain.model.EndpointConfigType

@Composable
fun EndpointSwitcher(
    modifier: Modifier = Modifier,
    options: List<EndpointConfigType>,
    selectedOption: EndpointConfigType,
    isApplyButtonVisible: Boolean,
    onOptionClick: (EndpointConfigType) -> Unit,
    onApplyClick: () -> Unit
) {
    Column(
        modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(dimensionResource(id = org.hyperskill.app.android.R.dimen.corner_radius))
            )
            .padding(Dp(16f))
    ) {
        ServerOptionsDropDown(
            options = options,
            selectedOption = selectedOption,
            onOptionClick = onOptionClick
        )
        AnimatedVisibility(
            visible = isApplyButtonVisible
        ) {
            Button(
                onClick = onApplyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = Dp(20f))
                    .height(dimensionResource(id = org.hyperskill.app.android.R.dimen.action_button_height))
            ) {
                Text(text = stringResource(id = R.string.debug_menu_apply_settings_button_text))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServerOptionsDropDown(
    options: List<EndpointConfigType>,
    selectedOption: EndpointConfigType,
    onOptionClick: (EndpointConfigType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = selectedOption.name,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.debug_menu_section_api_header_title)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onOptionClick(option)
                        expanded = false
                    }
                ) {
                    Text(text = option.name)
                }
            }
        }
    }
}