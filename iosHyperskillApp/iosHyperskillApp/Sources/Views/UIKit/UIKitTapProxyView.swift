import UIKit

class UIKitTapProxyView: UIView {
    var targetView: UIView?

    convenience init(targetView: UIView) {
        self.init()
        self.targetView = targetView
    }

    override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        bounds.contains(point) ? targetView : nil
    }
}
