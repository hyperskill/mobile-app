import Foundation
import shared
import SwiftUI

final class WelcomeOnboardingQuestionnaireAssembly: Assembly {
    private let type: WelcomeQuestionnaireType

    private let onViewDidAppear: () -> Void
    private let onQuestionnaireItemTap: (WelcomeQuestionnaireItem) -> Void

    init(
        type: WelcomeQuestionnaireType,
        onViewDidAppear: @escaping () -> Void,
        onQuestionnaireItemTap: @escaping (WelcomeQuestionnaireItem) -> Void
    ) {
        self.type = type
        self.onViewDidAppear = onViewDidAppear
        self.onQuestionnaireItemTap = onQuestionnaireItemTap
    }

    func makeModule() -> WelcomeOnboardingQuestionnaireView {
        let welcomeOnboardingComponent = AppGraphBridge.sharedAppGraph.buildWelcomeQuestionnaireComponent()

        let welcomeQuestionnaireViewStateMapper = welcomeOnboardingComponent.welcomeQuestionnaireViewStateMapper
        let welcomeQuestionnaireViewState =
            welcomeQuestionnaireViewStateMapper.mapQuestionnaireTypeToViewState(type: type)

        return WelcomeOnboardingQuestionnaireView(
            viewState: welcomeQuestionnaireViewState,
            onViewDidAppear: onViewDidAppear,
            onQuestionnaireItemTap: onQuestionnaireItemTap
        )
    }
}
