package org.hyperskill.app.android.core.view.ui.widget.compose

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

/**
 * A composable function that displays a text with a typewriter-like effect, revealing characters in chunks.
 *
 * @param text The input text to be displayed with the typewriter effect.
 * @param minDelayInMillis The minimum delay in milliseconds between revealing character chunks, defaults to 30ms.
 * @param maxDelayInMillis The maximum delay in milliseconds between revealing character chunks, defaults to 80ms.
 * @param minCharacterChunk The minimum number of characters to reveal at once, defaults to 1.
 * @param maxCharacterChunk The maximum number of characters to reveal at once, defaults to 3.
 * @param onEffectCompleted A callback function invoked when the entire text has been revealed.
 * @param displayTextComposable A composable function that receives the text to display with the typewriter effect.
 *
 * @throws IllegalArgumentException if [minDelayInMillis] is greater than [maxDelayInMillis].
 * @throws IllegalArgumentException if [minCharacterChunk] is greater than [maxCharacterChunk].
 */
@Composable
fun TypewriterTextEffect(
    text: String,
    startTypingDelayInMillis: Int? = null,
    minDelayInMillis: Long = 30,
    maxDelayInMillis: Long = 80,
    minCharacterChunk: Int = 1,
    maxCharacterChunk: Int = 3,
    onEffectCompleted: () -> Unit = {},
    displayTextComposable: @Composable (displayedText: String) -> Unit
) {
    // Ensure minDelayInMillis is less than or equal to maxDelayInMillis
    require(minDelayInMillis <= maxDelayInMillis) {
        "TypewriterTextEffect: Invalid delay range. minDelayInMillis ($minDelayInMillis) must be less than or equal to maxDelayInMillis ($maxDelayInMillis)."
    }

    // Ensure minCharacterChunk is less than or equal to maxCharacterChunk
    require(minCharacterChunk <= maxCharacterChunk) {
        "TypewriterTextEffect: Invalid character chunk range. minCharacterChunk ($minCharacterChunk) must be less than or equal to maxCharacterChunk ($maxCharacterChunk)."
    }

    // Initialize and remember the displayedText
    var displayedText by remember { mutableStateOf("") }

    // Call the displayTextComposable with the current displayedText value
    displayTextComposable(displayedText)

    // Launch the effect to update the displayedText value over time
    LaunchedEffect(text) {
        Log.d("TypewriterTextEffect", "Start LaunchedEffect")
        if (startTypingDelayInMillis != null) {
            delay(startTypingDelayInMillis.milliseconds)
        }
        Log.d("TypewriterTextEffect", "Start typing")

        val textLength = text.length
        var endIndex = 0

        while (endIndex < textLength) {
            endIndex = minOf(
                endIndex + Random.nextInt(minCharacterChunk, maxCharacterChunk + 1),
                textLength
            )
            displayedText = text.substring(startIndex = 0, endIndex = endIndex)
            delay(Random.nextLong(minDelayInMillis, maxDelayInMillis))
        }
        onEffectCompleted()
    }
}