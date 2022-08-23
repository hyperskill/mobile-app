package org.hyperskill.app.step_quiz.data.source

interface SubmissionCacheDataSource {
    fun incrementStepsInAppSolved()
    fun getStepsInAppSolved(): Long
}