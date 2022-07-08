import shared
import SwiftUI

final class ProfileViewModel: FeatureViewModel<
  ProfileFeatureState,
  ProfileFeatureMessage,
  ProfileFeatureActionViewAction
> {
    private let presentationDescription: ProfilePresentationDescription

    init(presentationDescription: ProfilePresentationDescription, feature: Presentation_reduxFeature) {
        self.presentationDescription = presentationDescription
        super.init(feature: feature)
    }
}
