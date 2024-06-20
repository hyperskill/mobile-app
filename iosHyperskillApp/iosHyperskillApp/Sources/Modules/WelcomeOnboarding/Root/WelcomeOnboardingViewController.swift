import Foundation
import shared
import SwiftUI
import UIKit

protocol WelcomeOnboardingViewControllerProtocol: AnyObject {
    func displayState(_ state: WelcomeOnboardingFeature.State)
    func displayViewAction(_ viewAction: WelcomeOnboardingFeatureActionViewAction)
}

final class WelcomeOnboardingViewController: UIViewController {
    private let viewModel: WelcomeOnboardingViewModel

    init(viewModel: WelcomeOnboardingViewModel) {
        self.viewModel = viewModel
        super.init(nibName: nil, bundle: nil)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        viewModel.startListening()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        viewModel.stopListening()
    }
}

extension WelcomeOnboardingViewController: WelcomeOnboardingViewControllerProtocol {
    func displayState(_ state: WelcomeOnboardingFeature.State) {
        if state.isNextLearningActivityLoadingShown {
            ProgressHUD.show()
        } else {
            ProgressHUD.dismissWithDelay()
        }
    }

    func displayViewAction(_ viewAction: WelcomeOnboardingFeatureActionViewAction) {
        switch WelcomeOnboardingFeatureActionViewActionKs(viewAction) {
        case .completeWelcomeOnboarding(let data):
            print(data.stepRoute as Any)
            #warning("TODO")
        case .navigateTo(let navigateToViewAction):
            handleNavigateToViewAction(
                WelcomeOnboardingFeatureActionViewActionNavigateToKs(navigateToViewAction)
            )
        }
    }

    private func handleNavigateToViewAction(_ viewAction: WelcomeOnboardingFeatureActionViewActionNavigateToKs) {
        let viewControllerToPresent: UIViewController = {
            switch viewAction {
            case .startScreen:
                let welcomeOnboardingStartView = WelcomeOnboardingStartView(
                    onCallToActionButtonTap: { [weak self] in
                        guard let self else {
                            return
                        }

                        self.viewModel.doStartJourneyAction()
                    }
                )
                return UIHostingController(rootView: welcomeOnboardingStartView)
            case .welcomeOnboardingQuestionnaire(let data):
                let questionnaireType = data.type

                let assembly = WelcomeOnboardingQuestionnaireAssembly(
                    type: questionnaireType,
                    onQuestionnaireItemTap: { [weak self] item in
                        guard let self else {
                            return
                        }

                        self.viewModel.doQuestionnaireItemAction(
                            questionnaireType: questionnaireType,
                            itemType: item.type
                        )
                    }
                )

                return UIHostingController(rootView: assembly.makeModule())
            case .chooseProgrammingLanguage:
                break
            case .trackDetails:
                break
            case .notificationOnboarding:
                break
            case .onboardingFinish:
                break
            }

            return UIViewController()
        }()

        assert(children.count <= 2)

        UIViewController.swapRootViewController(
            for: self,
            from: children.first,
            to: viewControllerToPresent
        )
    }
}
