import Foundation
import shared
import SwiftUI

protocol StepQuizProblemOnboardingModalViewControllerDelegate: AnyObject {
    func stepQuizProblemOnboardingModalViewControllerDidAppear(
        _ viewController: StepQuizProblemOnboardingModalViewController,
        modalType: StepQuizFeatureProblemOnboardingModalKs
    )
    func stepQuizProblemOnboardingModalViewControllerDidDisappear(
        _ viewController: StepQuizProblemOnboardingModalViewController,
        modalType: StepQuizFeatureProblemOnboardingModalKs
    )
}

final class StepQuizProblemOnboardingModalViewController: PanModalSwiftUIViewController<
  StepQuizProblemOnboardingModalView
> {
    weak var delegate: StepQuizProblemOnboardingModalViewControllerDelegate?

    private let modalType: StepQuizFeatureProblemOnboardingModalKs

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(
        modalType: StepQuizFeatureProblemOnboardingModalKs,
        delegate: StepQuizProblemOnboardingModalViewControllerDelegate?
    ) {
        self.modalType = modalType
        self.delegate = delegate
        super.init(
            isPresented: .constant(false),
            content: { StepQuizProblemOnboardingModalView(modalType: modalType) }
        )
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.stepQuizProblemOnboardingModalViewControllerDidAppear(self, modalType: modalType)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.stepQuizProblemOnboardingModalViewControllerDidDisappear(self, modalType: modalType)
    }
}
