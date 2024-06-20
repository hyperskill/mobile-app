package org.hyperskill.app.android.welcome_onbaording.questionnaire.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType
import org.hyperskill.app.welcome_onboarding.questionnaire.model.WelcomeQuestionnaireItemType.ClientSource
import org.hyperskill.app.welcome_onboarding.questionnaire.view.WelcomeQuestionnaireItem
import org.hyperskill.app.welcome_onboarding.questionnaire.view.WelcomeQuestionnaireViewState

@Composable
fun WelcomeQuestionnaire(
    viewState: WelcomeQuestionnaireViewState,
    modifier: Modifier = Modifier,
    onItemClick: (type: WelcomeQuestionnaireItemType) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            Title(
                text = viewState.title,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            ItemsList(
                items = viewState.items,
                onItemClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    val style = WelcomeOnboardingDefault.titleTextStyle
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = remember(text) {
        textMeasurer.measure(text, style)
    }
    TypewriterTextEffect(
        text = text,
        startTypingDelay = WelcomeOnboardingDefault.startTypingAnimationDelayMillis
    ) {
        Text(
            text = it,
            style = WelcomeOnboardingDefault.titleTextStyle,
            textAlign = TextAlign.Center,
            minLines = textLayoutResult.lineCount,
            modifier = modifier
        )
    }
}

@Composable
private fun ItemsList(
    items: List<WelcomeQuestionnaireItem>,
    onItemClick: (type: WelcomeQuestionnaireItemType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        items.forEachIndexed { i, item ->
            WelcomeQuestionnaireItem(
                title = item.text,
                iconPainter = item.type.iconPainter,
                onClick = { onItemClick(item.type) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(
                    if (i == items.lastIndex) {
                        16.dp
                    } else {
                        12.dp
                    }
                )
            )
        }
    }
}

private class WelcomeQuestionnairePreviewProvider : PreviewParameterProvider<List<WelcomeQuestionnaireItem>> {
    override val values: Sequence<List<WelcomeQuestionnaireItem>>
        get() = sequenceOf(
            listOf(
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Tik tok"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.GOOGLE_SEARCH,
                    "Google search"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.NEWS,
                    "News"
                ),
            ),
            listOf(
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Tik tok"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.GOOGLE_SEARCH,
                    "Google search"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.NEWS,
                    "News"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
            ),
            listOf(
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Tik tok"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.GOOGLE_SEARCH,
                    "Google search"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.NEWS,
                    "News"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                ),
                WelcomeQuestionnaireItem(
                    ClientSource.TIK_TOK,
                    "Some long text Some long text Some long text"
                )
            )
        )
}

@Composable
@Preview(showSystemUi = true)
private fun WelcomeQuestionnairePreview(
    @PreviewParameter(WelcomeQuestionnairePreviewProvider::class) items: List<WelcomeQuestionnaireItem>
) {
    HyperskillTheme {
        WelcomeQuestionnaire(
            WelcomeQuestionnaireViewState(
                title = stringResource(id = R.string.welcome_questionnaire_how_did_you_hear_about_hyperskill_title),
                items = items
            ),
            onItemClick = {}
        )
    }
}