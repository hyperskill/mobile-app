import UIKit

extension UIViewController {
    var isVisible: Bool {
        isViewLoaded && view.window != nil
    }
}
