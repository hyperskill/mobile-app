import Foundation
import shared

final class StepQuizPyCharmViewModel: StepQuizCodeViewModel {
    override func createReply() -> Reply {
        Reply.Companion.shared.pycharm(step: step, pycharmCode: viewData.code)
    }

    override func syncReply(code: String?) {
        let reply = Reply.Companion.shared.pycharm(step: step, pycharmCode: code)
        moduleOutput?.handleChildQuizSync(reply: reply)
    }
}
