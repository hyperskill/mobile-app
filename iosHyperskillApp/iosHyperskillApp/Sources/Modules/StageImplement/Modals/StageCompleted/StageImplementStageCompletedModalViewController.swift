import SwiftUI

protocol StageImplementStageCompletedModalViewControllerDelegate: AnyObject {
    func stageImplementStageCompletedModalViewControllerDidAppear(
        _ viewController: StageImplementStageCompletedModalViewController
    )
    func stageImplementStageCompletedModalViewControllerDidDisappear(
        _ viewController: StageImplementStageCompletedModalViewController
    )
    func stageImplementStageCompletedModalViewControllerDidTapCallToActionButton(
        _ viewController: StageImplementStageCompletedModalViewController
    )
}

final class StageImplementStageCompletedModalViewController: PanModalSwiftUIViewController<
  StageImplementStageCompletedModalView
> {
    private weak var delegate: StageImplementStageCompletedModalViewControllerDelegate?

    convenience init(
        title: String,
        award: Int,
        delegate: StageImplementStageCompletedModalViewControllerDelegate?
    ) {
        var view = StageImplementStageCompletedModalView(title: title, award: award)

        self.init(
            isPresented: .init(get: { false }, set: { _ in }),
            content: { view }
        )

        view.onCallToActionTap = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.stageImplementStageCompletedModalViewControllerDidTapCallToActionButton(strongSelf)
            strongSelf.dismiss(animated: true)
        }

        self.delegate = delegate
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.stageImplementStageCompletedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.stageImplementStageCompletedModalViewControllerDidDisappear(self)
    }
}
