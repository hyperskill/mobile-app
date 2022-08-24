package org.hyperskill.app.step_quiz.data.source

interface SubmissionCacheDataSource {
    fun incrementSolvedStepsCount()
    fun getSolvedStepsCount(): Long
    fun clearSolvedStepsCount()
}