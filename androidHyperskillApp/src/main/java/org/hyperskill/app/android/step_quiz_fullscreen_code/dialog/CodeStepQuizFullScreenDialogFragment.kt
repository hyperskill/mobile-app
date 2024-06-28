package org.hyperskill.app.android.step_quiz_fullscreen_code.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import by.kirich1409.viewbindingdelegate.viewBinding
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.applyInsetter
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.util.CodeEditorKeyboardExtensionUtil
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.extensions.setTintList
import org.hyperskill.app.android.databinding.DialogStepQuizCodeFullscreenBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeFullscreenInstructionBinding
import org.hyperskill.app.android.databinding.LayoutStepQuizCodeFullscreenPlaygroundBinding
import org.hyperskill.app.android.step_content_text.view.delegate.TextStepContentDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeLayoutDelegate
import org.hyperskill.app.android.step_quiz_code.view.delegate.CodeQuizInstructionDelegate
import org.hyperskill.app.android.step_quiz_code.view.model.CodeStepQuizConfigFactory
import org.hyperskill.app.android.step_quiz_code.view.model.config.CodeStepQuizConfig
import org.hyperskill.app.android.step_quiz_fullscreen_code.adapter.CodeStepQuizFullScreenPagerAdapter
import org.hyperskill.app.code.domain.model.ProgrammingLanguage
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

    private var instructionsBinding: LayoutStepQuizCodeFullscreenInstructionBinding? = null
    private var playgroundBinding: LayoutStepQuizCodeFullscreenPlaygroundBinding? = null

    private var codeToolbarAdapter: CodeToolbarAdapter? = null

    private var lang: String by argument()
    private var code: String by argument()
    private var step: Step by argument(serializer = Step.serializer())
    private var isShowRetryButton: Boolean by argument()
    private var titleRes: Int by argument()

    private val config: CodeStepQuizConfig by lazy(LazyThreadSafetyMode.NONE) {
        CodeStepQuizConfigFactory.create(step)
    }

    private var stepQuizStatsTextMapper: StepQuizStatsTextMapper? = null
    private lateinit var textStepContentDelegate: TextStepContentDelegate

    private var isCodeSyncedAfterSubmissionClick: Boolean = false

    private val callback: Callback?
        get() = parentFragment as? Callback

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
        textStepContentDelegate = TextStepContentDelegate(lifecycle)
        initCodeToolbarAdapter()
    }

    private fun injectComponent() {
        stepQuizStatsTextMapper = StepQuizStatsTextMapper(
            HyperskillApp.graph().commonComponent.resourceProvider,
            HyperskillApp.graph().commonComponent.dateFormatter
        )
    }

    private fun initCodeToolbarAdapter() {
        codeToolbarAdapter = CodeToolbarAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.dialog_step_quiz_code_fullscreen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initViewPager()
        setupDescription()
        setupCodeDetails()
        setupCodePlayground()
        applyWindowInsets()
        viewBinding.fullScreenCodeViewPager.setCurrentItem(CODE_TAB, false)
    }

    private fun setupToolbar() {
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
    }

    private fun onResetClick() {
        syncCodeStateWithParent()
        callback?.onResetCodeClick()
    }

    private fun initViewPager() {
        val pagerAdapter =
            CodeStepQuizFullScreenPagerAdapter(requireContext())

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

        instructionsBinding =
            LayoutStepQuizCodeFullscreenInstructionBinding
                .bind(pagerAdapter.getViewAt(INSTRUCTION_TAB))
        playgroundBinding =
            LayoutStepQuizCodeFullscreenPlaygroundBinding
                .bind(pagerAdapter.getViewAt(CODE_TAB))
    }

    private fun setupDescription() {
        val descriptionBinding = requireNotNull(instructionsBinding).stepQuizCodeFullscreenInstructionTextHeader
        textStepContentDelegate.setup(
            context = requireContext(),
            latexView = descriptionBinding.stepPracticeDetailsContent.root,
            step = step,
            viewLifecycle = viewLifecycleOwner.lifecycle
        )
        with(descriptionBinding) {
            stepPracticeDetailsStepTitle.text = step.title
            stepPracticeDetailsArrow.isVisible = false
            stepPracticeDetailsContent.root.isVisible = true
        }
    }

    private fun setupCodeDetails() {
        codeLayoutDelegate = CodeLayoutDelegate(
            codeLayout = requireNotNull(playgroundBinding?.fullScreenCodeEditorLayout),
            config = config,
            codeQuizInstructionDelegate = CodeQuizInstructionDelegate(
                binding = requireNotNull(instructionsBinding).stepQuizCodeFullscreenInstructionDetails,
                isCollapsible = false,
                onDetailsIsExpandedStateChanged = {}
            ),
            codeToolbarAdapter = codeToolbarAdapter
        )

        codeLayoutDelegate.setLanguage(lang, code)
        codeLayoutDelegate.setDetailsContentData(lang)
    }

    private fun setupCodePlayground() {
        playgroundBinding?.fullScreenSubmitButton?.setOnClickListener { submitCodeActionClick() }
        playgroundBinding?.fullScreenRetryButton?.apply {
            setOnClickListener { onResetClick() }
            isVisible = isShowRetryButton
        }
        setupCodeEditorKeyboardExtension()
    }

    private fun applyWindowInsets() {
        viewBinding.fullScreenCenteredToolbar.centeredToolbar.applyInsetter {
            type(statusBars = true) {
                padding()
            }
        }
        playgroundBinding?.fullScreenPlaygroundFooter?.applyInsetter {
            type(navigationBars = true, ime = true) {
                padding(animated = true)
            }
            playgroundBinding?.fullScreenCodeEditorLayout?.let {
                syncTranslationTo(it)
            }
            consume(Insetter.CONSUME_ALL)
        }
        instructionsBinding?.stepQuizCodeFullscreenInstructionLayout?.applyInsetter {
            type(navigationBars = true) {
                padding()
            }
        }
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
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.setWindowAnimations(R.style.ThemeOverlay_AppTheme_Dialog_Fullscreen)
            }
    }

    override fun onPause() {
        if (!isCodeSyncedAfterSubmissionClick) {
            syncCodeStateWithParent()
        }
        super.onPause()
    }

    override fun onDestroyView() {
        instructionsBinding = null
        playgroundBinding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        codeToolbarAdapter = null
    }

    /**
     * Called receiving new state from feature (calling [Callback] methods).
     */
    fun onNewCode(code: String?) {
        this.code = code ?: config.initialCode
        codeLayoutDelegate.setLanguage(lang, code)
    }

    private fun setupCodeEditorKeyboardExtension() {
        val codeLayout = requireNotNull(playgroundBinding?.fullScreenCodeEditorLayout)
        CodeEditorKeyboardExtensionUtil.setupKeyboardExtension(
            context = requireContext(),
            rootView = viewBinding.root,
            recyclerView = requireNotNull(
                playgroundBinding
                    ?.fullScreenCodeKeyboardExtension
                    ?.stepQuizCodeKeyboardExtensionRecycler
            ),
            codeLayout = codeLayout,
            codeToolbarAdapter = requireNotNull(codeToolbarAdapter),
            isToolbarEnabled = {
                // We show the keyboard extension only when "Code" tab is opened
                isResumed && viewBinding.fullScreenCodeViewPager.currentItem == CODE_TAB
            },
            onToolbarSymbolClicked = { symbol, resultCode ->
                callback?.onKeyboardExtensionSymbolClicked(symbol, resultCode)
            },
            codeEditorKeyboardListener = { isKeyboardShown, insets, _ ->
                if (isResumed) {
                    setViewsVisibility(isKeyboardShown, insets)
                }
            }
        )
    }

    private fun setViewsVisibility(
        isKeyboardShown: Boolean,
        insets: WindowInsetsCompat
    ) {
        playgroundBinding?.fullScreenButtonContainer?.isVisible = !isKeyboardShown
        viewBinding.fullScreenAppBar.isVisible = !isKeyboardShown
        playgroundBinding?.fullScreenCodeEditorLayout
            ?.updatePadding(
                top = if (isKeyboardShown) {
                    insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
                } else {
                    0
                }
            )
    }

    private fun submitCodeActionClick() {
        syncCodeStateWithParent(onSubmitClicked = true)
        isCodeSyncedAfterSubmissionClick = true
        dismiss()
    }

    private fun syncCodeStateWithParent(onSubmitClicked: Boolean = false) {
        val text = playgroundBinding?.fullScreenCodeEditorLayout?.text
        if (text != null) {
            callback?.onSyncCodeStateWithParent(
                code = text,
                onSubmitClicked = onSubmitClicked
            )
        }
    }

    interface Callback {
        fun onSyncCodeStateWithParent(code: String, onSubmitClicked: Boolean = false)
        fun onResetCodeClick()

        fun onKeyboardExtensionSymbolClicked(symbol: String, code: String)
    }

    data class Params(
        val lang: String,
        val code: String,
        val step: Step,
        val isShowRetryButton: Boolean
    ) {
        val titleRes: Int
            get() = if (lang == ProgrammingLanguage.SQL.languageName) {
                org.hyperskill.app.R.string.step_quiz_sql_title
            } else {
                org.hyperskill.app.R.string.step_quiz_code_title
            }
    }
}