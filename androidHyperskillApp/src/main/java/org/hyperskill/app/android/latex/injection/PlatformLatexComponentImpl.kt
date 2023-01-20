package org.hyperskill.app.android.latex.injection

import android.content.Context
import org.hyperskill.app.android.latex.view.mapper.LatexTextMapper
import org.hyperskill.app.android.latex.view.mapper.LatexWebViewMapper
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

class PlatformLatexComponentImpl(
    private val context: Context,
    private val networkEndpointConfigInfo: NetworkEndpointConfigInfo
) : PlatformLatexComponent {
    override val latexTextMapper: LatexTextMapper
        get() = LatexTextMapper(networkEndpointConfigInfo)

    override val latexWebViewMapper: LatexWebViewMapper
        get() = LatexWebViewMapper(context)
}