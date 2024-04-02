package org.hyperskill.app.android.latex.view.model.block

import android.webkit.JavascriptInterface

class HorizontalScrollBlock : ContentBlock {

    interface Listener {
        @JavascriptInterface
        fun onScroll(offsetWidth: Float, scrollWidth: Float, scrollLeft: Float)

        @JavascriptInterface
        fun onCodeScroll(offsetWidth: Float, scrollWidth: Float)
    }

    companion object {
        const val SCRIPT_NAME = "scrollListener"
        const val METHOD_NAME = "measureScroll"

        private val style = """
            <style>
                body > * {
                    max-width: 100%%;
                    overflow-x: scroll;
                    vertical-align: middle;
                }
                body > .no-scroll {
                    overflow: visible !important;
                }
            </style>
        """.trimIndent()

        private val script = """
            <script type="text/javascript">
                function $METHOD_NAME(x, y) {
                    var elem = document.elementFromPoint(x, y);
                    while(isNotGlobalScrollElement(elem) && !isCodeElement(elem)) {
                        elem = elem.parentElement;
                    }
                    if (isCodeElement(elem)) {
                        $SCRIPT_NAME.onCodeScroll(elem.offsetWidth, elem.scrollWidth);
                    } else {
                        $SCRIPT_NAME.onScroll(elem.offsetWidth, elem.scrollWidth, elem.scrollLeft);
                    }                
                }
                function isNotGlobalScrollElement(elem) {
                    return elem.parentElement.tagName !== 'BODY' && 
                        elem.parentElement.tagName !== 'HTML' && 
                        elem.className !== 'CodeMirror-scroll' && 
                        elem.className !== 'code-output'
                }
                function isCodeElement(elem) {
                    return elem.tagName === 'CODE'
                }
            </script>
        """.trimIndent()
    }

    override val header: String = style + script

    override fun isEnabled(content: String): Boolean =
        true
}