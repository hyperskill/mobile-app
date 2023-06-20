package org.hyperskill.app.${package}.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Action
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Message
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.State
import ru.nobird.app.presentation.redux.feature.Feature

class ${injectionComponentName}Impl(private val appGraph: AppGraph) : ${injectionComponentName} {

    override val ${componentFeatureVariable}: Feature<State, Message, Action>
        get() = ${featureBuilderName}.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}