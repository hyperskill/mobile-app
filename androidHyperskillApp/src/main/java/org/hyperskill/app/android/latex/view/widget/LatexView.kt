package org.hyperskill.app.android.latex.view.widget

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.ExternalLinkWebViewClient
import org.hyperskill.app.android.latex.view.mapper.LatexTextMapper
import org.hyperskill.app.android.latex.view.mapper.LatexWebViewMapper
import org.hyperskill.app.android.latex.view.model.LatexData
import org.hyperskill.app.android.latex.view.model.TextAttributes
import ru.nobird.android.view.base.ui.extension.inflate

class LatexView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        private fun TextView.setAttributes(textAttributes: TextAttributes) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textAttributes.textSize)
            setTextColor(textAttributes.textColor)
            highlightColor = textAttributes.textColorHighlight
            setTextIsSelectable(textAttributes.textIsSelectable)
            typeface = ResourcesCompat.getFont(context, textAttributes.fontResId)
        }
    }

    private val latexTextMapper: LatexTextMapper

    private val latexWebViewMapper: LatexWebViewMapper

    @IdRes
    private val textViewId: Int

    @IdRes
    private val webViewId: Int

    var textView: TextView? = null
        private set

    var webView: LatexWebView? = null
        private set

    var attributes = TextAttributes.fromAttributeSet(context, attrs)
        set(value) {
            field = value
            textView?.setAttributes(value)
            webView?.attributes = value
        }

    var latexData: LatexData? = null
        set(value) {
            textView?.isVisible = value is LatexData.Text
            webView?.isVisible = value is LatexData.Web

            if (field == value) return
            field = value

            if (value == null) return

            when (value) {
                is LatexData.Text ->
                    textView?.text = value.text

                is LatexData.Web -> {
                    webView?.apply {
                        text = latexWebViewMapper.mapLatexData(value, attributes)

                        // TODO Switch to WebViewAssetLoader
                        /**
                         * Allow WebView to open file://
                         */
                        settings.allowFileAccess = true
                        /**
                         * Kotlin Playground downloads a file with Kotlin versions which generates a mistake,
                         * if allowUniversalAccessFromFileURLs is false
                         */
                        settings.allowUniversalAccessFromFileURLs = value.settings.allowUniversalAccessFromFileURLs
                    }
                }
            }
        }

    var webViewClient: WebViewClient? = null
        set(value) {
            field = value
            if (ViewCompat.isAttachedToWindow(this)) {
                webView?.webViewClient = requireNotNull(webViewClient)
            }
        }

    init {
        val platformLatexComponent = HyperskillApp.graph().buildPlatformLatexComponent()
        latexTextMapper = platformLatexComponent.latexTextMapper
        latexWebViewMapper = platformLatexComponent.latexWebViewMapper

        val array = context.obtainStyledAttributes(attrs, R.styleable.LatexView)

        try {
            val textId = array.getResourceId(R.styleable.LatexView_textViewId, 0)
            if (textId == 0) {
                textViewId = R.id.latex_textview
                inflate(R.layout.layout_latex_textview, attachToRoot = true)
            } else {
                textViewId = textId
            }

            val webId = array.getResourceId(R.styleable.LatexView_webViewId, 0)
            if (webId == 0) {
                webViewId = R.id.latex_webview
                inflate(R.layout.layout_latex_webview, attachToRoot = true)
            } else {
                webViewId = webId
            }
        } finally {
            array.recycle()
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        when (child?.id) {
            textViewId -> {
                val textView = child as TextView
                this.textView = textView
                textView.setAttributes(attributes)
                textView.movementMethod = LinkMovementMethod.getInstance()
            }

            webViewId -> {
                val webView = child as LatexWebView
                this.webView = webView
                webView.attributes = attributes
                if (ViewCompat.isAttachedToWindow(this)) {
                    webView.webViewClient = getOrCreateWebViewClient()
                }
            }
        }
        super.addView(child, index, params)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        webView?.webViewClient = getOrCreateWebViewClient()
    }

    override fun onDetachedFromWindow() {
        webView?.onImageClickListener = null
        super.onDetachedFromWindow()
    }

    fun setText(text: String?) {
        latexData = text?.let(latexTextMapper::mapToLatexText)
    }

    private fun getOrCreateWebViewClient(): WebViewClient =
        webViewClient ?: ExternalLinkWebViewClient(context)
}