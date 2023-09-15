package org.hyperskill.app.android.step_quiz_fullscreen_code.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage
import org.hyperskill.app.android.code.util.CodeToolbarUtil
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.setTintList
import org.hyperskill.app.android.databinding.DialogStepQuizCodeFullscreenBinding
import org.hyperskill.app.android.latex.view.widget.LatexView
import org.hyperskill.app.android.latex.view.widget.LatexWebView
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.CodeStepQuizConfigFactory
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.android.step_quiz_fullscreen_code.adapter.CodeStepQuizFullScreenPagerAdapter
import org.hyperskill.app.android.view.base.ui.extension.setOnKeyboardOpenListener
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz.view.mapper.StepQuizStatsTextMapper
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.hideKeyboard

class CodeStepQuizFullScreenDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "CodeStepQuizFullScreenDialogFragment"

        private const val INSTRUCTION_TAB = 0
        private const val CODE_TAB = 1

        fun newInstance(
            params: Params
        ): CodeStepQuizFullScreenDialogFragment =
            CodeStepQuizFullScreenDialogFragment().apply {
                this.step = params.step
                this.lang = params.lang
                this.code = params.code
                this.isShowRetryButton = params.isShowRetryButton
                this.titleRes = params.titleRes
            }
    }

    private val viewBinding: DialogStepQuizCodeFullscreenBinding by viewBinding(
        DialogStepQuizCodeFullscreenBinding::bind
    )

    private lateinit var codeLayoutDelegate: CodeLayoutDelegate

    private lateinit var instructionsLayout: View
    private lateinit var playgroundLayout: View

    /**
     *  Code play ground views
     */
    private lateinit var codeLayout: CodeEditorLayout
    private lateinit var submitButtonSeparator: View
    private lateinit var codeSubmitButton: MaterialButton
    private lateinit var retryButton: View

    private lateinit var codeToolbarAdapter: CodeToolbarAdapter

    // Flag is necessary, because keyboard listener is constantly invoked (probably global layout listener reacts to view changes)
    private var keyboardShown: Boolean = false

    private var lang: String by argument()
    private var code: String by argument()
    private var step: Step by argument(serializer = Step.serializer())
    private var isShowRetryButton: Boolean by argument()
    private var titleRes: Int by argument()

    private val config: CodeStepQuizConfig by lazy(LazyThreadSafetyMode.NONE) {
        CodeStepQuizConfigFactory.create(step)
    }

    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private var latexWebView: LatexWebView? = null

    private var isCodeSyncedAfterSubmissionClick: Boolean = false

    private val callback: Callback?
        get() = parentFragment as? Callback

    private fun injectComponent() {
        stepQuizStatsTextMapper = StepQuizStatsTextMapper(
            HyperskillApp.graph().commonComponent.resourceProvider,
            HyperskillApp.graph().commonComponent.dateFormatter
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
        injectComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.dialog_step_quiz_code_fullscreen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding.fullScreenCenteredToolbar) {
            centeredToolbarTitle.text = getString(titleRes)
            centeredToolbar.inflateMenu(R.menu.code_playground_menu)
            centeredToolbar.setNavigationOnClickListener { dismiss() }
            centeredToolbar.apply {
                navigationIcon = AppCompatResources
                    .getDrawable(context, R.drawable.ic_close_thin)
                    ?.setTintList(context, androidx.appcompat.R.attr.colorControlNormal)
            }
            centeredToolbar.setOnMenuItemClickListener { item ->
                if (item?.itemId == R.id.action_reset_code) {
                    onResetClick()
                    true
                } else {
                    false
                }
            }
        }

        initViewPager()

        val text = step
            .block
            .text
            .takeIf(String::isNotEmpty)

        val textHeader =
            instructionsLayout.findViewById<LatexView>(R.id.stepQuizCodeFullscreenInstructionTextHeader)
        if (latexWebView == null) {
            latexWebView = LayoutInflater
                .from(requireContext().applicationContext)
                .inflate(
                    R.layout.layout_latex_webview,
                    textHeader as ViewGroup,
                    false
                ) as LatexWebView
        }

        latexWebView?.let {
            (textHeader as ViewGroup).addView(it)
        }

        instructionsLayout.findViewById<LatexView>(R.id.stepQuizCodeFullscreenInstructionTextHeader)
            .setText(text)

        /**
         *  Code play ground view binding
         */
        submitButtonSeparator = playgroundLayout.findViewById(R.id.submitButtonSeparator)

        codeSubmitButton = playgroundLayout.findViewById(R.id.stepQuizSubmitButton)
        codeSubmitButton.setText(org.hyperskill.app.R.string.step_quiz_code_run_solution_button_text)
        codeSubmitButton.setIconResource(R.drawable.ic_run)
        codeSubmitButton.iconPadding =
            requireContext().resources
                .getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        codeSubmitButton.setOnClickListener { submitCodeActionClick() }

        retryButton = playgroundLayout.findViewById(R.id.stepQuizRetryLogoOnlyButton)
        retryButton.setOnClickListener {
            onResetClick()
        }
        if (isShowRetryButton) {
            retryButton.visibility = View.VISIBLE
        }

        setupCodeToolbarAdapter()
        setupKeyboardExtension()

        codeLayoutDelegate = CodeLayoutDelegate(
            codeLayout = codeLayout,
            config = config,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                instructionsLayout.findViewById(R.id.stepQuizCodeFullscreenInstructionDetails),
                false,
                onDetailsIsExpandedStateChanged = {}
            ),
            codeToolbarAdapter = codeToolbarAdapter
        )

        codeLayoutDelegate.setLanguage(lang, code)
        codeLayoutDelegate.setDetailsContentData(lang)
        viewBinding.fullScreenCodeViewPager.setCurrentItem(CODE_TAB, false)
    }

    private fun onResetClick() {
        syncCodeStateWithParent()
        callback?.onResetCodeClick()
    }

    private fun initViewPager() {
        val pagerAdapter = CodeStepQuizFullScreenPagerAdapter(requireContext())

        viewBinding.fullScreenCodeViewPager.adapter = pagerAdapter
        viewBinding.fullScreenCodeTabs.setupWithViewPager(viewBinding.fullScreenCodeViewPager)
        viewBinding.fullScreenCodeViewPager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(p0: Int) {}
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
                override fun onPageSelected(p0: Int) {
                    view?.hideKeyboard()
                }
            }
        )

        instructionsLayout = pagerAdapter.getViewAt(INSTRUCTION_TAB)
        playgroundLayout = pagerAdapter.getViewAt(CODE_TAB)
        codeLayout = playgroundLayout.findViewById(R.id.codeStepLayout)
    }

    override fun onStart() {
        super.onStart()
        dialog
            ?.window
            ?.let { window ->
                window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                window.setWindowAnimations(R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
            }
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        if (!isCodeSyncedAfterSubmissionClick) {
            syncCodeStateWithParent()
        }
        super.onPause()
    }

    /**
     * Called receiving new state from feature (calling [Callback] methods).
     */
    fun onNewCode(code: String?) {
        this.code = code ?: config.initialCode
        codeLayoutDelegate.setLanguage(lang, code)
    }

    private fun setupCodeToolbarAdapter() {
        codeToolbarAdapter = CodeToolbarAdapter(requireContext())
            .apply {
                onSymbolClickListener = object : CodeToolbarAdapter.OnSymbolClickListener {
                    override fun onSymbolClick(symbol: String, offset: Int) {
                        codeLayout.insertText(
                            CodeToolbarUtil.mapToolbarSymbolToPrintable(
                                symbol,
                                codeLayout.indentSize
                            ),
                            offset
                        )
                    }
                }
            }
    }

    /**
     * Keyboard extension
     */
    private fun setupKeyboardExtension() {
        with(viewBinding.fullScreenCodeKeyboardExtension.stepQuizCodeKeyboardExtension) {
            adapter = codeToolbarAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        codeLayout.codeToolbarAdapter = codeToolbarAdapter

        setOnKeyboardOpenListener(
            viewBinding.coordinator,
            onKeyboardHidden = {
                if (keyboardShown) {
                    viewBinding.fullScreenCodeKeyboardExtension.stepQuizCodeKeyboardExtension.visibility =
                        View.GONE
                    codeLayout.isNestedScrollingEnabled = true
                    codeLayout.layoutParams =
                        (codeLayout.layoutParams as RelativeLayout.LayoutParams)
                            .apply {
                                bottomMargin = 0
                            }
                    codeLayout.setPadding(
                        0,
                        0,
                        0,
                        requireContext().resources
                            .getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_bottom_padding)
                    )
                    setViewsVisibility(needShow = true)
                    keyboardShown = false
                }
            },
            onKeyboardShown = {
                if (!keyboardShown) {
                    // We show the keyboard extension only when "Code" tab is opened
                    if (viewBinding.fullScreenCodeViewPager.currentItem == CODE_TAB) {
                        viewBinding.fullScreenCodeKeyboardExtension.stepQuizCodeKeyboardExtension.visibility =
                            View.VISIBLE
                    }
                    codeLayout.isNestedScrollingEnabled = false
                    codeLayout.layoutParams =
                        (codeLayout.layoutParams as RelativeLayout.LayoutParams)
                            .apply {
                                bottomMargin =
                                    viewBinding.fullScreenCodeKeyboardExtension.stepQuizCodeKeyboardExtension.height
                            }
                    codeLayout.setPadding(0, 0, 0, 0)
                    setViewsVisibility(needShow = false)
                    keyboardShown = true
                }
            }
        )
    }

    /**
     *  Hiding views upon opening keyboard
     */
    private fun setViewsVisibility(needShow: Boolean) {
        retryButton.isVisible = needShow and isShowRetryButton
        submitButtonSeparator.isVisible = needShow
        codeSubmitButton.isVisible = needShow
        viewBinding.fullScreenCenteredToolbar.centeredToolbar.isVisible = needShow
        viewBinding.fullScreenCodeTabs.isVisible = needShow
    }

    private fun submitCodeActionClick() {
        syncCodeStateWithParent(onSubmitClicked = true)
        isCodeSyncedAfterSubmissionClick = true
        dismiss()
    }

    private fun syncCodeStateWithParent(onSubmitClicked: Boolean = false) {
        callback?.onSyncCodeStateWithParent(codeLayout.text.toString(), onSubmitClicked)
    }

    interface Callback {
        fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean = false)
        fun onResetCodeClick()
    }

    data class Params(
        val lang: String,
        val code: String,
        val step: Step,
        val isShowRetryButton: Boolean
    ) {
        val titleRes: Int
            get() = if (lang == ProgrammingLanguage.SQL.serverPrintableName) {
                org.hyperskill.app.R.string.step_quiz_sql_title
            } else {
                org.hyperskill.app.R.string.step_quiz_code_write_program_text
            }
    }
}