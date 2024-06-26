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
            viewModel.doCompleteOnboarding(stepRoute: data.stepRoute)
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
                let startView = WelcomeOnboardingStartView(
                    onViewDidAppear: { [weak self] in
                        self?.viewModel.doStartOnboardingViewed()
                    },
                    onCallToActionButtonTap: { [weak self] in
                        self?.viewModel.doStartJourneyAction()
                    }
                )
                return UIHostingController(rootView: startView)
            case .welcomeOnboardingQuestionnaire(let data):
                let questionnaireType = data.type

                let assembly = WelcomeOnboardingQuestionnaireAssembly(
                    type: questionnaireType,
                    onViewDidAppear: { [weak self] in
                        self?.viewModel.doQuestionnaireViewed(questionnaireType: questionnaireType)
                    },
                    onQuestionnaireItemTap: { [weak self] item in
                        self?.viewModel.doQuestionnaireItemAction(
                            questionnaireType: questionnaireType,
                            itemType: item.type
                        )
                    }
                )

                return UIHostingController(rootView: assembly.makeModule())
            case .chooseProgrammingLanguage:
                let chooseProgrammingLanguageView = WelcomeOnboardingChooseProgrammingLanguageView(
                    onViewDidAppear: { [weak self] in
                        self?.viewModel.doSelectProgrammingLanguageViewed()
                    },
                    onProgrammingLanguageTap: { [weak self] language in
                        self?.viewModel.doSelectProgrammingLanguage(language: language)
                    }
                )
                return UIHostingController(rootView: chooseProgrammingLanguageView)
            case .trackDetails(let data):
                let assembly = WelcomeOnboardingTrackDetailsAssembly(
                    track: data.track,
                    moduleOutput: viewModel
                )
                return assembly.makeModule()
            case .notificationOnboarding:
                let assembly = NotificationsOnboardingAssembly(output: viewModel)
                return assembly.makeModule()
            case .onboardingFinish(let data):
                let assembly = WelcomeOnboardingFinishAssembly(
                    track: data.selectedTrack,
                    onViewDidAppear: { [weak self] in
                        self?.viewModel.doFinishOnboardingViewed()
                    },
                    onCallToActionButtonTap: { [weak self] in
                        self?.viewModel.doFinishOnboarding()
                    }
                )
                return UIHostingController(rootView: assembly.makeModule())
            }
        }()

        assert(children.count <= 2)

        UIViewController.swapRootViewController(
            for: self,
            from: children.first,
            to: viewControllerToPresent
        )
    }
}
