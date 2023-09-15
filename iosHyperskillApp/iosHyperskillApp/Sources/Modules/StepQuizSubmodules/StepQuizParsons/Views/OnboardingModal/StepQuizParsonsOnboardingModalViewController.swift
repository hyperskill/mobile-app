import Foundation
import shared
import SwiftUI

protocol StepQuizParsonsOnboardingModalViewControllerDelegate: AnyObject {
    func stepQuizParsonsOnboardingModalViewControllerDidAppear(
        _ viewController: StepQuizParsonsOnboardingModalViewController
    )
    func stepQuizParsonsOnboardingModalViewControllerDidDisappear(
        _ viewController: StepQuizParsonsOnboardingModalViewController
    )
}

final class StepQuizParsonsOnboardingModalViewController: PanModalSwiftUIViewController<
  StepQuizParsonsOnboardingModalView
> {
    weak var delegate: StepQuizParsonsOnboardingModalViewControllerDelegate?

    override var shouldUpdateAdditionalSafeAreaInsets: Bool { false }

    init(delegate: StepQuizParsonsOnboardingModalViewControllerDelegate?) {
        self.delegate = delegate
        super.init(
            isPresented: .constant(false),
            content: { StepQuizParsonsOnboardingModalView() }
        )
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.stepQuizParsonsOnboardingModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.stepQuizParsonsOnboardingModalViewControllerDidDisappear(self)
    }
}
