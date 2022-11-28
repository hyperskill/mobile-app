import UIKit

extension UIView {
    /// Finds an ancestor of the specified type.
    /// If it reaches the top of the view without finding the specified view type, it returns nil.
    func findAncestor<AnyViewType: UIView>(ofType type: AnyViewType.Type) -> AnyViewType? {
        var superview = superview
        while let itSuperview = superview {
            if let typed = itSuperview as? AnyViewType {
                return typed
            }
            superview = itSuperview.superview
        }
        return nil
    }
}
