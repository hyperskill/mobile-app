import SwiftUI

struct UIViewControllerEventsWrapper: UIViewControllerRepresentable {
    var onViewWillAppear: (() -> Void)?
    var onViewDidAppear: (() -> Void)?

    var onViewWillDisappear: (() -> Void)?
    var onViewDidDisappear: (() -> Void)?

    func makeUIViewController(context: Context) -> ViewRelatedEventsViewController {
        ViewRelatedEventsViewController()
    }

    func updateUIViewController(_ uiViewController: ViewRelatedEventsViewController, context: Context) {
        uiViewController.onViewWillAppear = onViewWillAppear
        uiViewController.onViewDidAppear = onViewDidAppear
        uiViewController.onViewWillDisappear = onViewWillDisappear
        uiViewController.onViewDidDisappear = onViewDidDisappear
    }
}
