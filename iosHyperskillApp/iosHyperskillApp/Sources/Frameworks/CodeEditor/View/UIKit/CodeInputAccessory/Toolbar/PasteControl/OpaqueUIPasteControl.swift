import UIKit

@available(iOS 16.0, *)
final class OpaqueUIPasteControl: UIPasteControl {
    private var observation: NSKeyValueObservation?

    override func removeFromSuperview() {
        super.removeFromSuperview()
        observation = nil
    }

    override func didAddSubview(_ subview: UIView) {
        super.didAddSubview(subview)

        guard observation == nil else {
            return
        }

        observation = subview.observe(\.alpha, options: [.new]) { [weak self] view, change in
            guard let strongSelf = self else {
                return
            }

            if strongSelf.isHighlighted {
                return
            }
            if change.newValue != 1 {
                view.alpha = 1
            }
        }
    }
}
