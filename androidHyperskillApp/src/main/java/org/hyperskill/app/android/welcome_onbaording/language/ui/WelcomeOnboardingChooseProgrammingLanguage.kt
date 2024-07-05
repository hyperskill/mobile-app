package org.hyperskill.app.android.welcome_onbaording.language.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect
import org.hyperskill.app.android.welcome_onbaording.questionnaire.ui.WelcomeQuestionnaireItem
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.welcome_onboarding.root.model.WelcomeOnboardingProgrammingLanguage

@Composable
fun WelcomeOnboardingChooseProgrammingLanguage(
    onLangClick: (WelcomeOnboardingProgrammingLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val languages = remember {
        WelcomeOnboardingProgrammingLanguage.entries
    }
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = WelcomeOnboardingDefault.horizontalPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            TypewriterTextEffect(
                text = stringResource(id = R.string.welcome_onboarding_pick_language_title),
                startTypingDelay = WelcomeOnboardingDefault.startTypingAnimationDelayMillis
            ) { text ->
                Text(
                    text = text,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.text_primary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            ProgrammingLanguageList(
                items = languages,
                onLangClick = onLangClick
            )
        }
    }
}

@Composable
private fun ProgrammingLanguageList(
    items: List<WelcomeOnboardingProgrammingLanguage>,
    onLangClick: (type: WelcomeOnboardingProgrammingLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        items.forEachIndexed { i, item ->
            WelcomeQuestionnaireItem(
                title = item.title,
                iconPainter = item.iconPainter,
                onClick = { onLangClick(item) },
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

@Preview(showSystemUi = true)
@Composable
private fun WelcomeOnboardingChooseProgrammingLanguagePreview() {
    HyperskillTheme {
        WelcomeOnboardingChooseProgrammingLanguage(onLangClick = {})
    }
}