package org.hyperskill.app.android.step_quiz_sql.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.databinding.LayoutStepQuizDescriptionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizSqlBinding
import org.hyperskill.app.android.step_quiz.view.delegate.StepQuizFormDelegate
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_fullscreen_code.dialog.CodeStepQuizFullScreenDialogFragment
import org.hyperskill.app.android.step_quiz_sql.view.delegate.SqlStepQuizFormDelegate
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class SqlStepQuizFragment : DefaultStepQuizFragment(), CodeStepQuizFullScreenDialogFragment.Callback {

    companion object {
        fun newInstance(step: Step, stepRoute: StepRoute): Fragment =
            SqlStepQuizFragment().apply {
                this.step = step
                this.stepRoute = stepRoute
            }

        private val SqlLangName = ProgrammingLanguage.SQL.serverPrintableName
    }

    private var _binding: LayoutStepQuizSqlBinding? = null
    private val binding: LayoutStepQuizSqlBinding
        get() = requireNotNull(_binding) { "Binding was not set!" }

    private var codeStepQuizFormDelegate: SqlStepQuizFormDelegate? = null

    override val quizViews: Array<View>
        get() = arrayOf(binding.stepQuizCodeContainer)

    override val skeletonView: View
        get() = binding.stepQuizCodeSkeleton.root

    override val descriptionBinding: LayoutStepQuizDescriptionBinding
        get() = binding.sqlStepDescription

    override fun createStepView(layoutInflater: LayoutInflater, parent: ViewGroup): View {
        val binding = LayoutStepQuizSqlBinding.inflate(layoutInflater, parent, false).also {
            _binding = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        codeStepQuizFormDelegate = null
        super.onDestroyView()
    }

    override fun createStepQuizFormDelegate(): StepQuizFormDelegate {
        val codeStepQuizFormDelegate = SqlStepQuizFormDelegate(
            codeLayout = binding.codeStepLayout,
            sqlCodeTemplate = getSqlCodeTemplate(step),
            onFullscreenClicked = ::onFullScreenClicked,
            onQuizChanged = ::syncReplyState
        )
        this.codeStepQuizFormDelegate = codeStepQuizFormDelegate
        return codeStepQuizFormDelegate
    }

    private fun onFullScreenClicked(lang: String, code: String?) {
        val initialCode = getSqlCodeTemplate(step)
        CodeStepQuizFullScreenDialogFragment
            .newInstance(
                CodeStepQuizFullScreenDialogFragment.Params(
                    lang = lang,
                    code = code ?: initialCode,
                    step = step,
                    isShowRetryButton = viewBinding.stepQuizButtons.stepQuizRetryLogoOnlyButton.isVisible
                )
            )
            .showIfNotExists(childFragmentManager, CodeStepQuizFullScreenDialogFragment.TAG)
    }

    override fun onNewState(state: StepQuizFeature.State) {
        (state.stepQuizState as? StepQuizFeature.StepQuizState.AttemptLoaded)?.let { attemptLoadedState ->
            val submission = (attemptLoadedState.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                ?.submission
            val replyCode = submission?.reply?.code
            val fullScreenFragment = childFragmentManager
                .findFragmentByTag(CodeStepQuizFullScreenDialogFragment.TAG) as? CodeStepQuizFullScreenDialogFragment
            fullScreenFragment?.onNewCode(replyCode)
        }
    }

    override fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean) {
        codeStepQuizFormDelegate?.updateCodeLayoutFromDialog(code, onSubmitClicked)
        if (onSubmitClicked) {
            onSubmitButtonClicked()
        }
    }

    override fun onResetCodeClick() {
        onRetryButtonClicked()
    }

    private fun getSqlCodeTemplate(step: Step): String =
        step.block.options.codeTemplates?.get(SqlLangName) ?: ""
}