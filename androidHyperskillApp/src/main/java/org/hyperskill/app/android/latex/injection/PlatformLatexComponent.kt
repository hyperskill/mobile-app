package org.hyperskill.app.android.latex.injection

import org.hyperskill.app.android.latex.view.mapper.LatexTextMapper
import org.hyperskill.app.android.latex.view.mapper.LatexWebViewMapper

interface PlatformLatexComponent {
    val latexTextMapper: LatexTextMapper
    val latexWebViewMapper: LatexWebViewMapper
}