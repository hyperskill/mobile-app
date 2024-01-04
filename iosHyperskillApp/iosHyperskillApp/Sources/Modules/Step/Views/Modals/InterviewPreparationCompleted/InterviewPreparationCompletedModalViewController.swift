import SwiftUI

protocol InterviewPreparationCompletedModalViewControllerDelegate: AnyObject {
    func interviewPreparationCompletedModalViewControllerDidAppear(
        _ viewController: InterviewPreparationCompletedModalViewController
    )
    func interviewPreparationCompletedModalViewControllerDidDisappear(
        _ viewController: InterviewPreparationCompletedModalViewController
    )
    func interviewPreparationCompletedModalViewControllerDidTapCallToActionButton(
        _ viewController: InterviewPreparationCompletedModalViewController
    )
}

final class InterviewPreparationCompletedModalViewController: PanModalSwiftUIViewController<
  InterviewPreparationCompletedModalView
> {
    private weak var delegate: InterviewPreparationCompletedModalViewControllerDelegate?

    convenience init(delegate: InterviewPreparationCompletedModalViewControllerDelegate?) {
        var view = InterviewPreparationCompletedModalView()

        self.init(
            isPresented: .init(get: { false }, set: { _ in }),
            content: { view }
        )

        view.onCallToActionTap = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.interviewPreparationCompletedModalViewControllerDidTapCallToActionButton(strongSelf)
            strongSelf.dismiss(animated: true)
        }

        self.delegate = delegate
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.interviewPreparationCompletedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.interviewPreparationCompletedModalViewControllerDidDisappear(self)
    }
}
