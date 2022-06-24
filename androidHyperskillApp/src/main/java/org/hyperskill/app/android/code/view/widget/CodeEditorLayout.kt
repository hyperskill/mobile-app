package org.hyperskill.app.android.code.view.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.highlight.themes.CodeTheme
import org.hyperskill.app.android.code.highlight.themes.Presets
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.core.extensions.insertText
import ru.nobird.android.view.base.ui.extension.inflate

class CodeEditorLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : NestedScrollView(context, attrs, defStyleAttr) {
    private val codeEditor: CodeEditor

    var theme: CodeTheme
        get() = codeEditor.theme
        set(value) {
            setBackgroundColor(value.background)
            codeEditor.theme = value
        }

    val text: CharSequence
        get() = codeEditor.text.toString()

    var lang: String
        get() = codeEditor.lang
        set(value) {
            codeEditor.lang = value
        }

    val indentSize: Int
        get() = codeEditor.indentSize

    var codeToolbarAdapter: CodeToolbarAdapter?
        get() = codeEditor.codeToolbarAdapter
        set(value) {
            codeEditor.codeToolbarAdapter = value
        }

    init {
        inflate(R.layout.view_code_editor, true)
        codeEditor = findViewById(R.id.codeEdit)

        val isNightMode =
            context.obtainStyledAttributes(intArrayOf(R.attr.isNightMode)).use {
                it.getBoolean(0, false)
            }

        theme =
            if (isNightMode) {
                Presets.themes[2]
            } else {
                Presets.themes[0]
            }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        codeEditor.scrollContainer = this
    }

    override fun onDetachedFromWindow() {
        codeEditor.scrollContainer = null
        super.onDetachedFromWindow()
    }

    fun setText(text: String?) {
        text?.let { codeEditor.setText(it) }
    }

    fun insertText(text: String, offset: Int) {
        codeEditor.insertText(text, offset)
    }

    fun setTextIfChanged(text: String) {
        if (this.text.toString() != text) {
            codeEditor.setText(text)
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        codeEditor.isEnabled = enabled
    }

    /**
     * In case when [NestedScrollView] is inside another [NestedScrollView]
     * startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH) is being called at start of fling
     * but stopNestedScroll(ViewCompat.TYPE_NON_TOUCH) wasn't.
     *
     * That behaviour leads to scroll issues when nested one was scrolled.
     */
    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
    }
}