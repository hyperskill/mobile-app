package org.hyperskill.app.android.users_questionnaire.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.extensions.plus
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTextButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingFeature
import org.hyperskill.app.users_questionnaire.onboarding.presentation.UsersQuestionnaireOnboardingViewModel

@Composable
fun UsersQuestionnaireOnboardingScreen(viewModel: UsersQuestionnaireOnboardingViewModel) {
    DisposableEffect(viewModel) {
        viewModel.onNewMessage(
            UsersQuestionnaireOnboardingFeature.Message.ViewedEventMessage
        )
        onDispose {
            // no op
        }
    }
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    UsersQuestionnaireOnboardingScreen(
        viewState = viewState,
        onChoiceClicked = viewModel::onChoiceClicked,
        onTextInputChanged = viewModel::onTextInputChanged,
        onSendClick = viewModel::onSendButtonClick,
        onSkipClick = viewModel::onSkipButtonClick
    )
}

@Composable
fun UsersQuestionnaireOnboardingScreen(
    viewState: UsersQuestionnaireOnboardingFeature.ViewState,
    onChoiceClicked: (String) -> Unit,
    onTextInputChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding + UsersQuestionnaireOnboardingDefaults.ContentPadding)
        ) {
            Text(
                text = viewState.title,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            UsersQuestionnaireOptionsList(
                choices = viewState.choices,
                selectedChoice = viewState.selectedChoice,
                textInputValue = viewState.textInputValue,
                isTextInputVisible = viewState.isTextInputVisible,
                onChoiceClicked = onChoiceClicked,
                onTextInputChanged = onTextInputChanged,
                onDoneClick = onSendClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                HyperskillButton(
                    onClick = onSendClick,
                    enabled = viewState.isSendButtonEnabled,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.users_questionnaire_onboarding_send_button_text))
                }
                HyperskillTextButton(
                    onClick = onSkipClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.users_questionnaire_onboarding_skip_button_text))
                }
            }
        }
    }
}

private class UsersQuestionnaireOnboardingPreviewParameterProvider :
    PreviewParameterProvider<UsersQuestionnaireOnboardingFeature.ViewState> {
    override val values: Sequence<UsersQuestionnaireOnboardingFeature.ViewState>
        get() = sequenceOf(
            UsersQuestionnaireOnboardingPreviewDefault.getUnselectedViewState(),
            UsersQuestionnaireOnboardingPreviewDefault.getFirstOptionSelectedViewState(),
            UsersQuestionnaireOnboardingPreviewDefault.getOtherOptionSelectedViewState(false),
            UsersQuestionnaireOnboardingPreviewDefault.getOtherOptionSelectedViewState(true)
        )
    }

@Preview(device = "id:pixel_3", showSystemUi = true)
@Composable
private fun UsersQuestionnaireOnboardingScreenPreview(
    @PreviewParameter(UsersQuestionnaireOnboardingPreviewParameterProvider::class)
    viewState: UsersQuestionnaireOnboardingFeature.ViewState
) {
    HyperskillTheme {
        UsersQuestionnaireOnboardingScreen(
            viewState = viewState,
            onChoiceClicked = {},
            onTextInputChanged = {},
            onSendClick = {},
            onSkipClick = {}
        )
    }
}

@Preview(device = "id:Nexus S", showSystemUi = true)
@Composable
private fun UsersQuestionnaireOnboardingScreenPreviewSmallDevice(
    @PreviewParameter(UsersQuestionnaireOnboardingPreviewParameterProvider::class)
    viewState: UsersQuestionnaireOnboardingFeature.ViewState
) {
    HyperskillTheme {
        UsersQuestionnaireOnboardingScreen(
            viewState = viewState,
            onChoiceClicked = {},
            onTextInputChanged = {},
            onSendClick = {},
            onSkipClick = {}
        )
    }
}