package org.hyperskill.app.android.code.util

import android.content.Context
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.applyInsetter
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.core.extensions.doOnApplyWindowInsets

object CodeEditorKeyboardExtensionUtil {

    fun interface CodeEditorKeyboardListener {
        fun onCodeEditorKeyboardStateChanged(
            isKeyboardShown: Boolean,
            toolbarHeight: Int
        )
    }

    fun interface OnToolbarSymbolClickListener {
        fun onToolbarSymbolClick(symbol: String, resultCode: String)
    }

    fun setupKeyboardExtension(
        window: Window,
        context: Context,
        recyclerView: RecyclerView,
        codeLayout: CodeEditorLayout,
        codeToolbarAdapter: CodeToolbarAdapter,
        isToolbarEnabled: () -> Boolean = { true },
        onToolbarSymbolClicked: OnToolbarSymbolClickListener? = null,
        codeEditorKeyboardListener: CodeEditorKeyboardListener? = null
    ) {
        codeToolbarAdapter.onSymbolClickListener = CodeToolbarAdapter.OnSymbolClickListener { symbol, offset ->
            // insert should be done first
            applySymbolTo(codeLayout, symbol, offset)
            onToolbarSymbolClicked?.onToolbarSymbolClick(
                symbol = symbol,
                resultCode = codeLayout.text
            )
        }

        codeLayout.codeToolbarAdapter = codeToolbarAdapter

        setupRecycler(context, recyclerView, codeToolbarAdapter)

        window.decorView.doOnApplyWindowInsets(Insetter.CONSUME_NONE) { _, insets, _ ->
            if (insets.isVisible(WindowInsetsCompat.Type.ime())) {
                if (isToolbarEnabled()) {
                    recyclerView.isInvisible = false
                }
                codeLayout.isNestedScrollingEnabled = false
                codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                    isKeyboardShown = true,
                    toolbarHeight = recyclerView.height
                )
            } else {
                recyclerView.isInvisible = true
                codeLayout.isNestedScrollingEnabled = true
                codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                    isKeyboardShown = false,
                    toolbarHeight = 0
                )
            }
        }
    }

    private fun setupRecycler(context: Context, recyclerView: RecyclerView, codeToolbarAdapter: CodeToolbarAdapter) {
        with(recyclerView) {
            applyInsetter {
                type(ime = true) {
                    margin(animated = true)
                }
            }
            adapter = codeToolbarAdapter
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setBackgroundResource(R.color.color_elevation_overlay_2dp)
            clipToPadding = false
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            isMotionEventSplittingEnabled = false
            isInvisible = true
        }
    }

    private fun applySymbolTo(codeEditorLayout: CodeEditorLayout, symbol: String, offset: Int) {
        codeEditorLayout.insertText(
            mapToolbarSymbolToPrintable(
                symbol,
                codeEditorLayout.indentSize
            ),
            offset
        )
    }

    private fun mapToolbarSymbolToPrintable(symbol: String, indentSize: Int): String =
        if (symbol.equals("tab", ignoreCase = true)) {
            " ".repeat(indentSize)
        } else {
            symbol
        }
}