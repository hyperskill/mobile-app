import Foundation

protocol StepQuizParsonsViewDataMapperCodeContentCacheProtocol: AnyObject {
    func get(for key: Int) -> StepQuizParsonsViewData.CodeContent?
    func set(codeContent: StepQuizParsonsViewData.CodeContent, for key: Int)
}

final class StepQuizParsonsViewDataMapperCodeContentCache: StepQuizParsonsViewDataMapperCodeContentCacheProtocol {
    static let shared = StepQuizParsonsViewDataMapperCodeContentCache()

    private lazy var cache: NSCache<NSNumber, CodeContentWrapper> = {
        let cache = NSCache<NSNumber, CodeContentWrapper>()
        cache.countLimit = 10
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

    func get(for key: Int) -> StepQuizParsonsViewData.CodeContent? {
        cache.object(forKey: key as NSNumber)?.wrappedValue
    }

    func set(codeContent: StepQuizParsonsViewData.CodeContent, for key: Int) {
        cache.setObject(CodeContentWrapper(wrappedValue: codeContent), forKey: key as NSNumber)
    }

    @objc
    private func clearCacheOnEnterBackground() {
        cache.removeAllObjects()
    }

    private final class CodeContentWrapper: NSObject {
        let wrappedValue: StepQuizParsonsViewData.CodeContent

        init(wrappedValue: StepQuizParsonsViewData.CodeContent) {
            self.wrappedValue = wrappedValue
            super.init()
        }
    }
}
