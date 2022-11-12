import CombineSchedulers
import SwiftUI

private struct PullToRefresh: UIViewRepresentable {
    typealias UIViewType = UIView

    @Binding var isShowing: Bool

    let onRefresh: () -> Void

    // MARK: UIViewRepresentable

    func makeCoordinator() -> Coordinator {
        Coordinator(isShowing: $isShowing, onRefresh: onRefresh)
    }

    func makeUIView(context: Context) -> UIView {
        let view = UIView()
        view.isHidden = true
        view.isUserInteractionEnabled = false
        return view
    }

    func updateUIView(_ uiView: UIView, context: Context) {
        context.coordinator.mainScheduler.schedule {
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
}

// MARK: - PullToRefresh (Coordinator) -

extension PullToRefresh {
    class Coordinator {
        let isShowing: Binding<Bool>

        let onRefresh: () -> Void

        let mainScheduler: AnySchedulerOf<RunLoop>

        weak var refreshControl: UIRefreshControl?

        init(
            isShowing: Binding<Bool>,
            onRefresh: @escaping () -> Void,
            mainScheduler: AnySchedulerOf<RunLoop> = .main
        ) {
            self.onRefresh = onRefresh
            self.isShowing = isShowing
            self.mainScheduler = mainScheduler
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
