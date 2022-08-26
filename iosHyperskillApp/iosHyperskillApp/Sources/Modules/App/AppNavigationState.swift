import Foundation

struct AppNavigationState {
    var selectedTab = AppTabItem.home {
        didSet {
            onSelectedTabChanged?(oldValue, selectedTab)
        }
    }
    var onSelectedTabChanged: ((_ oldValue: AppTabItem, _ newValue: AppTabItem) -> Void)?

    var activeFullScreenModal: AppFullScreenModal?
}
