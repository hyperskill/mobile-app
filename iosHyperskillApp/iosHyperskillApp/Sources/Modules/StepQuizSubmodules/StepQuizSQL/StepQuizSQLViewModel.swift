import Foundation
import shared

final class StepQuizSQLViewModel: StepQuizCodeViewModel {
    override func createReply() -> Reply {
        Reply.Companion.shared.sql(sqlCode: viewData.code)
    }

    override func syncReply(code: String?) {
        let reply = Reply.Companion.shared.sql(sqlCode: code)
        moduleOutput?.handleChildQuizSync(reply: reply)
    }
}
