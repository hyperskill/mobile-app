import SwiftUI

struct UIViewControllerEventsWrapper: UIViewControllerRepresentable {
    var onViewWillAppear: (() -> Void)?
    var onViewDidAppear: (() -> Void)?

    var onViewWillDisappear: (() -> Void)?
    var onViewDidDisappear: (() -> Void)?

    static func dismantleUIViewController(_ uiViewController: ViewRelatedEventsViewController, coordinator: ()) {
        uiViewController.onViewWillAppear = nil
        uiViewController.onViewDidAppear = nil
        uiViewController.onViewWillDisappear = nil
        uiViewController.onViewDidDisappear = nil
    }

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
