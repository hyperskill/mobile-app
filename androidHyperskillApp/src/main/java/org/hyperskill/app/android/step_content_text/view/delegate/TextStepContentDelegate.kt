package org.hyperskill.app.android.step_content_text.view.delegate

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.hyperskill.app.android.R
import org.hyperskill.app.android.latex.view.widget.LatexView
import org.hyperskill.app.android.latex.view.widget.LatexWebView
import org.hyperskill.app.step.domain.model.Step

class TextStepContentDelegate(
    fragmentLifecycle: Lifecycle
) : LifecycleObserver {

    private var latexWebView: LatexWebView? = null

    init {
        fragmentLifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                if (event.targetState == Lifecycle.State.DESTROYED) {
                    latexWebView = null
                }
            }
        )
    }

    fun setup(
        context: Context,
        latexView: LatexView,
        step: Step,
        viewLifecycle: Lifecycle
    ) {
        val webView = latexWebView ?: createLatexWebView(context, latexView)
        this.latexWebView = webView
        latexView.addView(webView)
        latexView.setText(step.block.text)
        viewLifecycle.addObserver(
            object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event.targetState == Lifecycle.State.DESTROYED) {
                        latexView.removeView(latexWebView)
                    }
                    viewLifecycle.removeObserver(this)
                }
            }
        )
    }

    private fun createLatexWebView(context: Context, latexView: LatexView): LatexWebView =
        LayoutInflater
            .from(context.applicationContext)
            .inflate(R.layout.layout_latex_webview, latexView, false) as LatexWebView
}