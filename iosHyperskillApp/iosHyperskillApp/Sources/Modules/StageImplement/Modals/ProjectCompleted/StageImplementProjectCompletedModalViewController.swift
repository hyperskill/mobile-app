import SwiftUI

protocol StageImplementProjectCompletedModalViewControllerDelegate: AnyObject {
    func stageImplementProjectCompletedModalViewControllerDidAppear(
        _ viewController: StageImplementProjectCompletedModalViewController
    )
    func stageImplementProjectCompletedModalViewControllerDidDisappear(
        _ viewController: StageImplementProjectCompletedModalViewController
    )
    func stageImplementProjectCompletedModalViewControllerDidTapCallToActionButton(
        _ viewController: StageImplementProjectCompletedModalViewController
    )
}

final class StageImplementProjectCompletedModalViewController: PanModalSwiftUIViewController<
  StageImplementProjectCompletedModalView
> {
    private weak var delegate: StageImplementProjectCompletedModalViewControllerDelegate?

    convenience init(
        stageAward: Int,
        projectAward: Int,
        delegate: StageImplementProjectCompletedModalViewControllerDelegate?
    ) {
        var view = StageImplementProjectCompletedModalView(stageAward: stageAward, projectAward: projectAward)

        self.init(
            isPresented: .init(get: { false }, set: { _ in }),
            content: { view }
        )

        view.onCallToActionTap = { [weak self] in
            guard let strongSelf = self else {
                return
            }

            strongSelf.delegate?.stageImplementProjectCompletedModalViewControllerDidTapCallToActionButton(strongSelf)
            strongSelf.dismiss(animated: true)
        }

        self.delegate = delegate
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        delegate?.stageImplementProjectCompletedModalViewControllerDidAppear(self)
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        delegate?.stageImplementProjectCompletedModalViewControllerDidDisappear(self)
    }
}
