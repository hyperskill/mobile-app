package org.hyperskill.app.${package}.injection

import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Action
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.Message
import org.hyperskill.app.${package}.presentation.${featureNameWithPostfix}.State
import ru.nobird.app.presentation.redux.feature.Feature

interface ${injectionComponentName} {
    val ${componentFeatureVariable}: Feature<State, Message, Action>
}