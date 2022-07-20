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
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chrynan.parcelable.core.getParcelable
import com.chrynan.parcelable.core.putParcelable
import com.google.android.material.button.MaterialButton
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.util.CodeToolbarUtil
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.databinding.DialogStepQuizCodeFullscreenBinding
import org.hyperskill.app.android.step_quiz.view.fragment.DefaultStepQuizFragment
import org.hyperskill.app.android.step_quiz_fullscreen_code.adapter.CodeStepQuizFullScreenPagerAdapter
import org.hyperskill.app.android.core.extensions.setTintList
import org.hyperskill.app.android.latex.view.widget.LatexView
import org.hyperskill.app.android.latex.view.widget.LatexWebView
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.view.base.ui.extension.setOnKeyboardOpenListener
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.step.domain.model.Step
import ru.nobird.android.view.base.ui.extension.argument
import ru.nobird.android.view.base.ui.extension.hideKeyboard

class CodeStepQuizFullScreenDialogFragment :
    DialogFragment(),
    ResetCodeDialogFragment.Callback {
    companion object {
        const val TAG = "CodeStepQuizFullScreenDialogFragment"

        private const val ARG_LANG = "LANG"
        private const val ARG_CODE = "CODE"

        private const val INSTRUCTION_TAB = 0
        private const val CODE_TAB = 1

        fun newInstance(
            lang: String,
            code: String,
            codeTemplates: Map<String, String>,
            step: Step,
            isShowRetryButton: Boolean
        ): DialogFragment {
            val arguments = Bundle().apply {
                putParcelable(DefaultStepQuizFragment.KEY_STEP, step)
            }
            return CodeStepQuizFullScreenDialogFragment().apply {
                this.arguments = arguments
                this.lang = lang
                this.code = code
                this.codeTemplates = codeTemplates
                this.isShowRetryButton = isShowRetryButton
            }
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
    private var codeTemplates: Map<String, String> by argument()
    private lateinit var step: Step
    private var isShowRetryButton: Boolean by argument()

    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var resourceProvider: ResourceProvider
    private var latexWebView: LatexWebView? = null

    private fun injectComponent() {
        resourceProvider = HyperskillApp.graph().commonComponent.resourceProvider
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

        step = requireArguments().getParcelable<Step>(DefaultStepQuizFragment.KEY_STEP)
            ?: throw IllegalStateException()

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
            centeredToolbarTitle.text = getString(R.string.step_quiz_code_title)
            centeredToolbar.inflateMenu(R.menu.code_playground_menu)
            centeredToolbar.setNavigationOnClickListener { dismiss() }
            centeredToolbar.apply {
                navigationIcon = AppCompatResources
                    .getDrawable(context, R.drawable.ic_close_thin)
                    ?.setTintList(context, R.attr.colorControlNormal)
            }
            centeredToolbar.setOnMenuItemClickListener { item ->
                if (item?.itemId == R.id.action_reset_code) {
                    val dialog = ResetCodeDialogFragment.newInstance()
                    if (!dialog.isAdded) {
                        dialog.show(childFragmentManager, null)
                    }
                    true
                } else {
                    false
                }
            }
        }

//        if (savedInstanceState != null) {
//            lang = savedInstanceState.getString(ARG_LANG) ?: return
//            code = savedInstanceState.getString(ARG_CODE) ?: return
//        }

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
        instructionsLayout.findViewById<AppCompatTextView>(R.id.stepQuizCodeFullscreenInstructionPracticeCompletion).text =
            resourceProvider.getString(
                SharedResources.strings.step_quiz_stats_text,
                step.solvedBy.toString(),
                "10 minutes" // TODO Update with data from step
            )
        instructionsLayout.findViewById<LatexView>(R.id.stepQuizCodeFullscreenInstructionTextHeader)
            .setText(text)

        /**
         *  Code play ground view binding
         */
        submitButtonSeparator = playgroundLayout.findViewById(R.id.submitButtonSeparator)

        codeSubmitButton = playgroundLayout.findViewById(R.id.stepQuizSubmitButton)
        codeSubmitButton.setText(R.string.step_quiz_code_run_solution_button_text)
        codeSubmitButton.setIconResource(R.drawable.ic_run)
        codeSubmitButton.iconPadding =
            requireContext().resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_action_button_icon_padding)
        codeSubmitButton.setOnClickListener { submitCodeActionClick() }

        retryButton = playgroundLayout.findViewById(R.id.stepQuizRetryButton)
        retryButton.setOnClickListener {
            val dialog = ResetCodeDialogFragment.newInstance()
            if (!dialog.isAdded) {
                dialog.show(childFragmentManager, null)
            }
        }
        if (isShowRetryButton) {
            retryButton.visibility = View.VISIBLE
        }

        setupCodeToolbarAdapter()
        setupKeyboardExtension()

        codeLayoutDelegate = CodeLayoutDelegate(
            codeLayout = codeLayout,
            step = step,
            codeTemplates = codeTemplates,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                instructionsLayout.findViewById(R.id.stepQuizCodeFullscreenInstructionDetails),
                false
            ),
            codeToolbarAdapter = codeToolbarAdapter,
        )

        codeLayoutDelegate.setLanguage(lang, code)
        codeLayoutDelegate.setDetailsContentData(lang)
        viewBinding.fullScreenCodeViewPager.setCurrentItem(CODE_TAB, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_LANG, lang)
        outState.putString(ARG_CODE, codeLayout.text.toString())
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
        (parentFragment as? Callback)
            ?.onSyncCodeStateWithParent(codeLayout.text.toString())
        super.onPause()
    }

    override fun onReset() {
        codeLayoutDelegate.setLanguage(lang)
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
                        requireContext().resources.getDimensionPixelSize(R.dimen.step_quiz_fullscreen_code_layout_bottom_padding)
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
        (parentFragment as? Callback)
            ?.onSyncCodeStateWithParent(codeLayout.text.toString(), onSubmitClicked = true)
        dismiss()
    }

    interface Callback {
        fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean = false)
    }
}