import Foundation
import shared

extension KotlinThrowable {
    func asError() -> Error {
        do {
            try RethrowThrowableKt.rethrow(exception: self)
        } catch {
            return error
        }

        fatalError("RethrowThrowableKt :: should not reach here")
    }
}
