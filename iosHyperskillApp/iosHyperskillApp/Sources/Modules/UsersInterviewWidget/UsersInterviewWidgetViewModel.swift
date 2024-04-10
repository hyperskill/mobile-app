import Foundation

final class UsersInterviewWidgetViewModel {
    weak var moduleOutput: UsersInterviewWidgetOutputProtocol?

    func doCallToAction() {
        moduleOutput?.handleUsersInterviewWidgetClicked()
    }

    func doCloseAction() {
        moduleOutput?.handleUsersInterviewWidgetCloseClicked()
    }

    func logViewedEvent() {
        moduleOutput?.handleUsersInterviewWidgetDidAppear()
    }
}
