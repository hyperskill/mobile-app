import SwiftUI
import UIKit

private struct IntrospectViewController: UIViewRepresentable {
    typealias UIViewType = UIKitIntrospectionView

    let customize: (UIViewController) -> Void

    // MARK: UIViewRepresentable

    static func dismantleUIView(_ uiView: UIKitIntrospectionView, coordinator: Coordinator) {
        uiView.moveToWindowHandler = nil
        coordinator.customize = nil
        coordinator.viewController = nil
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(customize: customize)
    }

    func makeUIView(context: Context) -> UIKitIntrospectionView {
        UIKitIntrospectionView()
    }

    func updateUIView(_ uiView: UIKitIntrospectionView, context: Context) {
        let coordinator = context.coordinator

        if context.coordinator.viewController == nil {
            uiView.moveToWindowHandler = { [weak uiView, weak coordinator] in
                DispatchQueue.main.async {
                    guard let uiView, let coordinator else {
                        return
                    }

                    updateViewController(uiView, coordinator: coordinator)
                }
            }
        } else {
            updateViewController(uiView, coordinator: coordinator)
        }
    }

    private func updateViewController(_ uiView: UIKitIntrospectionView, coordinator: Coordinator) {
        guard let viewController = uiView.findViewController() else {
            return
        }

        coordinator.viewController = viewController
        coordinator.customize?(viewController)
    }
}

// MARK: - IntrospectViewController (Coordinator) -

extension IntrospectViewController {
    class Coordinator {
        var customize: ((UIViewController) -> Void)?

        weak var viewController: UIViewController?

        init(customize: @escaping (UIViewController) -> Void) {
            self.customize = customize
        }
    }
}

// MARK: - View+IntrospectViewController -

extension View {
    /// Finds the containing `UIViewController` of a SwiftUI view.
    func introspectViewController(customize: @escaping (UIViewController) -> Void) -> some View {
        overlay(
            IntrospectViewController(
                customize: customize
            )
            .frame(width: 0, height: 0)
        )
    }

    /// Finds the containing `UIHostingController` of a SwiftUI view.
    func introspectHostingController<RootView: View>(
        customize: @escaping (UIHostingController<RootView>) -> Void
    ) -> some View {
        overlay(
            IntrospectViewController(
                customize: { viewController in
                    if let hostingController = viewController as? UIHostingController<RootView> {
                        customize(hostingController)
                    }
                }
            )
            .frame(width: 0, height: 0)
        )
    }
}
