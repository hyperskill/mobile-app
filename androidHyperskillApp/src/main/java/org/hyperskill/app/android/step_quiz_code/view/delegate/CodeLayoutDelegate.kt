package org.hyperskill.app.android.step_quiz_code.view.delegate

import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.code.domain.model.fileExtensionForLanguage

class CodeLayoutDelegate(
    private val codeLayout: CodeEditorLayout,
    private val config: CodeStepQuizConfig,
    private val codeQuizInstructionDelegate: CodeQuizInstructionDelegate,
    private var codeToolbarAdapter: CodeToolbarAdapter?
) {

    /**
     * if [code] is null then [CodeStepQuizConfig.initialCode] will be used
     */
    fun setLanguage(lang: String, code: String? = null) {
        codeLayout.langExtension = fileExtensionForLanguage(lang)
        codeLayout.setTextIfChanged(code ?: config.initialCode)
        codeToolbarAdapter?.setLanguage(lang)
    }

    fun setDetailsContentData(lang: String?) {
        codeQuizInstructionDelegate.setCodeDetailsData(config.codeDetails, lang)
    }

    fun setEnabled(isEnabled: Boolean) {
        codeLayout.isEnabled = isEnabled
    }
}