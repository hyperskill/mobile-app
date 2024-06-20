import Foundation
import shared
import SwiftUI

final class WelcomeOnboardingQuestionnaireAssembly: Assembly {
    private let type: WelcomeQuestionnaireType

    private let onQuestionnaireItemTap: (WelcomeQuestionnaireItem) -> Void

    init(type: WelcomeQuestionnaireType, onQuestionnaireItemTap: @escaping (WelcomeQuestionnaireItem) -> Void) {
        self.type = type
        self.onQuestionnaireItemTap = onQuestionnaireItemTap
    }

    func makeModule() -> WelcomeOnboardingQuestionnaireView {
        let welcomeOnboardingComponent = AppGraphBridge.sharedAppGraph.buildWelcomeQuestionnaireComponent()

        let welcomeQuestionnaireViewStateMapper = welcomeOnboardingComponent.welcomeQuestionnaireViewStateMapper
        let welcomeQuestionnaireViewState =
            welcomeQuestionnaireViewStateMapper.mapQuestionnaireTypeToViewState(type: type)

        return WelcomeOnboardingQuestionnaireView(
            viewState: welcomeQuestionnaireViewState,
            onQuestionnaireItemTap: onQuestionnaireItemTap
        )
    }
}
