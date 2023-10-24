import Foundation

protocol StepQuizFillBlanksViewDataMapperCacheProtocol: AnyObject {
    func getHighlightedCode(for key: Int) -> NSAttributedString?
    func setHighlightedCode(_ code: NSAttributedString, for key: Int)
}

final class StepQuizFillBlanksViewDataMapperCache: StepQuizFillBlanksViewDataMapperCacheProtocol {
    static let shared = StepQuizFillBlanksViewDataMapperCache()

    private lazy var cache: NSCache<NSNumber, NSAttributedString> = {
        let cache = NSCache<NSNumber, NSAttributedString>()
        cache.countLimit = 50
        return cache
    }()

    private init() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(clearCacheOnEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil
        )
    }

    func getHighlightedCode(for key: Int) -> NSAttributedString? {
        cache.object(forKey: key as NSNumber)
    }

    func setHighlightedCode(_ code: NSAttributedString, for key: Int) {
        cache.setObject(code, forKey: key as NSNumber)
    }

    @objc
    private func clearCacheOnEnterBackground() {
        cache.removeAllObjects()
    }
}
