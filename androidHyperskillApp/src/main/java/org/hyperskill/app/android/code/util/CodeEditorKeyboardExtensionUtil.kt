package org.hyperskill.app.android.code.util

import android.content.Context
import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.view.adapter.CodeToolbarAdapter
import org.hyperskill.app.android.code.view.widget.CodeEditorLayout
import org.hyperskill.app.android.view.base.ui.extension.setOnKeyboardOpenListener

object CodeEditorKeyboardExtensionUtil {

    fun interface CodeEditorKeyboardListener {
        fun onCodeEditorKeyboardStateChanged(
            isKeyboardShown: Boolean,
            toolbarHeight: Int
        )
    }

    fun setupKeyboardExtension(
        context: Context,
        rootView: View,
        recyclerView: RecyclerView,
        codeLayout: CodeEditorLayout,
        codeToolbarAdapter: CodeToolbarAdapter,
        isToolbarEnabled: () -> Boolean = { true },
        codeEditorKeyboardListener: CodeEditorKeyboardListener? = null
    ) {
        codeToolbarAdapter.onSymbolClickListener = CodeToolbarAdapter.OnSymbolClickListener { symbol, offset ->
            applySymbolTo(codeLayout, symbol, offset)
        }

        codeLayout.codeToolbarAdapter = codeToolbarAdapter

        setupRecycler(context, recyclerView, codeToolbarAdapter)

        // Flag is necessary,
        // because keyboard listener is constantly invoked
        // (probably global layout listener reacts to view changes)
        var keyboardShown = false

        setOnKeyboardOpenListener(
            rootView,
            onKeyboardHidden = {
                if (keyboardShown) {
                    recyclerView.isInvisible = true
                    codeLayout.isNestedScrollingEnabled = true
                    codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                        isKeyboardShown = false,
                        toolbarHeight = 0
                    )
                    keyboardShown = false
                }
            },
            onKeyboardShown = {
                if (!keyboardShown) {
                    if (isToolbarEnabled()) {
                        recyclerView.isInvisible = false
                    }
                    codeLayout.isNestedScrollingEnabled = false
                    codeEditorKeyboardListener?.onCodeEditorKeyboardStateChanged(
                        isKeyboardShown = true,
                        toolbarHeight = recyclerView.height
                    )
                    keyboardShown = true
                }
            }
        )
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