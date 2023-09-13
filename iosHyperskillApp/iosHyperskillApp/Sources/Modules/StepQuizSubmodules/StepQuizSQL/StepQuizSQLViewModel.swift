import Foundation
import shared

final class StepQuizSQLViewModel: StepQuizCodeViewModel {
    override func createReply() -> Reply {
        Reply(solveSql: viewData.code)
    }

    override func syncReply(code: String?) {
        let reply = Reply(solveSql: code)
        moduleOutput?.handleChildQuizSync(reply: reply)
    }
}
