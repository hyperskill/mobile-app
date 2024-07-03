package org.hyperskill.app.android.code.util

import android.content.Context
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.chrisbanes.insetter.Insetter
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.core.extensions.doOnApplyWindowInsets

object CodeEditorKeyboardExtensionUtil {

    fun interface CodeEditorKeyboardListener {
        fun onCodeEditorKeyboardStateChanged(
            isKeyboardShown: Boolean,
            insets: WindowInsetsCompat,
            toolbarHeight: Int
        )
    }

    fun interface OnToolbarSymbolClickListener {
        fun onToolbarSymbolClick(symbol: String, resultCode: String)
    }

    fun setupKeyboardExtension(
        rootView: View,
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

        rootView.doOnApplyWindowInsets(Insetter.CONSUME_NONE) { _, insets, _ ->
            val imeInsetsType = WindowInsetsCompat.Type.ime()
            if (insets.isVisible(imeInsetsType)) {
                if (isToolbarEnabled()) {
                    recyclerView.isInvisible = false
                }
                codeLayout.isNestedScrollingEnabled = false
                codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                    isKeyboardShown = true,
                    insets = insets,
                    toolbarHeight = recyclerView.height
                )
            } else {
                recyclerView.isInvisible = true
                codeLayout.isNestedScrollingEnabled = true
                codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                    isKeyboardShown = false,
                    insets = insets,
                    toolbarHeight = 0
                )
            }
        }
    }

    private fun setupRecycler(context: Context, recyclerView: RecyclerView, codeToolbarAdapter: CodeToolbarAdapter) {
        with(recyclerView) {
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