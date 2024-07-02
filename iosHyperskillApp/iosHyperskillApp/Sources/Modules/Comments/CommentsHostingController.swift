import SwiftUI

final class CommentsHostingController: UIHostingController<CommentsView> {
    private lazy var closeBarButtonItem = UIBarButtonItem(
        barButtonSystemItem: .close,
        target: self,
        action: #selector(closeButtonClicked)
    )

    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.rightBarButtonItem = closeBarButtonItem
    }

    @objc
    private func closeButtonClicked() {
        dismiss(animated: true, completion: nil)
    }
}
