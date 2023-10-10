import SwiftUI
import UIKit

private struct IntrospectScrollView: UIViewRepresentable {
    typealias UIViewType = UIKitIntrospectionView

    let customize: (UIScrollView) -> Void

    static func dismantleUIView(_ uiView: UIKitIntrospectionView, coordinator: Coordinator) {
        uiView.moveToWindowHandler = nil
        coordinator.customize = nil
        coordinator.scrollView = nil
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(customize: customize)
    }

    func makeUIView(context: Context) -> UIKitIntrospectionView {
        UIKitIntrospectionView()
    }

    func updateUIView(_ uiView: UIKitIntrospectionView, context: Context) {
        let coordinator = context.coordinator

        if context.coordinator.scrollView == nil {
            uiView.moveToWindowHandler = { [weak uiView, weak coordinator] in
                DispatchQueue.main.async {
                    guard let uiView, let coordinator else {
                        return
                    }

                    updateScrollView(uiView, coordinator: coordinator)
                }
            }
        } else {
            updateScrollView(uiView, coordinator: coordinator)
        }
    }

    private func updateScrollView(_ uiView: UIKitIntrospectionView, coordinator: Coordinator) {
        guard let scrollView = uiView.findAncestor(ofType: UIScrollView.self) else {
            return
        }

        coordinator.scrollView = scrollView
        coordinator.customize?(scrollView)
    }
}

// MARK: - IntrospectScrollView (Coordinator) -

extension IntrospectScrollView {
    class Coordinator {
        var customize: ((UIScrollView) -> Void)?

        weak var scrollView: UIScrollView?

        init(customize: @escaping (UIScrollView) -> Void) {
            self.customize = customize
        }
    }
}

// MARK: - View+IntrospectScrollView -

extension View {
    /// Finds the containing `UIScrollView` of a SwiftUI view.
    func introspectScrollView(customize: @escaping (UIScrollView) -> Void) -> some View {
        overlay(
            IntrospectScrollView(
                customize: customize
            )
            .frame(width: 0, height: 0)
        )
    }
}
