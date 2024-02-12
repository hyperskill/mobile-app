import Foundation

final class UsersQuestionnaireWidgetViewModel {
    weak var moduleOutput: UsersQuestionnaireWidgetOutputProtocol?

    func doCallToAction() {
        moduleOutput?.handleUsersQuestionnaireWidgetClicked()
    }

    func doCloseAction() {
        moduleOutput?.handleUsersQuestionnaireWidgetCloseClicked()
    }

    func logViewedEvent() {
        moduleOutput?.handleUsersQuestionnaireWidgetDidAppear()
    }
}
