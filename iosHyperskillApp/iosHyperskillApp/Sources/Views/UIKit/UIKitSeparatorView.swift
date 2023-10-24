import SnapKit
import UIKit

extension UIKitSeparatorView {
    struct Appearance {
        /// UITableView's default separator height.
        let height: CGFloat = 1.0
        /// UITableView's default separator color.
        var color = UIColor.separator
    }
}

/// View to make separator consistent appearance.
final class UIKitSeparatorView: UIView {
    let appearance: Appearance

    override var intrinsicContentSize: CGSize {
        CGSize(width: UIView.noIntrinsicMetric, height: self.appearance.height / UIScreen.main.scale)
    }

    init(frame: CGRect = .zero, appearance: Appearance = Appearance()) {
        self.appearance = appearance
        super.init(frame: frame)
        self.setupView()
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        self.invalidateIntrinsicContentSize()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setupView() {
        self.backgroundColor = self.appearance.color
    }
}
