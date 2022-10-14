package org.hyperskill.app.step_quiz.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest

class StepQuizUserPermissionRequestTextMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getTitle(request: StepQuizUserPermissionRequest): String =
        when (request) {
            StepQuizUserPermissionRequest.RESET_CODE ->
                resourceProvider.getString(SharedResources.strings.reset_code_dialog_title)
            StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS ->
                resourceProvider.getString(SharedResources.strings.after_daily_step_completed_dialog_title)
        }

    fun getMessage(request: StepQuizUserPermissionRequest): String =
        when (request) {
            StepQuizUserPermissionRequest.RESET_CODE ->
                resourceProvider.getString(SharedResources.strings.reset_code_dialog_explanation)
            StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS ->
                resourceProvider.getString(SharedResources.strings.after_daily_step_completed_dialog_text)
        }
}