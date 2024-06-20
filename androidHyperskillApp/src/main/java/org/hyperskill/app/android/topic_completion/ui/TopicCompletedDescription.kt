package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect

@Composable
fun TopicCompletedDescription(
    text: String,
    modifier: Modifier = Modifier,
    onDescriptionFullyShowed: () -> Unit = {}
) {
    TypewriterTextEffect(
        text = text,
        startTypingDelay = TopicCompletedDefaults.DESCRIPTION_TYPING_DELAY_MILLIS,
        onEffectCompleted = onDescriptionFullyShowed
    ) { displayedText ->
        Text(
            text = displayedText,
            style = MaterialTheme.typography.subtitle1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            color = colorResource(id = R.color.color_on_surface_alpha_87),
            textAlign = TextAlign.Center,
            minLines = 2,
            modifier = modifier
        )
    }
}