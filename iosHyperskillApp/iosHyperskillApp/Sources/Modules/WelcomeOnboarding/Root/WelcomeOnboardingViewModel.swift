import Combine
import Foundation
import shared

final class WelcomeOnboardingViewModel: FeatureViewModel<
  WelcomeOnboardingFeature.State,
  WelcomeOnboardingFeatureMessage,
  WelcomeOnboardingFeatureActionViewAction
> {
    weak var viewController: WelcomeOnboardingViewControllerProtocol?

    private var objectWillChangeSubscription: AnyCancellable?

    init(feature: Presentation_reduxFeature) {
        super.init(feature: feature)

        self.objectWillChangeSubscription = objectWillChange.sink { [weak self] _ in
            self?.mainScheduler.schedule { [weak self] in
                guard let self else {
                    return
                }

                self.viewController?.displayState(self.state)
            }
        }
        self.onViewAction = { [weak self] viewAction in
            self?.mainScheduler.schedule { [weak self] in
                self?.viewController?.displayViewAction(viewAction)
            }
        }

        onNewMessage(WelcomeOnboardingFeatureMessageInitialize())
    }

    override func shouldNotifyStateDidChange(
        oldState: WelcomeOnboardingFeature.State,
        newState: WelcomeOnboardingFeature.State
    ) -> Bool {
        !oldState.isEqual(newState)
    }

    func doStartJourneyAction() {
        onNewMessage(WelcomeOnboardingFeatureMessageStartJourneyClicked())
    }

    func doQuestionnaireItemAction(
        questionnaireType: WelcomeQuestionnaireType,
        itemType: WelcomeQuestionnaireItemType
    ) {
        onNewMessage(
            WelcomeOnboardingFeatureMessageQuestionnaireItemClicked(
                questionnaireType: questionnaireType,
                itemType: itemType
            )
        )
    }
}
