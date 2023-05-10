package org.hyperskill.app.android.latex.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import kotlin.math.abs
import org.hyperskill.app.android.BuildConfig
import org.hyperskill.app.android.latex.view.model.TextAttributes
import org.hyperskill.app.android.latex.view.model.block.HorizontalScrollBlock
import ru.nobird.android.view.base.ui.extension.px
import ru.nobird.android.view.base.ui.extension.toDp
import ru.nobird.android.view.base.ui.extension.toFloat

@SuppressLint("AddJavascriptInterface")
class LatexWebView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : WebView(context, attrs, defStyleAttr),
    View.OnLongClickListener,
    View.OnClickListener,
    View.OnTouchListener {

    companion object {
        private const val MAX_CLICK_DURATION = 200

        private const val MIME_TYPE = "text/html"
        private const val ENCODING = "UTF-8"
        private const val ASSETS = "file:///android_asset/"
    }

    var attributes = TextAttributes.fromAttributeSet(context, attrs)
        set(value) {
            field = value
            settings.defaultFontSize = value.textSize.toInt()

            setOnLongClickListener(this.takeIf { !value.textIsSelectable })
        }

    var onImageClickListener: ((path: String) -> Unit)? = null

    var text: String = ""
        set(value) {
            if (field != value) { // update check in order to prevent re-rendering
                field = value
                loadDataWithBaseURL(ASSETS, text, MIME_TYPE, ENCODING, "")
            }
        }

    private var scrollState =
        ScrollState()

    private var startX = 0f
    private var startY = 0f

    init {
        setBackgroundColor(context.getColor(android.R.color.transparent))
        setOnLongClickListener(this.takeIf { !attributes.textIsSelectable })

        setOnTouchListener(this)
        setOnClickListener(this)

        isFocusable = true
        isFocusableInTouchMode = true

        settings.domStorageEnabled = true
        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.defaultFontSize = attributes.textSize.toInt()
        settings.mediaPlaybackRequiresUserGesture = false

        addJavascriptInterface(OnScrollWebListener(), HorizontalScrollBlock.SCRIPT_NAME)
        isSoundEffectsEnabled = false
    }

    override fun onLongClick(v: View?): Boolean = true

    override fun onClick(v: View?) {
        val hr = hitTestResult
        try {
            if (hr.type == HitTestResult.IMAGE_TYPE) {
                onImageClickListener?.invoke(hr.extra ?: return)
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP &&
            event.downTime - event.eventTime < MAX_CLICK_DURATION
        ) {
            performClick()
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y

                val dpx = event.x.px.toDp().toFloat()
                val dpy = event.y.px.toDp().toFloat()
                evaluateJavascript("${HorizontalScrollBlock.METHOD_NAME}($dpx, $dpy)", null)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = startX - event.x
                val dy = startY - event.y
                event.setLocation(event.x, event.y)

                if (abs(dx) > abs(dy) && canScrollHorizontally(dx.toInt())) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                scrollState.reset()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun canScrollHorizontally(dx: Int): Boolean =
        super.canScrollHorizontally(dx) ||
            dx < 0 && scrollState.canScrollLeft ||
            dx > 0 && scrollState.canScrollRight

    private inner class OnScrollWebListener {
        @JavascriptInterface
        fun onScroll(offsetWidth: Float, scrollWidth: Float, scrollLeft: Float) {
            scrollState.canScrollLeft = scrollLeft > 0
            scrollState.canScrollRight = offsetWidth + scrollLeft < scrollWidth
        }
    }

    private class ScrollState(
        internal var canScrollLeft: Boolean = false,
        internal var canScrollRight: Boolean = false
    ) {
        internal fun reset() {
            canScrollLeft = false
            canScrollRight = false
        }
    }
}