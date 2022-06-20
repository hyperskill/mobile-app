package org.hyperskill.app.android.latex.injection

import android.content.Context
import org.hyperskill.app.android.latex.view.mapper.LatexTextMapper
import org.hyperskill.app.android.latex.view.mapper.LatexWebViewMapper

class PlatformLatexComponentImpl(
    private val context: Context
) : PlatformLatexComponent {
    override val latexTextMapper: LatexTextMapper
        get() = LatexTextMapper()

    override val latexWebViewMapper: LatexWebViewMapper
        get() = LatexWebViewMapper(context)
}