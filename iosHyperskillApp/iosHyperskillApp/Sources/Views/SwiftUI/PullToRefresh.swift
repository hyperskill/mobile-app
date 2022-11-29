import SwiftUI
import UIKit

private struct PullToRefresh: UIViewRepresentable {
    typealias UIViewType = UIKitIntrospectionView

    @Binding var isShowing: Bool

    let onRefresh: () -> Void

    // MARK: UIViewRepresentable

    func makeCoordinator() -> Coordinator {
        Coordinator(isShowing: $isShowing, onRefresh: onRefresh)
    }

    func makeUIView(context: Context) -> UIKitIntrospectionView {
        UIKitIntrospectionView()
    }

    func updateUIView(_ uiView: UIKitIntrospectionView, context: Context) {
        /// When `updateUiView` is called after creating the Introspection view, it is not yet in the UIKit hierarchy.
        /// At this point, `introspectionView.superview.superview` is nil and we can't access the target UIKit view.
        /// To workaround this, we wait until the runloop is done inserting the introspection view in the hierarchy, then run the selector.
        /// Finding the target view fails silently if the selector yield no result. This happens when `updateUIView`
        /// gets called when the introspection view gets removed from the hierarchy.
        if context.coordinator.refreshControl == nil {
            uiView.moveToWindowHandler = {
                DispatchQueue.main.async {
                    updateRefreshControl(uiView, context: context)
                }
            }
        } else {
            updateRefreshControl(uiView, context: context)
        }
    }

    private func updateRefreshControl(_ uiView: UIKitIntrospectionView, context: Context) {
        // View disappeared -> endRefreshing
        if uiView.superview == nil {
            context.coordinator.refreshControl?.endRefreshing()
        }

        guard let scrollView = uiView.findAncestor(ofType: UIScrollView.self) else {
            return
        }

        if let refreshControl = scrollView.refreshControl {
            if isShowing {
                refreshControl.beginRefreshing()
            } else {
                refreshControl.endRefreshing()
            }

            context.coordinator.refreshControl = refreshControl
        } else {
            let refreshControl = UIRefreshControl()
            refreshControl.addTarget(
                context.coordinator,
                action: #selector(Coordinator.refreshControlValueChanged),
                for: .valueChanged
            )

            scrollView.refreshControl = refreshControl
            context.coordinator.refreshControl = refreshControl
        }
    }
}

// MARK: - PullToRefresh (Coordinator) -

extension PullToRefresh {
    class Coordinator {
        let isShowing: Binding<Bool>

        let onRefresh: () -> Void

        weak var refreshControl: UIRefreshControl?

        init(isShowing: Binding<Bool>, onRefresh: @escaping () -> Void) {
            self.onRefresh = onRefresh
            self.isShowing = isShowing
        }

        @objc
        func refreshControlValueChanged() {
            isShowing.wrappedValue = true
            onRefresh()
        }
    }
}

// MARK: - View+PullToRefresh -

extension View {
    func pullToRefresh(isShowing: Binding<Bool>, onRefresh: @escaping () -> Void) -> some View {
        overlay(
            PullToRefresh(
                isShowing: isShowing,
                onRefresh: onRefresh
            )
            .frame(width: 0, height: 0)
        )
    }
}
