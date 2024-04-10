package org.hyperskill.app.android.users_questionnaire_onboarding.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UsersQuestionnaireOptionsList(
    choices: List<String>,
    selectedChoice: String?,
    textInputValue: String?,
    isTextInputVisible: Boolean,
    onChoiceClicked: (String) -> Unit,
    onTextInputChanged: (String) -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        choices.forEach { choice ->
            key(choice) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.requiredSize(24.dp)) {
                        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                            RadioButton(
                                selected = choice == selectedChoice,
                                onClick = { onChoiceClicked(choice) },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colors.primary),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Text(
                        text = choice,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
        AnimatedVisibility(visible = isTextInputVisible) {
            OutlinedTextField(
                value = textInputValue ?: "",
                onValueChange = onTextInputChanged,
                placeholder = {
                    Text(text = stringResource(id = R.string.users_questionnaire_onboarding_text_input_placeholder))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onDoneClick() }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}