import Foundation

final class AppNavigationState: ObservableObject {
    @Published var selectedTab = AppTabItem.home

    @Published var presentingAuthScreen = false
}
