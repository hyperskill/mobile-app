import Foundation

protocol StepQuizFillBlanksViewDataMapperCacheProtocol: AnyObject {
    func getHighlightedCode(for key: Int) -> NSAttributedString?
    func setHighlightedCode(_ code: NSAttributedString, for key: Int)

    func getHTMLUnescapedString(for key: Int) -> String?
    func setHTMLUnescapedString(_ string: String, for key: Int)
}

final class StepQuizFillBlanksViewDataMapperCache: StepQuizFillBlanksViewDataMapperCacheProtocol {
    static let shared = StepQuizFillBlanksViewDataMapperCache()

    private let highlightedCodeCache = NSCache<NSNumber, NSAttributedString>()
    private let htmlUnescapedStringCache = NSCache<NSNumber, NSString>()

    private init() {
        highlightedCodeCache.countLimit = 50
        htmlUnescapedStringCache.countLimit = 50

        NotificationCenter.default.addObserver(
            self,
            selector: #selector(clearCacheOnEnterBackground),
            name: UIApplication.didEnterBackgroundNotification,
            object: nil
        )
    }

    func getHighlightedCode(for key: Int) -> NSAttributedString? {
        highlightedCodeCache.object(forKey: key as NSNumber)
    }

    func setHighlightedCode(_ code: NSAttributedString, for key: Int) {
        highlightedCodeCache.setObject(code, forKey: key as NSNumber)
    }

    func getHTMLUnescapedString(for key: Int) -> String? {
        htmlUnescapedStringCache.object(forKey: key as NSNumber) as String?
    }

    func setHTMLUnescapedString(_ string: String, for key: Int) {
        htmlUnescapedStringCache.setObject(string as NSString, forKey: key as NSNumber)
    }

    @objc
    private func clearCacheOnEnterBackground() {
        highlightedCodeCache.removeAllObjects()
        htmlUnescapedStringCache.removeAllObjects()
    }
}
