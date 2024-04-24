import Foundation

extension Thread {
    /// Invokes `block` on the main thread asynchronously
    /// or synchronously if called from the main thread.
    static func dispatchOnMainThread(_ block: @escaping () -> Void) {
        if Thread.isMainThread {
            block()
        } else {
            DispatchQueue.main.async(execute: block)
        }
    }
}
