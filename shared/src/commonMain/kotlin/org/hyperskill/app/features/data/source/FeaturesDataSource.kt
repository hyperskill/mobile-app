package org.hyperskill.app.features.data.source

import org.hyperskill.app.profile.domain.model.FeatureValues
import org.hyperskill.app.profile.domain.model.FeaturesMap

interface FeaturesDataSource {
    fun getFeaturesMap(): FeaturesMap
    fun getFeatureValues(): FeatureValues
}