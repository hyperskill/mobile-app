package org.hyperskill.app.android.welcome_onbaording.questionnaire.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun WelcomeQuestionnaireItem(
    title: String,
    iconPainter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val view = LocalView.current
    val currentOnClick by rememberUpdatedState(
        newValue = {
            view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
            onClick()
        }
    )
    HyperskillCard(
        modifier = modifier,
        onClick = currentOnClick
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier
                    .requiredSize(16.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun WelcomeQuestionnaireItemPreview() {
    HyperskillTheme {
        WelcomeQuestionnaireItem(
            title = "Tik tok Tik tok Tik tok",
            iconPainter = painterResource(id = R.drawable.ic_welcome_questionnaire_tiktok),
            onClick = {}
        )
    }
}