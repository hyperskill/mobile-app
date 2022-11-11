import UIKit

final class ViewRelatedEventsViewController: UIViewController {
    private var applicationDidTransitionToBackground = false

    var onViewWillAppear: (() -> Void)?
    var onViewDidAppear: (() -> Void)?

    var onViewWillDisappear: (() -> Void)?
    var onViewDidDisappear: (() -> Void)?

    init() {
        super.init(nibName: nil, bundle: nil)

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: UIApplication.shared
        )

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationWillResignActive),
            name: UIApplication.willResignActiveNotification,
            object: UIApplication.shared
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationDidEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: UIApplication.shared
        )
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    // MARK: Responding to view-related events

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        onViewWillAppear?()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        onViewDidAppear?()
    }

    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        onViewWillDisappear?()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        onViewDidDisappear?()
    }

    // MARK: Private API

    @objc
    private func handleApplicationWillEnterForeground() {
        guard applicationDidTransitionToBackground && isVisible else {
            return
        }

        onViewWillAppear?()
    }

    @objc
    private func handleApplicationDidBecomeActive() {
        guard applicationDidTransitionToBackground else {
            return
        }

        applicationDidTransitionToBackground = false

        if isVisible {
            onViewDidAppear?()
        }
    }

    @objc
    private func handleApplicationWillResignActive() {
        applicationDidTransitionToBackground = true

        if isVisible {
            onViewWillDisappear?()
        }
    }

    @objc
    private func handleApplicationDidEnterBackground() {
        applicationDidTransitionToBackground = true

        if isVisible {
            onViewDidDisappear?()
        }
    }
}
