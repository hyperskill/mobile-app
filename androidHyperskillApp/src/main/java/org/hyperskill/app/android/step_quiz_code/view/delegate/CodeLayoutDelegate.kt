package org.hyperskill.app.android.step_quiz_code.view.delegate

import org.hyperskill.app.android.code.presentation.model.extensionForLanguage
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.step.domain.model.Step

class CodeLayoutDelegate(
    private val codeLayout: CodeEditorLayout,
    private val step: Step,
    private val codeTemplates: Map<String, String>,
    private val codeQuizInstructionDelegate: CodeQuizInstructionDelegate,
    private var codeToolbarAdapter: CodeToolbarAdapter?
) {

    /**
     * if [code] is null then default code template for [lang] will be used
     */
    fun setLanguage(lang: String, code: String? = null) {
        codeLayout.langExtension = extensionForLanguage(lang)
        codeLayout.setTextIfChanged(code ?: codeTemplates[lang] ?: "")
        codeToolbarAdapter?.setLanguage(lang)
    }

    fun setDetailsContentData(lang: String?) {
        codeQuizInstructionDelegate.setCodeDetailsData(step, lang)
    }

    fun setEnabled(isEnabled: Boolean) {
        codeLayout.isEnabled = isEnabled
    }
}