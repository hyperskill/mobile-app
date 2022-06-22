package org.hyperskill.app.android.code.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.model.ProgrammingLanguage
import org.hyperskill.app.android.code.model.extensionForLanguage
import org.hyperskill.app.android.databinding.LayoutCodeEditorTestBinding

// TODO: Remove before pushing to develop
class CodeEditorFragment : Fragment(R.layout.layout_code_editor_test) {
    private val binding: LayoutCodeEditorTestBinding by viewBinding(LayoutCodeEditorTestBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.codeStepLayout.lang = extensionForLanguage(ProgrammingLanguage.PYTHON.serverPrintableName)
    }
}