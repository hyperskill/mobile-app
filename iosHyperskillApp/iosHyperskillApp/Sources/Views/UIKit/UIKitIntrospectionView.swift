import UIKit

/// Introspection UIView that is inserted alongside the target view.
public class UIKitIntrospectionView: UIView {
    var moveToWindowHandler: (() -> Void)?

    required init() {
        super.init(frame: .zero)
        isHidden = true
        isUserInteractionEnabled = false
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override public func didMoveToWindow() {
        super.didMoveToWindow()
        moveToWindowHandler?()
    }
}
