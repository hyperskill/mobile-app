package org.hyperskill.app.step_quiz.cache

import com.russhwolf.settings.Settings
import org.hyperskill.app.step_quiz.data.source.SubmissionCacheDataSource

class SubmissionCacheDataSourceImpl(
    private val settings: Settings
) : SubmissionCacheDataSource {
    override fun incrementSolvedStepsCount() {
        settings.putLong(SubmissionCacheKeyValues.STEPS_SOLVED_COUNT, getSolvedStepsCount() + 1)
    }

    override fun getSolvedStepsCount(): Long =
        settings.getLong(SubmissionCacheKeyValues.STEPS_SOLVED_COUNT, 0)
}