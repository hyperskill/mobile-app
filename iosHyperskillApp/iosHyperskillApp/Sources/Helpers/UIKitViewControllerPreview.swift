import SwiftUI
import UIKit

struct UIKitViewControllerPreview: UIViewControllerRepresentable {
    private let viewControllerBuilder: () -> UIViewController

    init(_ viewControllerBuilder: @escaping () -> UIViewController) {
        self.viewControllerBuilder = viewControllerBuilder
    }

    func makeUIViewController(context: Context) -> some UIViewController { viewControllerBuilder() }

    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
        // no op
    }
}
