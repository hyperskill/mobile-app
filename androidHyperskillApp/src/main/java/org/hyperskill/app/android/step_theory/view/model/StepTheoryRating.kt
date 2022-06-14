package org.hyperskill.app.android.step_theory.view.model

import androidx.annotation.DrawableRes
import org.hyperskill.app.android.R

enum class StepTheoryRating(
    @DrawableRes
    val drawableRes: Int
) {
    EXCELLENT(R.drawable.ic_step_theory_rating_excellent),
    GOOD(R.drawable.ic_step_theory_rating_good),
    SATISFACTORY(R.drawable.ic_step_theory_rating_satisfactory),
    WEAK(R.drawable.ic_step_theory_rating_weak),
    POOR(R.drawable.ic_step_theory_rating_poor)
}